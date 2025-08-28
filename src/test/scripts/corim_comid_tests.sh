#!/bin/bash
# RIM Tool COSE (rfc 8152) System Tests

scriptDir=$(dirname -- "$(readlink -f -- "${BASH_SOURCE[0]}")")
# relative path to data stored for the project
dataDir=$scriptDir/../../../data
# Temporary invocation for java call for the project
rim="java -jar $scriptDir/../../../build/libs/rim-tool.jar"

# go to the script directory so everything runs smoothly ...
pushd $scriptDir > /dev/null
. ./rim_functions.sh
failCount=0;

rm -rf $dataDir/tmp
mkdir -p $dataDir/tmp

# Test Patterns from veraison
echo "CoRim TEST 1: Print and validate the format of signed Corim test pattern"
eval $rim print -r corim_comid --in $dataDir/corim/veraison/signed-example-corim.cbor >>/dev/null
rim_expected_pass_status $? "CoRim TEST 1: CoRim print"

# signing a Corim
echo "CoRim TEST 2: Sign a CoRIM using a valid private key"
eval $rim sign -r corim_comid --in $dataDir/corim/veraison/unsigned-example-corim.cbor \
  --out $dataDir/tmp/corim-test-signed1.cose -p $dataDir/certs/COMP_OEM1_rim_signer_ecc_512_sha384.pem -k \
  $dataDir/keys/COMP_OEM1_rim_signer_ecc_512_sha384.key  >>/dev/null
rim_expected_pass_status $? "CoRim TEST 2: CoRim sign"

# validating a Corim
echo "CoRim TEST 3: Verify a signed CoRIM"
eval $rim verify -r corim_comid --in $dataDir/tmp/corim-test-signed1.cose \
  -p $dataDir/certs/COMP_OEM1_rim_signer_ecc_512_sha384.pem >>/dev/null
rim_expected_pass_status $? "CoRim TEST 3: CoRim verify"

# creating a CoRIM with CoMID
echo "CoRim TEST 4: Create an unsigned CoRIM (with CoMID) from an input configuration file"
eval $rim create -r corim_comid -c $dataDir/corim/corim_1.json --out \
  $dataDir/tmp/corim-test-unsigned1.cbor -u >>/dev/null
rim_expected_pass_status $? "CoRim TEST 4: CoRim create with CoMID (unsigned)"

# sign CoRIM with embedded cert
echo "CoRim TEST 5: Sign a CoRIM using an embedded cert"
eval $rim sign -r corim_comid --in $dataDir/tmp/corim-test-unsigned1.cbor \
  --out $dataDir/tmp/corim-test-embedded-signed1.cose -p $dataDir/certs/COMP_OEM1_rim_signer_ecc_512_sha384.pem -k \
  $dataDir/keys/COMP_OEM1_rim_signer_ecc_512_sha384.key -e >>/dev/null
rim_expected_pass_status $? "CoRim TEST 5: CoRim sign (embedded)"

# verify CoRIM with embedded cert
echo "CoRim TEST 6: Verify a signed CoRIM with an embedded cert"
eval $rim verify -r corim_comid --in $dataDir/tmp/corim-test-embedded-signed1.cose -e >>/dev/null
rim_expected_pass_status $? "CoRim TEST 6: CoRim verify (embedded)"

# TODO: Corim with Coswids
# TODO: Corim with CoTLs

rm -rf $dataDir/tmp

if  [ $failCount -eq 0 ]; then
  echo "All COSE tests passed."
    exit 0
 else
    echo "$failCount COSE Tests failed."
    exit 1
fi
popd > /dev/null