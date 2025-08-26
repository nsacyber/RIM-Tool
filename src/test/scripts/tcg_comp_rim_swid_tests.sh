#!/bin/bash
# RIM Tool TCG Component RIM (SWID binding) System Tests

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


echo "TCG Comp RIM TEST 1: Create a TCG Component RIM Coswid file"
eval $rim create -r comp_swid -c $dataDir/tcg_comp_rim_swid/tcg_comp_rim_swid_1.json -o $dataDir/tmp/tcg_comp_rim_1.swid -k $dataDir/keys/COMP_OEM1_rim_signer_rsa_3k_sha384.key -p $dataDir/certs/COMP_OEM1_rim_signer_rsa_3k_sha384.pem >>/dev/null
rim_expected_pass_status $? "TCG Comp RIM TEST 1: Create"

echo "TCG Comp RIM TEST 2: Print a TCG Component RIM Coswid file"
eval $rim print -r comp_swid --in $dataDir/tmp/tcg_comp_rim_1.swid  >>/dev/null
rim_expected_pass_status $? "TCG Comp RIM TEST 2: Print"

rm -rf $dataDir/tmp

if  [ $failCount -eq 0 ]; then
  echo "All TCG Component RIM tests passed."
    exit 0
 else
    echo "$failCount TCG Component RIM Tests failed."
    exit 1
fi
popd > /dev/null