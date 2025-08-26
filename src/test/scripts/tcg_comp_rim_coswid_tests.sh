#!/bin/bash
# RIM Tool TCG Component RIM (Coswid binding) System Tests

scriptDir=$(dirname -- "$(readlink -f -- "${BASH_SOURCE[0]}")")
# relative path to data stored for the project
dataDir=$scriptDir/../../../data
# Temporary invocation for java call for the project
rim="java -jar $scriptDir/../../../build/libs/rim-tool.jar"
rimType="tcg_component-rim-coswid"
# go to the script directory so everything runs smoothly ...
pushd $scriptDir > /dev/null
. ./rim_functions.sh
failCount=0;

rm -rf $dataDir/tmp
mkdir -p $dataDir/tmp

echo "TCG Comp RIM TEST 1: Create an unsigned TCG Component RIM Coswid file"
eval $rim create -r comp_coswid -c $dataDir/tcg_comp_rim/tcg_comp_rim_coswid_1.json -o $dataDir/tmp/tcg_comp_rim_1.coswid -u >>/dev/null
rim_expected_pass_status $? "TCG Comp RIM TEST 1: Create unsigned"

echo "TCG Comp RIM TEST 2: Print a TCG Component RIM Coswid file"
eval $rim print -r comp_coswid --in $dataDir/tmp/tcg_comp_rim_1.coswid  >>/dev/null
rim_expected_pass_status $? "TCG Comp RIM TEST 2: Print"

echo "TCG Comp RIM TEST 3: Attempt to create a TCG Component RIM using an invalid json file"
eval $rim create -r comp_coswid -c $dataDir/tcg_comp_rim/tcg_comp_rim_coswid_bad_1.json -o $dataDir/tmp/tcg_comp_rim_bad_1.coswid  >>/dev/null
rim_expected_fail_status $? "TCG Comp RIM TEST 3: Attempt to create using a bad Json file"

echo "TCG Comp RIM TEST 4: Create a signed TCG Component RIM"
eval $rim create -r comp_coswid -c $dataDir/tcg_comp_rim/tcg_comp_rim_coswid_1.json -k $dataDir/tcg_comp_rim/COMP_OEM1_rim_signer_rsa_3k_sha384.key -p $dataDir/tcg_comp_rim/COMP_OEM1_rim_signer_rsa_3k_sha384.pem -o $dataDir/tmp/tcg_comp_rim_1.coswid.cose  #>>/dev/null
rim_expected_pass_status $? "TCG Comp RIM TEST 4: Create a signed TCG Component RIM"

echo "TCG Comp RIM TEST 5: Verify a TCG Component RIM created with a signature"
eval $rim verify -r comp_coswid -p $dataDir/tcg_comp_rim/COMP_OEM1_rim_signer_rsa_3k_sha384.pem --in $dataDir/tmp/tcg_comp_rim_1.coswid.cose  ##>>/dev/null
rim_expected_pass_status $? "TCG Comp RIM TEST 5: Verify a TCG Component RIM created with a signature"

echo "TCG Comp RIM TEST 6: Sign a previously created TCG Component RIM"
eval $rim sign -r comp_coswid --in $dataDir/tmp/tcg_comp_rim_1.coswid -k $dataDir/tcg_comp_rim/COMP_OEM1_rim_signer_rsa_3k_sha384.key -p $dataDir/tcg_comp_rim/COMP_OEM1_rim_signer_rsa_3k_sha384.pem -o $dataDir/tmp/tcg_comp_rim_2.coswid.cose  ##>>/dev/null
rim_expected_pass_status $? "TCG Comp RIM TEST 6: Sign a previously created TCG Component RIM"

echo "TCG Comp RIM TEST 7: Verify a TCG Component RIM previously signed"
eval $rim verify -r comp_coswid -p $dataDir/tcg_comp_rim/COMP_OEM1_rim_signer_rsa_3k_sha384.pem --in $dataDir/tmp/tcg_comp_rim_2.coswid.cose  ##>>/dev/null
rim_expected_pass_status $? "TCG Comp RIM TEST 7: Verify a TCG Component RIM previously signed"

rm -rf $dataDir/tmp

if  [ $failCount -eq 0 ]; then
  echo "All TCG Component RIM tests passed."
    exit 0
 else
    echo "$failCount TCG Component RIM Tests failed."
    exit 1
fi
popd > /dev/null