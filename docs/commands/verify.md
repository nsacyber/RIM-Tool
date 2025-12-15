---
title: Verify
---

# `verify` Command
Verifies the signature of a RIM, CoRIM, or CoSWID tag.

## Usage

```shell
rim verify -r <string> -i <file> -p <file> [-l <path>] [-t <file>] [-d <file>] [-e]
```

## Parameters

| Parameter                    | Description                                                         | Required | Type   |
|------------------------------|---------------------------------------------------------------------|----------|--------|
| `-r`, `--rim-type`           | Specifies the [RIM type](../RIMs/index.md).                         | Yes      | String |
| `-i`, `--in`                 | Specifies the input file to verify.                                 | Yes      | File   |
| `-l`, `--rimel`              | Support RIM (PC Client RIM only). See [below](#-l-option-pc-client-rim-only).    | No[^1]   | Path   |
| `-p`, `--public-certificate` | The public key certificate to be used to verify the RIM.            | Yes      | File   |
| `-t`, `--truststore`         | The trust store used to validate the Base RIM (PC Client RIM only). | No[^2]   | File   |
| `-d`, `--detached`           | Uses a detached signature file for verification.                    | No       | File   |
| `-e`, `--embed-cert`         | Uses embedded certificate, depending on input type.[^3]             | No       |        |


[^1]: A Support RIM file is only used for a [TCG PC Client RIM :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/wp-content/uploads/TCG-PC-Client-Reference-Integrity-Manifest-Specification-Version-1.1-Revision-11_pub.pdf). See `-l` option [documentation](#-l-option-pc-client-rim-only).
[^2]: Required if type is set to [TCG PC Client RIM :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/wp-content/uploads/TCG-PC-Client-Reference-Integrity-Manifest-Specification-Version-1.1-Revision-11_pub.pdf).
[^3]: If specified, embedded certificate supersedes `-p` option. For [XML-DSig :fontawesome-solid-external-link:](https://www.w3.org/TR/xmldsig-core/): a certificate may be embedded into the signed SWID tag. For [COSE :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/html/rfc8152): a certificate and its thumbprint may be embedded into the protected header.

### `-l` option (PC Client RIM only)

For [TCG PC Client RIMs](../RIMs/tcg-pc-client-rim.md), the `-l` option has the following effects:

- If present with a valid, *non-empty* path specified, Support RIM validation will be enabled. The validation will be performed by searching the specified path for contained Support RIMs (with matching filenames), ignoring absolute paths.
- If present with an *empty* (`-l ""`) path specified, Support RIM validation will also be enabled. However, the validation will be performed from **absolute** file system paths that are specified in the Base RIM. Ensure that these paths and associated Support RIM files are present on the file system, in this case.

If no option is present, no Support RIM validation will be performed. See below [Examples](#examples) section for sample usage of the `-l` option.

## Examples

!!! example "Before Starting"
Before running these examples, please ensure that you change into the `data` directory (requires [install](../getting-started.md/#installation)):
=== "Linux"
```shell
cd /opt/rimtool/data
```

Verify a [TCG PC Client Base RIM](../RIMs/tcg-pc-client-rim.md), alongside Support RIM:

=== "Linux"
```shell
rim verify -r pcrim --in pcrim/laptop.default.1.swidtag -p pcrim/RimSignCert.pem -t pcrim/RIMCaCert.pem -l "/opt/rimtool/data/"
```

Verify a signed [CoSWID tag](../RIMs/coswid.md):

=== "Linux"
```shell
rim verify -r coswid --in coswid/coswid_rim_1.signed.coswid.cose -p certs/COMP_OEM1_rim_signer_ecc_512_sha384.pem
```

Verify a signed [CoRIM](../RIMs/corim.md):

=== "Linux"
```shell
rim verify -r corim_comid --in corim/corim_1.signed.corim.cose -p certs/COMP_OEM1_rim_signer_ecc_512_sha384.pem
```
