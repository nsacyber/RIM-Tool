#!/bin/bash

##############################################################################################
#
# quick_pcrim.sh 
#    
# Makes a test PC Client RIM Bundle.
#
############################################################################################

DATA_OUT_DIR="/opt/rimtool/data/local"
DATA_IN_DIR=""
TPM_EVENT_LOG="binary_bios_measurements";
DEFAULT_SRIM="/sys/kernel/security/tpm0/$TPM_EVENT_LOG"
RIM_CONF="rim_fields.json"
ORIG_CONFIG_FILE="../pcrim/rim_fields.json"
USE_RM_FILE=NO
RIM_TAG=$(uuidgen)
# Certificate params
RIM_CERT="BaseRIMTestCA.example.com.pem"
SIG_ALG="rsa:2048"
DAYS_VALID="3652"
SUBJECT_DN="/C=US/O=example.com/OU=PC_RIM_Test"
RIM_KEY="key.pem"

check_admin () {
	if [ "$EUID" -ne 0 ]
     then echo "Error: quick_prcrim.sh requires root privileges.  Please run as root" 
     exit 1
fi
}

help () {
  echo "  Create a quick test for a PC Client Rim Bundle based upon your local device"
  echo "  Syntax: sh aca_setup.sh [-h|--help|-l |--rimel]"
  echo "  options:"
  echo "     -l  | --rimel  Optional path to the PC Client support RIM file"
  echo "     -h  | --help   Print this help"
  echo
}

# Many files and function used require admin access, so check and exit if not provided 
check_admin

OEM="$(cat /sys/devices/virtual/dmi/id/sys_vendor)";
OEM_NO_SPACES=$(cat /sys/devices/virtual/dmi/id/sys_vendor| tr -d '[:space:]');
MODEL="$(cat /sys/devices/virtual/dmi/id/product_name)";
MODEL_NO_SPACES=$(cat /sys/devices/virtual/dmi/id/product_name| tr -d '[:space:]');
OEM_URL=$OEM_NO_SPACES.com
SUPPORT_RIM=$OEM_NO_SPACES.$MODEL_NO_SPACES.1.rimel
BASE_RIM=$OEM_NO_SPACES.$MODEL_NO_SPACES.1.swidtag

mkdir -p $DATA_OUT_DIR
chmod 777 $DATA_OUT_DIR
pushd $DATA_OUT_DIR

## Process parameters
# Process parameters Argument handling 
POSITIONAL_ARGS=()
ORIGINAL_ARGS=("$@")
while [[ $# -gt 0 ]]; do
  case $1 in
    -l|--rimel)
      USE_RIMEL_FILE=true
      RIMEL_PATH=$@
      shift # past argument
      shift # past parameter
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

# Step 1 Obtain the Support RIM file...
if [ -z "$USE_SM_FILE" ]; then
  echo "Using $DEFAULT_SRIM as the Support RIM file..."
  cp $DEFAULT_SRIM $DATA_OUT_DIR/$SUPPORT_RIM
  chmod 777 $DATA_OUT_DIR/$SRIM_FILE_NAME
  else
    echo "Using provided support RIM $RIMEL_PATH as the Support RIM file..."
    echo "Storing $RIMEL_PATH as $SUPPORT_RIM for use as the Support RIM file..."
    cp $RIMEL_PATH $DATA_OUT_DIR/$SUPPORT_RIM
fi

# Step 2: Obtain and modify a config file
cp  $ORIG_CONFIG_FILE $DATA_OUT_DIR/$RIM_CONF
# Update the config file with local mfg and model
sed -i "s/Example.com/$OEM/g" $RIM_CONF;
sed -i "s/ProductA/$MODEL/g" $RIM_CONF;
sed -i "s/ExampleUrl/$OEM_URL/g" $RIM_CONF;
 #update the tagid so the ACA will not reject the upload 
jq --arg new_val "$RIM_TAG" '.SoftwareIdentity.tagId = $new_val' "$RIM_CONF" > temp.json && mv temp.json "$RIM_CONF"
chmod 777 $RIM_CONF

# Step 3, set up the keys/certs needed for Base RIM Creation
echo "Creating a signing key for signing the Bse RIM"
    $(openssl req -x509 -nodes -days "$DAYS_VALID" -newkey "$SIG_ALG" -keyout "$RIM_KEY" -out "$RIM_CERT" -subj "$SUBJECT_DN" >> /dev/null)
    if [ $? -ne 0 ]; then
        echo "Failed to create the key pair, exiting"
        exit 1
    fi
chmod 777 $RIM_SIGN_CERT $SIG_KEY

# Step 4 create the new PC CLient Base RIM file
echo "Creating new Base RIM ..... "

echo "rim create -r pcrim -c $RIM_CONF -k  $RIM_KEY -p $RIM_CERT -l $SUPPORT_RIM -o $BASE_RIM"
/usr/local/bin/rim create -r pcrim -c $RIM_CONF -k  $RIM_KEY -p $RIM_CERT  -l $SUPPORT_RIM -o $BASE_RIM

chmod 777 $BASE_RIM

popd
echo "Done."
