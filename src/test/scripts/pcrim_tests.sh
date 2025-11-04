#!/bin/bash
# RIM Tool TCG PC Client RIM System Tests

scriptDir=$(dirname -- "$(readlink -f -- "${BASH_SOURCE[0]}")")
# relative path to data stored for the project
dataDir=$scriptDir/../../../data
# Temporary invocation for java call for the project
rim="java -jar $scriptDir/../../../build/libs/rim-tool*.jar"

# go to the script directory so everything runs smoothly ...
pushd $dataDir > /dev/null
source $scriptDir/rim_functions.sh
failCount=0;

rm -rf ./tmp
mkdir -p ./tmp

# Create and Verify Test patterns
echo "PC Client RIM TEST 1: Create PC Client signed RIM test pattern using specified rimmel file"
eval $rim create -r pcrim -l pcrim/laptop.default.1.rimel --out tmp/laptop.default.1.swidtag -p pcrim/RimSignCert.pem -k pcrim/rimKey.pem -c pcrim/rim_fields.json >>/dev/null
rim_expected_pass_status $? "PC RIM TEST 1: PC RIM Create with -l"

echo "PC Client RIM TEST 2: Verify PC Client signed RIM test pattern using specified rimmel file"
eval $rim verify -r pcrim -l pcrim/laptop.default.1.rimel --in tmp/laptop.default.1.swidtag -p pcrim/RimSignCert.pem -t pcrim/RIMCaCert.pem  >>/dev/null
rim_expected_pass_status $? "PC RIM TEST 2: PC RIM Verify with -l"

echo "PC Client RIM TEST 3: Create PC Client signed RIM test pattern with no specified rimmel file"
eval $rim create -r pcrim --out tmp/laptop.default.2.swidtag -p pcrim/RimSignCert.pem -k pcrim/rimKey.pem -c pcrim/rim_fields.json >>/dev/null
rim_expected_fail_status $? "PC RIM TEST 3: PC RIM Create"

echo "PC Client RIM TEST 4: Verify PC Client signed RIM test pattern with no specified rimmel file"
eval $rim verify -r pcrim --in tmp/laptop.default.1.swidtag -p pcrim/RimSignCert.pem -t pcrim/RIMCaCert.pem  #>>/dev/null
rim_expected_pass_status $? "PC RIM TEST 4: PC RIM Verify"

echo "PC Client RIM TEST 5: Create PC Client signed RIM test pattern with multiple Payload file hashes"
eval $rim create -r pcrim -l pcrim/laptop.default.1.rimel --out tmp/laptop.default.3.swidtag -p pcrim/RimSignCert.pem -k pcrim/rimKey.pem -c pcrim/rim_fields_multiple_files.json #>>/dev/null
rim_expected_pass_status $? "PC RIM TEST 5: PC RIM Create with multiple payload hashes"

echo "PC Client RIM TEST 6: Verify PC Client signed RIM test pattern with multiple Payload file hashes"
eval $rim verify -r pcrim -l pcrim/laptop.default.1.rimel --in tmp/laptop.default.3.swidtag -p pcrim/RimSignCert.pem -t pcrim/RIMCaCert.pem  >>/dev/null
rim_expected_pass_status $? "PC RIM TEST 6: PC RIM Verify with multiple payload hashes"

echo "PC Client RIM TEST 7: Verify PC Client will fail when a signature is invalid"
eval $rim verify -r pcrim -l pcrim/laptop.default.1.rimel --in pcrim/laptop.default.bad.swidtag -p pcrim/RimSignCert.pem -t pcrim/RIMCaCert.pem  >>/dev/null
rim_expected_fail_status $? "PC RIM TEST 7: PC RIM Bad signature check"

echo "PC Client RIM TEST 8: Create PC Client signed Patch RIM "
eval $rim create -r pcrim --out tmp/laptop.patch.1.swidtag -l pcrim/laptop.default.1.rimel -p pcrim/RimSignCert.pem -k pcrim/rimKey.pem -c pcrim/rim_fields_patch.json #>>/dev/null
rim_expected_pass_status $? "PC RIM TEST 8: Create PC Patch RIM"

echo "PC Client RIM TEST 9: Verify PC Client signed Patch RIM"
eval $rim verify -r pcrim --in tmp/laptop.patch.1.swidtag -p pcrim/RimSignCert.pem -t pcrim/RIMCaCert.pem  #>>/dev/null
rim_expected_pass_status $? "PC RIM TEST 9: PC Patch RIM Verify"

echo "PC Client RIM TEST 10: Create PC Client signed Supplemental RIM "
eval $rim create -r pcrim --out tmp/laptop.supplemental.1.swidtag -l pcrim/laptop.default.1.rimel -p pcrim/RimSignCert.pem -k pcrim/rimKey.pem -c pcrim/rim_fields_supplemental.json #>>/dev/null
rim_expected_pass_status $? "PC RIM TEST 10: Create PC Supplemental RIM"

echo "PC Client RIM TEST 11: Verify PC Client signed Supplemental RIM"
eval $rim verify -r pcrim --in tmp/laptop.supplemental.1.swidtag -p pcrim/RimSignCert.pem -t pcrim/RIMCaCert.pem  #>>/dev/null
rim_expected_pass_status $? "PC RIM TEST 11: PC Supplemental RIM Verify"

rm -rf tmp

if  [ $failCount -eq 0 ]; then
  echo "All PC RIM tests passed."
    exit 0
 else
    echo "$failCount PC RIM Tests failed."
    exit 1
fi
popd > /dev/null
