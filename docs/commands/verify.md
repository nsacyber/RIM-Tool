---
title: Verify
---

# `verify` Command
Verifies the signature of a RIM, CoRIM, or CoSWID tag.

## Usage

```shell
rim verify -rt <string> -i <file> -p <file> [-l <file>] [-t <file>] [-d]
```

## Parameters

| Parameter                    | Description                                                         | Required | Type   |
|------------------------------|---------------------------------------------------------------------|----------|--------|
| `-rt`, `--rim-type`          | Specifies the [RIM type](../RIMs/index.md).                         | Yes      | String |
| `-i`, `--in`                 | Specifies the input file to verify.                                 | Yes      | File   |
| `-l`, `--rimel`              | Support RIM (PC Client RIM only).                                   | No[^1]   | File   |
| `-p`, `--public-certificate` | The public key certificate to be used to verify the RIM.            | Yes      | File   |
| `-t`, `--truststore`         | The trust store used to validate the base RIM (PC Client RIM only). | No[^2]   | File   |
| `-o`, `--out`                | The path of the file to write the RIM to.                           | Yes      | File   |
| `-d`, `--detached`           | Uses a detached signature file. By default, embeds the signature.   | No       | File   |

[^1]: A Support RIM file is only used for a [TCG PC Client RIM :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/wp-content/uploads/TCG-PC-Client-Reference-Integrity-Manifest-Specification-Version-1.1-Revision-11_pub.pdf). If this type is used, this option is required.
[^2]: Required if type is set to [TCG PC Client RIM :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/wp-content/uploads/TCG-PC-Client-Reference-Integrity-Manifest-Specification-Version-1.1-Revision-11_pub.pdf).

## Examples

Verify a [TCG PC Client Base RIM](../RIMs/tcg-pc-client-rim.md):

```shell
rim verify -rt pcrim --in test.1.swidtag -l TPMEventLog.1.rimel -p RimSignCert.pem -t RIMCaCert.pem
```

Verify a signed [CoSWID tag](../RIMs/coswid.md):

```shell
rim verify -rt coswid --in test1.coswid.cose coswid_rim_1.json -p RimSignCert.pem -t RIMCaCert.pem
```

Verify a [CoRIM](../RIMs/corim.md):

```shell
rim verify -rt corim-comid --in corim-test.cose -p RimSignCert.pem -t RIMCaCert.pem
```
