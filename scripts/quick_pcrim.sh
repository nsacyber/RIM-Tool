#!/bin/bash

##############################################################################################
#
# quick_pcrim.sh 
#    
# Makes a test PC Client RIM Bundle.
#  Writes the test files to /opt/rimtool/data/local
#
############################################################################################

DATA_OUT_DIR="/opt/rimtool/data/local"
#DATA_IN_DIR=""
TPM_EVENT_LOG="binary_bios_measurements";
DEFAULT_SRIM="/sys/kernel/security/tpm0/$TPM_EVENT_LOG"
RIM_CONF="rim_fields.json"
ORIG_CONFIG_FILE="/opt/rimtool/data/pcrim/rim_fields.json"
USE_RM_FILE=NO
RIM_TAG=$(uuidgen)
RIM_EXE="/usr/local/bin/rim"
VERBOSE=false
DAYS=365

# PKI
PKI_ROOT="RIM_Test_RSA_Root"
PKI_ROOT_DN="/C=US/ST=MD/O=example.com/CN=Test_PC_RIM_RootCA"
RIM_SIGNER_DN="/C=US/ST=MD/O=example.com/CN=Test_PC_RIM_SIGNER"

# Certificate params
RIM_SIGNER="RIM_Test_Signer"
RIM_SIGNER_CERT="BaseRIMTestCA.example.com.pem"
SIG_ALG="rsa:2048"
DAYS_VALID="3652"
SUBJECT_DN="/C=US/O=example.com/OU=PC_RIM_Test"
RIM_KEY="key.pem"

check_admin () {
	if [ "$EUID" -ne 0 ]; then
     echo "Error: quick_prcrim.sh requires root privileges.  Please run as root" 
     exit 1
fi
}

check_prereq () {
	if [ ! -f ${RIM_EXE} ]; then 
     echo "Error: Rim tool executable not found."
     echo "Please install the RIM_Tool and try again." 
     exit 1	
	fi
    if [ ! command -v openssl &> /dev/null ]; then 
     echo "OpenSSL is not installed."
     exit 1;
    fi
}

check_error () {
	if [ "$1" -ne 0 ]; then 
		echo "Error: $2"
		popd
		exit 1;
	fi
}

help () {
  echo "  Create a quick PC Client Rim Bundle based upon your local device"
  echo "  Syntax: sh quick_pcrim.sh [-h|--help|-l |--rimel]"
  echo "  options:"
  echo "     -h  | --help   Print this help"
  echo "     -l  | --rimel  Optional path to the PC Client support RIM file"
  echo "     -v  | --verbose  Verbose output"
  echo
}

## Process parameters
# Process parameters Argument handling 
POSITIONAL_ARGS=()
#ORIGINAL_ARGS=("$@")
while [[ $# -gt 0 ]]; do
  case $1 in
    -l|--rimel)
      USE_RIMEL_FILE=true
      shift # past argument
      RIMEL_PATH=$@
      if [ -z ${RIMEL_PATH} ]; then
          echo "Error: -l requires a file path";
          exit 1;
       else
          shift # past parameter
       fi
      ;;
    -v|--verbose)
      VERBOSE=true
      shift # past argument
      ;;
    -h|--help)
      help
      exit 0
      shift # past argument
      ;;
    -*|--*)
      echo "aca_setup.sh: Unknown option $1"
      help
      exit 1
      ;;
    *)
     POSITIONAL_ARGS+=("$1") # save positional arg
     # shift # past argument
     break
      ;;
  esac
done

# Many files and function used require admin access, so check and exit if not provided
check_admin
# Make sure RIM Tool and openssl are installed 
check_prereq
# Get require data for specific device (requires admin)
OEM="$(cat /sys/devices/virtual/dmi/id/sys_vendor)";
OEM_NO_SPACES=$(cat /sys/devices/virtual/dmi/id/sys_vendor| tr -d '[:space:]');
MODEL="$(cat /sys/devices/virtual/dmi/id/product_name)";
MODEL_NO_SPACES=$(cat /sys/devices/virtual/dmi/id/product_name| tr -d '[:space:]');
OEM_URL=$OEM_NO_SPACES.com
SUPPORT_RIM=$OEM_NO_SPACES.$MODEL_NO_SPACES.1.rimel
BASE_RIM=$OEM_NO_SPACES.$MODEL_NO_SPACES.1.swidtag

mkdir -p ${DATA_OUT_DIR}
chmod 777 ${DATA_OUT_DIR}


pushd ${DATA_OUT_DIR} > /dev/null
# Remove existing RIM file if they exist
rm -f ${BASE_RIM}
rm -f ${SUPPORT_RIM}

# Step 1 Obtain the Support RIM file...
if [ -z "${USE_RIMEL_FILE}" ]; then
  echo "Using ${DEFAULT_SRIM} as the Support RIM file..."
  cp ${DEFAULT_SRIM} ${DATA_OUT_DIR}/${SUPPORT_RIM}
  else
    if [ ! -f ${RIMEL_PATH} ]; then
      echo "Error: file ${RIMEL_PATH} for use with -l parameter does  not exist"
      exit 1;
    fi
    echo "Using provided support RIM ${RIMEL_PATH} as the Support RIM file..."
    echo "Storing ${RIMEL_PATH} as ${SUPPORT_RIM} for use as the Support RIM file..."
    cp ${RIMEL_PATH} ${DATA_OUT_DIR}/${SUPPORT_RIM}
fi
chmod 777 ${DATA_OUT_DIR}/${SUPPORT_RIM}

# Step 2: Obtain and modify a config file using OS provided parameters
cp  ${ORIG_CONFIG_FILE} ${DATA_OUT_DIR}/${RIM_CONF}
# Update the config file with local mfg and model
sed -i "s/Example.com/$OEM/g" ${RIM_CONF};
sed -i "s/ProductA/$MODEL/g" ${RIM_CONF};
sed -i "s/ExampleUrl/$OEM_URL/g" ${RIM_CONF};
# Update the tagid so the ACA will not reject the upload if a RIM with the same tag exists 
jq --arg new_val "${RIM_TAG}" '.SoftwareIdentity.tagId = $new_val' "${RIM_CONF}" > temp.json && mv temp.json "${RIM_CONF}"
chmod 777 ${RIM_CONF}

# Step 3, set up the keys/certs needed for Base RIM Creation

# Root CA
if [ ! -f ${PKI_ROOT}.pem ]; then
    echo "Generating RSA Root CA Certificate...."
    openssl genrsa -out ${PKI_ROOT}.key
    openssl req -new -x509 -nodes -sha256 -days ${DAYS} -key ${PKI_ROOT}.key -subj ${PKI_ROOT_DN} -out ${PKI_ROOT}.pem 
    check_error $? "Generating Root CA Certificate"
  else
    echo "Using Exsiting Root Certificate ${PKI_ROOT}.pem "
fi 

if ${VERBOSE}; then openssl x509 -in ${PKI_ROOT}.pem -noout -text; fi;

# RIM Signing Cert
if [ ! -f ${RIM_SIGNER}.pem ]; then
    echo "Generating RSA RIM Signing Certificate ...." 
    openssl genrsa -out ${RIM_SIGNER}.key 3072 > /dev/null 2>&1
    openssl req -new -key ${RIM_SIGNER}.key -sha256 -subj ${RIM_SIGNER_DN} -out ${RIM_SIGNER}.csr > /dev/null 2>&1
    openssl x509 -req -in ${RIM_SIGNER}.csr -CA ${PKI_ROOT}.pem -CAkey ${PKI_ROOT}.key -CAcreateserial -days ${DAYS} -sha256 -out ${RIM_SIGNER}.pem > /dev/null 2>&1 
    check_error $? "Generating RIM Signing Certificate"
    rm ${RIM_SIGNER}.csr
  else
    echo "Using Exsiting Rim Signing Certificate ${RIM_SIGNER}.pem "  
fi

if ${VERBOSE}; then openssl x509 -in ${RIM_SIGNER}.pem -noout -text; fi;

chmod 777 ${PKI_ROOT}.key ${PKI_ROOT}.pem

# Step 4 create the new PC Client Base RIM file
echo "Creating new Base RIM ..... "

echo "rim create -r pcrim -c ${RIM_CONF} -e -k ${RIM_SIGNER}.key -p ${RIM_SIGNER}.pem -l ${SUPPORT_RIM} -o ${BASE_RIM}"
${RIM_EXE} create -r pcrim -c ${RIM_CONF} -e -k ${RIM_SIGNER}.key -p ${RIM_SIGNER}.pem -l ${SUPPORT_RIM} -o ${BASE_RIM}
check_error $? "Generating Base RIM File ${BASE_RIM}"

if ${VERBOSE}; then cat ${BASE_RIM}; fi;

chmod 777 ${BASE_RIM}
# Step 5 Verify the RIM

${RIM_EXE} verify -r pcrim -p ${RIM_SIGNER}.pem   --in ${BASE_RIM}
check_error $? "Verifying Base RIM File ${BASE_RIM}"

if [[ $? -eq 0 ]]; then
    echo "${BASE_RIM} and ${SUPPORT_RIM} can be found at ${DATA_OUT_DIR}"
fi

popd > /dev/null
echo "Done."
