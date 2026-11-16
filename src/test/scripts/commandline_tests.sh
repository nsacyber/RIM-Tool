#!/bin/bash
# RIM Tool Commandline System Tests

scriptDir=$(dirname -- "$(readlink -f -- "${BASH_SOURCE[0]}")")
# relative path to data stored for the project
dataDir=$scriptDir/../../../data
# Temporary invocation for java call for the project
rim="java -jar $scriptDir/../../../build/libs/rim-tool*.jar"

# go to the script directory so everything runs smoothly ...
pushd $scriptDir > /dev/null
. ./rim_functions.sh
failCount=0;

rm -rf $dataDir/tmp
mkdir -p $dataDir/tmp

echo "Commandline TEST 1: Parse and Print coswid file - no rimtype"
eval $rim print coswid -i $dataDir/tmp/coswid_rim_1.coswid >>/dev/null
rim_expected_fail_status $? "Commandline TEST 1: Coswid print - no rimtype"

echo "Commandline TEST 2: Parse and Print coswid file - no command"
eval $rim -r coswid $dataDir/tmp/coswid_rim_1.coswid >>/dev/null
rim_expected_fail_status $? "Commandline TEST 2: Coswid print - no command"

echo "Commandline TEST 3: Parse and Print coswid file - no input file"
eval $rim print -r coswid >>/dev/null
rim_expected_fail_status $? "Commandline TEST 3: Coswid print - no input file"

echo "Commandline TEST 4: Create an unsigned Coswid object and save it to the filesystem - no output file"
eval $rim create -r coswid -c $dataDir/coswid/coswid_rim_1.json -u
rim_expected_fail_status $? "Commandline TEST 4: Unsigned Coswid create - no output file"

echo "Commandline TEST 5: Create an unsigned Coswid object and save it to the filesystem - incorrectly include input file"
eval $rim create -r coswid -i dataDir/tmp/coswid_rim_1.coswid -c $dataDir/coswid/coswid_rim_1.json -o $dataDir/tmp/coswid_rim_1.signed.ecc.coswid.cose -u
rim_expected_fail_status $? "Commandline TEST 5: Unsigned Coswid create - incorrectly include input file"

echo "Commandline TEST 6: Create a Signed Coswid using a cert for the Algorithm ID and a pem private key file - no cert"
eval $rim create -r coswid -c $dataDir/coswid/coswid_rim_1.json -k $dataDir/coswid/COMP_OEM1_rim_signer_ecc_512_sha384.key -o $dataDir/tmp/coswid_rim_1.signed.ecc.coswid.cose
rim_expected_fail_status $? "Commandline TEST 6: Sign Coswid object with cert - no cert"

echo "Commandline TEST 7: Create a Signed Coswid using a cert for the Algorithm ID and a pem private key file - no key"
eval $rim create -r coswid -c $dataDir/coswid/coswid_rim_1.json -p $dataDir/coswid/COMP_OEM1_rim_signer_ecc_512_sha384.pem -o $dataDir/tmp/coswid_rim_1.signed.ecc.coswid.cose
rim_expected_fail_status $? "Commandline TEST 7: Sign Coswid object with cert - no key"

echo "Commandline TEST 8: Create a Signed Coswid using a cert for the Algorithm ID and a pem private key file - bad cert"
eval $rim create -r coswid -c $dataDir/coswid/coswid_rim_1.json -p $dataDir/tmp/coswid_rim_1.coswid -k $dataDir/coswid/COMP_OEM1_rim_signer_ecc_512_sha384.key -o $dataDir/tmp/coswid_rim_1.signed.ecc.coswid.cose
rim_expected_fail_status $? "Commandline TEST 8: Sign Coswid object with cert - bad cert"

echo "Commandline TEST 9: Version command matches VERSION file"
expectedVersion=$(< "$scriptDir/../../../VERSION")
versionOutput=$(eval $rim --version)
[[ "$versionOutput" == "TCG rimtool version: $expectedVersion" ]]
rim_expected_pass_status $? "Commandline TEST 9: Version matches VERSION"

rm -rf $dataDir/tmp

if  [ $failCount -eq 0 ]; then
  echo "All Commandline tests passed."
    exit 0
 else
    echo "$failCount Commandline Tests failed."
    exit 1
fi

popd > /dev/null
