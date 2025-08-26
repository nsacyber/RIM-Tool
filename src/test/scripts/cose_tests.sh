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

rm -rf $dataDir/tmp # Remove an previous test data
mkdir -p $dataDir/tmp

# Start of COSE WG Tests https://github.com/cose-wg/Examples/tree/master/sign1-tests
echo "COSE TEST 1: CoseWG sign-pass-01.cose"
eval $rim verify -r coswid -i $dataDir/cose/cosewg/sign-pass-01.cose -k $dataDir/cose/cosewg/signed-01.json.key  >>/dev/null
rim_expected_pass_status $? "CoSwid TEST 1: CoseWG sign-pass-01.cose"

echo "COSE TEST 2: CoseWG sign-pass-02 uses external signature data which is not supported by the RIM Tool"
eval $rim verify -r cose --in $dataDir/cose/cosewg/sign-pass-02.cose -k $dataDir/cose/cosewg/signed-01.json.key  >>/dev/null
rim_expected_fail_status $? "CoseWG TEST 2: CoseWG sign-pass-02.cose"

echo "COSE TEST 3: CoseWG sign-pass-03.cose is untagged and not supported by the RIM Tool"
eval $rim verify -r cose --in $dataDir/cose/cosewg/sign-pass-03.cose -k $dataDir/cose/cosewg/signed-01.json.key  >>/dev/null
rim_expected_fail_status $? "CoseWG TEST 3: CoseWG sign-pass-03.cose"

echo "COSE TEST 4: CoseWG sign-fail-01.cose uses an incorrect COSE CBOR Tag (998)"
eval $rim verify -r cose --in $dataDir/cose/cosewg/sign-fail-01.cose -k $dataDir/cose/cosewg/signed-01.json.key  >>/dev/null
rim_expected_fail_status $? "CoseWG TEST 4: CoseWG sign-fail-01.cose"

echo "COSE TEST 5: CoseWG sign-fail-02.cose has a corrupted signature"
eval $rim verify -r cose --in $dataDir/cose/cosewg/sign-fail-02.cose -k $dataDir/cose/cosewg/signed-01.json.key  >>/dev/null
rim_expected_fail_status $? "CoseWG TEST 5: CoseWG sign-fail-01.cose"

echo "COSE TEST 6: CoseWG sign-fail-03.cose has an incorrect signature algorithm"
eval $rim verify -r cose --in $dataDir/cose/cosewg/sign-fail-01.cose -k $dataDir/cose/cosewg/signed-01.json.key  >>/dev/null
rim_expected_fail_status $? "CoseWG TEST 6: CoseWG sign-fail-03.cose"

echo "COSE TEST 7: CoseWG sign-fail-04.cose has another incorrect signature algorithm"
eval $rim verify -r cose --in $dataDir/cose/cosewg/sign-fail-04.cose -k $dataDir/cose/cosewg/signed-01.json.key  >>/dev/null
rim_expected_fail_status $? "CoseWG TEST 7: CoseWG sign-fail-01.cose"

# Start of GlueCose tests https://github.com/gluecose/test-vectors

echo "COSE TEST 8: GlueCose sign1-verify-0000 uses external signature data which is not supported by the RIM Tool"
eval $rim verify -r cose --in $dataDir/cose/gluecose/sign1-verify-0000.cose  -k $dataDir/cose/gluecose/gluecose.ecdsaP256.json.key  >>/dev/null
rim_expected_fail_status $? "CoseWG TEST 8: Gluecose sign1-verify-0000"

echo "COSE TEST 9: GlueCose sign1-verify-0001 verifies using an ES256 key"
eval $rim verify -r cose --in $dataDir/cose/gluecose/sign1-verify-0001.cose  -k $dataDir/cose/gluecose/gluecose.ecdsaP256.json.key  >>/dev/null
rim_expected_pass_status $? "CoseWG TEST 9: Gluecose sign1-verify-0001"

echo "COSE TEST 10: GlueCose sign1-verify-0002 verifies using an ES384 key"
eval $rim verify -r cose --in $dataDir/cose/gluecose/sign1-verify-0002.cose  -k $dataDir/cose/gluecose/gluecose.ecdsaP384.json.key  >>/dev/null
rim_expected_pass_status $? "CoseWG TEST 10: Gluecose sign1-verify-0002"

echo "COSE TEST 11: GlueCose sign1-verify-0003 verifies using an ES512 key"
eval $rim verify -r cose --in $dataDir/cose/gluecose/sign1-verify-0003.cose  -k $dataDir/cose/gluecose/gluecose.ecdsaP512.json.key  >>/dev/null
rim_expected_pass_status $? "CoseWG TEST 11: Gluecose sign1-verify-0003"

echo "COSE TEST 12: GlueCose sign1-verify-0004 verifies using an RSASSA-PSS w/ SHA-256 key"
eval $rim verify -r cose --in $dataDir/cose/gluecose/sign1-verify-0004.cose  -k $dataDir/cose/gluecose/gluecose.rsaPss256.json.key  >>/dev/null
rim_expected_pass_status $? "CoseWG TEST 12: Gluecose sign1-verify-0004"

echo "COSE TEST 13: GlueCose sign1-verify-0005 verifies using an RSASSA-PSS w/ SHA-384 key"
eval $rim verify -r cose --in $dataDir/cose/gluecose/sign1-verify-0005.cose  -k $dataDir/cose/gluecose/gluecose.rsaPss384.json.key  >>/dev/null
rim_expected_pass_status $? "CoseWG TEST 13: Gluecose sign1-verify-0005"

echo "COSE TEST 14: GlueCose sign1-verify-0006 verifies using an RSASSA-PSS w/ SHA-512 key"
eval $rim verify -r cose --in $dataDir/cose/gluecose/sign1-verify-0006.cose  -k $dataDir/cose/gluecose/gluecose.rsaPss512.json.key  >>/dev/null
rim_expected_pass_status $? "CoseWG TEST 14: Gluecose sign1-verify-0006"

# Start of tests using data created by the MS Sign Tool

echo "COSE TEST 15: MS COSE Sign tool:  verify MS COSE Tool generated test pattern with embedded payload using ecc 512 with SHA-256"
eval $rim verify -r cose --in $dataDir/ms_cose_sign_tool/cst_test1.cose  -p $dataDir/certs/COMP_OEM1_rim_signer_ecc_512_sha384.pem >>/dev/null
rim_expected_pass_status $? "COSE TEST 15: verify MS COSE Tool generated test pattern"

echo "COSE TEST 16: MS COSE Sign tool:  verify MS COSE Tool generated test pattern with detached payload using ecc 512 with SHA-256"
eval $rim verify -r cose --detached $dataDir/ms_cose_sign_tool/test_data.txt --in $dataDir/ms_cose_sign_tool/cst_detachedSig1.cose -p $dataDir/certs/COMP_OEM1_rim_signer_ecc_512_sha384.pem >>/dev/null
rim_expected_pass_status $? "COSE TEST 16: verify MS COSE Tool generated test pattern"

echo "COSE TEST 17: MS COSE Sign tool: Create a Signed Coswid Object wih an embedded certificate and a detached Signature"
eval $rim create -r coswid -c $dataDir/coswid/coswid_rim_1.json -p $dataDir/certs/COMP_OEM1_rim_signer_ecc_512_sha384.pem -k $dataDir/keys/COMP_OEM1_rim_signer_ecc_512_sha384.key -o $dataDir/tmp/coswid_detached_1.cose -d $dataDir/tmp/coswid_detached_payload.bin -e >>/dev/null
rim_expected_pass_status $? "COSE TEST 17: sign a coswid with an embedded cert and a detached signature"

echo "COSE TEST 18: MS COSE Sign tool: Verify a Signed Coswid Object wih an embedded certificate and a detached Signature"
eval $rim verify -r cose -p $dataDir/certs/COMP_OEM1_rim_signer_ecc_512_sha384.pem --in $dataDir/tmp/coswid_detached_1.cose -d $dataDir/tmp/coswid_detached_payload.bin >>/dev/null
rim_expected_pass_status $? "COSE TEST 18: verify a signed coswid with an embedded cert and a detached signature"

# General cose sign tests

echo "COSE TEST 19: Create Signed Coswid using COSE: Create signed coswid using a config file"
eval $rim create -r coswid -c $dataDir/coswid/coswid_rim_1.json -p $dataDir/certs/COMP_OEM1_rim_signer_ecc_512_sha384.pem -k $dataDir/keys/COMP_OEM1_rim_signer_ecc_512_sha384.key -o $dataDir/tmp/cose_coswid_test_1.cose >>/dev/null
rim_expected_pass_status $? "COSE TEST 19: Create Signed Coswid using COSE: Create signed coswid using a config file"

echo "COSE TEST 20: Verify Sign Coswid: Verify a Signed Coswid Object"
eval $rim verify -r coswid -p $dataDir/certs/COMP_OEM1_rim_signer_ecc_512_sha384.pem --in $dataDir/tmp/cose_coswid_test_1.cose >>/dev/null
rim_expected_pass_status $? "COSE TEST 20: Verify Sign Coswid: Verify a Signed Coswid Object"

echo "COSE TEST 21: Get Payload: Extract the Coswid (payload) from the COSE file"
eval $rim get -r coswid --in $dataDir/tmp/cose_coswid_test_1.cose  --out $dataDir/tmp/cose_coswid_test_1.payload >>/dev/null
rim_expected_pass_status $? "COSE TEST 21: Get Payload: Extract the Coswid (payload) from the COSE file"

echo "COSE TEST 22: Print COSE file: Parses the cose file"
eval $rim print -r cose --in $dataDir/tmp/cose_coswid_test_1.cose >>/dev/null
rim_expected_pass_status $? "COSE TEST 22: Print COSE file: Parses the cose file"

echo "COSE TEST 23: Print COSE Payload file: Proves the Payload is intact and still parses"
eval $rim print -r coswid --in $dataDir/tmp/cose_coswid_test_1.payload >>/dev/null
rim_expected_pass_status $? "COSE TEST 23: Print COSE Payload file: Proves the Payload is intact and still parses"

echo "COSE TEST 24: Create COSE object with user specified kid in protected header"
eval $rim create -r coswid -c $dataDir/coswid/coswid_rim_1.json -k $dataDir/coswid/COMP_OEM1_rim_signer_ecc_512_sha384.key -p $dataDir/coswid/COMP_OEM1_rim_signer_ecc_512_sha384.pem -e -o $dataDir/tmp/coswid_rim_1.signed.ecc.coswid2.cose -pk 11 >>/dev/null
eval $rim print -r cose -i $dataDir/tmp/coswid_rim_1.signed.ecc.coswid2.cose | tr -d '\n\r[:space:]' | grep -q "ProtectedHeaderContents:Algorithm=ES384KeyId=11" >>/dev/null
rim_expected_pass_status $? "COSE TEST 24: verify cose signate added user specified kid to the protected header"

echo "COSE TEST 25: Create COSE object with user specified kid in the unprotected header"
eval $rim create -r coswid -c $dataDir/coswid/coswid_rim_1.json -k $dataDir/coswid/COMP_OEM1_rim_signer_ecc_512_sha384.key -p $dataDir/coswid/COMP_OEM1_rim_signer_ecc_512_sha384.pem -e -o $dataDir/tmp/coswid_rim_1.signed.ecc.coswid2.cose -uk 11 >>/dev/null
eval $rim print -r cose -i $dataDir/tmp/coswid_rim_1.signed.ecc.coswid2.cose | tr -d '\n\r[:space:]' | grep -q "UnprotectedHeaderContents:KeyId=11" >>/dev/null
rim_expected_pass_status $? "COSE TEST 25: verify cose signature added user specified kid to the unprotected header"

rm -rf $dataDir/tmp # remove temporary files

if  [ $failCount -eq 0 ]; then
  echo "All COSE tests passed."
    exit 0
 else
    echo "$failCount COSE Tests failed."
    exit 1
fi
popd > /dev/null