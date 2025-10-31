#!/bin/bash
# RIM Tool TCG PC Client RIM System Tests

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

# Create and Verify Test patterns
echo "PC Client RIM TEST 1: Create PC Client signed RIM test pattern using specified rimmel file"
eval $rim create -r pcrim -l $dataDir/pcrim/laptop.default.1.rimel --out $dataDir/tmp/laptop.default.1.swidtag -p $dataDir/pcrim/RimSignCert.pem -k $dataDir/pcrim/rimKey.pem -c $dataDir/pcrim/rim_fields.json >>/dev/null
rim_expected_pass_status $? "PC RIM TEST 1: PC RIM Create with -l"

echo "PC Client RIM TEST 2: Verify PC Client signed RIM test pattern using specified rimmel file"
eval $rim verify -r pcrim -l $dataDir/pcrim/laptop.default.1.rimel --in $dataDir/tmp/laptop.default.1.swidtag -p $dataDir/pcrim/RimSignCert.pem -t $dataDir/pcrim/RIMCaCert.pem  >>/dev/null
rim_expected_pass_status $? "PC RIM TEST 2: PC RIM Verify with -l"

echo "PC Client RIM TEST 3: Create PC Client signed RIM test pattern with no specified rimmel file"
eval $rim create -r pcrim --out $dataDir/tmp/laptop.default.2.swidtag -p $dataDir/pcrim/RimSignCert.pem -k $dataDir/pcrim/rimKey.pem -c $dataDir/pcrim/rim_fields.json >>/dev/null
rim_expected_fail_status $? "PC RIM TEST 3: PC RIM Create"

echo "PC Client RIM TEST 4: Verify PC Client signed RIM test pattern with no specified rimmel file"
eval $rim verify -r pcrim --in $dataDir/tmp/laptop.default.1.swidtag -p $dataDir/pcrim/RimSignCert.pem -t $dataDir/pcrim/RIMCaCert.pem  #>>/dev/null
rim_expected_pass_status $? "PC RIM TEST 4: PC RIM Verify"

echo "PC Client RIM TEST 5: Create PC Client signed RIM test pattern with multiple Payload file hashes"
eval $rim create -r pcrim -l $dataDir/pcrim/laptop.default.1.rimel --out $dataDir/tmp/laptop.default.3.swidtag -p $dataDir/pcrim/RimSignCert.pem -k $dataDir/pcrim/rimKey.pem -c $dataDir/pcrim/rim_fields_multiple_files.json #>>/dev/null
rim_expected_pass_status $? "PC RIM TEST 5: PC RIM Create with multiple payload hashes"

echo "PC Client RIM TEST 6: Verify PC Client signed RIM test pattern with multiple Payload file hashes"
eval $rim verify -r pcrim -l $dataDir/pcrim/laptop.default.1.rimel --in $dataDir/tmp/laptop.default.3.swidtag -p $dataDir/pcrim/RimSignCert.pem -t $dataDir/pcrim/RIMCaCert.pem  >>/dev/null
rim_expected_pass_status $? "PC RIM TEST 6: PC RIM Verify with multiple payload hashes"

echo "PC Client RIM TEST 7: Verify PC Client will fail when a signature is invalid"
eval $rim verify -r pcrim -l $dataDir/pcrim/laptop.default.1.rimel --in $dataDir/pcrim/laptop.default.bad.swidtag -p $dataDir/pcrim/RimSignCert.pem -t $dataDir/pcrim/RIMCaCert.pem  >>/dev/null
rim_expected_fail_status $? "PC RIM TEST 7: PC RIM Bad signature check"

echo "PC Client RIM TEST 8: Create PC Client signed Patch RIM "
eval $rim create -r pcrim --out $dataDir/tmp/laptop.patch.1.swidtag -l $dataDir/pcrim/laptop.default.1.rimel -p $dataDir/pcrim/RimSignCert.pem -k $dataDir/pcrim/rimKey.pem -c $dataDir/pcrim/rim_fields_patch.json #>>/dev/null
rim_expected_pass_status $? "PC RIM TEST 8: Create PC Patch RIM"

echo "PC Client RIM TEST 9: Verify PC Client signed Patch RIM"
eval $rim verify -r pcrim --in $dataDir/tmp/laptop.patch.1.swidtag -p $dataDir/pcrim/RimSignCert.pem -t $dataDir/pcrim/RIMCaCert.pem  #>>/dev/null
rim_expected_pass_status $? "PC RIM TEST 9: PC Patch RIM Verify"

echo "PC Client RIM TEST 10: Create PC Client signed Supplemental RIM "
eval $rim create -r pcrim --out $dataDir/tmp/laptop.supplemental.1.swidtag -l $dataDir/pcrim/laptop.default.1.rimel -p $dataDir/pcrim/RimSignCert.pem -k $dataDir/pcrim/rimKey.pem -c $dataDir/pcrim/rim_fields_supplemental.json #>>/dev/null
rim_expected_pass_status $? "PC RIM TEST 10: Create PC Supplemental RIM"

echo "PC Client RIM TEST 11: Verify PC Client signed Supplemental RIM"
eval $rim verify -r pcrim --in $dataDir/tmp/laptop.supplemental.1.swidtag -p $dataDir/pcrim/RimSignCert.pem -t $dataDir/pcrim/RIMCaCert.pem  #>>/dev/null
rim_expected_pass_status $? "PC RIM TEST 11: PC Supplemental RIM Verify"

#echo "PC Client RIM TEST 12: Create signed Patch RIM with RimLinkHash"
#eval $rim create -r pcrim -a $dataDir/pcrim/rim_fields_patch_RimLinkHash.json -p $dataDir/pcrim/RimSignCert.pem  -k $dataDir/pcrim/rimKey.pem --out $dataDir/tmp/laptop.patch.RimLinkHash.1.swidtag  >>/dev/null
#rim_expected_pass_status $? "PC Client RIM TEST 10: Create Rim with Bad RimLinkHash"

#echo "PC Client RIM TEST 13: Verify PC Client will fail Patch RIM with bad rimLinkHash"
#eval $rim verify -r pcrim --in $dataDir/tmp/laptop.patch.badRimLinkHash.1.swidtag -p $dataDir/pcrim/RimSignCert.pem -t $dataDir/pcrim/RIMCaCert.pem  #>>/dev/null
#rim_expected_fail_status $? "PC RIM TEST 11: PC Patch RIM bad rimLinkHash"

#echo "PC Client RIM TEST 14: Verify PC Client will fail with Patch RIM with no rimLinkHash"
#eval $rim verify -r pcrim --in $dataDir/pcrim/rim_fields_patch_noRimLinkHash.json -p $dataDir/pcrim/RimSignCert.pem -t $dataDir/pcrim/RIMCaCert.pem  #>>/dev/null
#rim_expected_fail_status $? "PC RIM TEST 11: PC Patch RIM with no rimLinkHash"

# Add Detached Signature Create
# Add Detached Signature Verify
# Add Timestamp Test (tool does not verify)

rm -rf $dataDir/tmp

if  [ $failCount -eq 0 ]; then
  echo "All PC RIM tests passed."
    exit 0
 else
    echo "$failCount PC RIM Tests failed."
    exit 1
fi
popd > /dev/null