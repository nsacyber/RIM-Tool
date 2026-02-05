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
[^3]:
    If specified, embedded certificate supersedes `-p` option.

    For [XML-DSig :fontawesome-solid-external-link:](https://www.w3.org/TR/xmldsig-core/): a certificate may be embedded into the signed SWID tag.
    
    For [COSE :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/html/rfc8152): a certificate and its thumbprint may be embedded into the protected header.

### `-l` option (PC Client RIM only)

The default behavior for the `verify` command for a [TCG PC Client RIM](../RIMs/tcg-pc-client-rim.md) is to *ignore* the file attribute that specifies the name of the Support RIM file, as well as the hash associated with that file.

This parameter is intended to change that behavior, and *enforce* the verification of the hash of the Support RIM file found on the host file system as follows:

- `-l ""`: uses the `<Directory>` element path (if present) associated with the corresponding `<File>` element to find the Support RIM.

- `-l <path>`: uses the supplied path, along with the file attribute, to find the Support RIM file.

??? info "Further Details"
    [NISTIR 8060 :fontawesome-solid-external-link:](https://doi.org/10.6028/NIST.IR.8060), section 4.6.1 states:
    > Files are described using the `<File>` element, and folders are described using the `<Directory>` element.

    When processing a PC Client RIM (with the `-l ""` option), the `RIM-Tool` will assume that the full path to a Support RIM file is a concatenation of the `<Directory>` element text (if present), and the `<File>` element text found in the Base RIM's payload element. 

See below [Examples](#examples) section for sample usage of the `-l` option.

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
    rim verify -r pcrim --in pcrim/laptop.default.1.swidtag -p pcrim/RimSignCert.pem -t pcrim/RIMCaCert.pem -l pcrim/
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