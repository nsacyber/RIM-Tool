#!/bin/bash
# RIM Tool Coswid System Tests

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

echo "CoSwid TEST 1: Parse and Print and LVFS SBOM test pattern"
eval $rim print -r coswid --in $dataDir/coswid/LVFS_sbom.coswid >>/dev/null
rim_expected_pass_status $? "CoSwid TEST 1: LVFS Unsigned Coswid print"

echo "CoSwid TEST 2: Create an unsigned Coswid object and save it to the filesystem"
eval $rim create -r coswid -c $dataDir/coswid/coswid_rim_1.json -o $dataDir/tmp/coswid_rim_1.coswid -u
rim_expected_pass_status $? "CoSwid TEST 2: Unsigned Coswid create"

echo "CoSwid TEST 3: Attempt to create an unsigned Coswid object using an invalid json file"
eval $rim create -r coswid -c $dataDir/coswid/coswid_rim_bad_1.json -o $dataDir/tmp/coswid_rim_1.coswid -u
rim_expected_fail_status $? "CoSwid TEST 3: Unsigned Coswid create with bad json file"

echo "CoSwid TEST 4: Print the unsigned Coswid object from a file to prove it will parse"
eval $rim print -r coswid --in $dataDir/tmp/coswid_rim_1.coswid >> /dev/null
rim_expected_pass_status $? "CoSwid TEST 4: Unsigned Coswid print"

echo "CoSwid TEST 5: Attempt to Print a bad unsigned Coswid object from a file to prove parse will return an error"
eval $rim print -r coswid --in $dataDir/coswid/ntia.coswid >> /dev/null
rim_expected_fail_status $? "CoSwid TEST 5: Bad Coswid print"

echo "CoSwid TEST 6: Create a Signed Coswid using a cert for the Algorithm ID and a pem private key file"
eval $rim create -r coswid -c $dataDir/coswid/coswid_rim_1.json -p $dataDir/coswid/COMP_OEM1_rim_signer_ecc_512_sha384.pem -k $dataDir/coswid/COMP_OEM1_rim_signer_ecc_512_sha384.key -o $dataDir/tmp/coswid_rim_1.signed.ecc.coswid.cose
rim_expected_pass_status $? "CoSwid TEST 6: Sign Coswid object wih cert"

echo "CoSwid TEST 7: Create a Signed Coswid using -al param for the Algorithm ID"
eval $rim create -r coswid -c $dataDir/coswid/coswid_rim_1.json -p $dataDir/coswid/COMP_OEM1_rim_signer_ecc_512_sha384.pem  -k $dataDir/coswid/COMP_OEM1_rim_signer_ecc_512_sha384.key -a ES512 -o $dataDir/tmp/coswid_rim_1.signed.ecc.coswid2.cose
rim_expected_pass_status $? "CoSwid TEST 7: Sign Coswid object with alg id"

echo "CoSwid TEST 8: Create a Signed Coswid using a Jason Web Key file"
eval $rim create -r coswid -c $dataDir/coswid/coswid_rim_1.json -k $dataDir/coswid/gluecose.ecdsaP384.json.key -o $dataDir/tmp/coswid_rim_1.signed.ecc.3.cose
rim_expected_pass_status $? "CoSwid TEST 8: Sign Coswid object wih Json Web Key"

# Verify a good Signed Coswid using the signing cert only
echo "CoSwid TEST 9: Verify a Signed Coswid using an X.509 Certificate"
eval $rim verify -r coswid -p $dataDir/coswid/COMP_OEM1_rim_signer_ecc_512_sha384.pem --in  $dataDir/tmp/coswid_rim_1.signed.ecc.coswid.cose
rim_expected_pass_status $? "CoSwid TEST 9: Verify Coswid Signature using an X.509 Certificate"

# Verify a bad Signed Coswid (signature corrupted)
echo "CoSwid TEST 10: Verify a bad Signed Coswid fails to verify"
eval $rim verify -r coswid -p $dataDir/coswid/COMP_OEM1_rim_signer_ecc_512_sha384.pem --in  $dataDir/coswid/coswid_rim_1.signed.bad.coswid.cose
rim_expected_fail_status $? "CoSwid TEST 10: Verify that bad Coswid Signature fails"

echo "CoSwid TEST 11: Verify a Signed Coswid using a Jason Web Key file"
eval $rim verify -r coswid -k $dataDir/coswid/gluecose.ecdsaP384.json.key --in $dataDir/tmp/coswid_rim_1.signed.ecc.3.cose
rim_expected_pass_status $? "CoSwid TEST 11: Verify Signed Coswid object with Json Web Key"

echo "CoSwid TEST 12: Sign a previously created CoSwid RIM"
eval $rim sign -r coswid --in $dataDir/tmp/coswid_rim_1.coswid -k $dataDir/coswid/COMP_OEM1_rim_signer_ecc_512_sha384.key -p $dataDir/coswid/COMP_OEM1_rim_signer_ecc_512_sha384.pem -o $dataDir/tmp/coswid_rim_2.coswid.cose  ##>>/dev/null
rim_expected_pass_status $? "CoSwid TEST 12: Sign a previously created CoSwid RIM"

echo "CoSwid TEST 13: Verify a CoSwid RIM previously signed"
eval $rim verify -r coswid -p $dataDir/coswid/COMP_OEM1_rim_signer_ecc_512_sha384.pem --in $dataDir/tmp/coswid_rim_2.coswid.cose  ##>>/dev/null
rim_expected_pass_status $? "CoSwid TEST 13: Verify a CoSwid previously signed"

rm -rf $dataDir/tmp

if  [ $failCount -eq 0 ]; then
  echo "All Coswid tests passed."
    exit 0
 else
    echo "$failCount Coswid Tests failed."
    exit 1
fi

popd > /dev/null