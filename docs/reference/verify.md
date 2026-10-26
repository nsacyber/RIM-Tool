---
title: Verify
---

# `verify` Command
Verifies the signature of a RIM, CoRIM, or CoSWID tag.

## Usage

```shell
rim verify -r <string> -i <file> [-p <file>] [-k <file>] [-l <path>] [-t <file>] [-d <payload-file>] [-e]
```

## Parameters

| Parameter                    | Description                                                         | Required | Type   |
|------------------------------|---------------------------------------------------------------------|----------|--------|
| `-r`, `--rim-type`           | Specifies the [RIM type](../RIMs/index.md).                         | Yes      | String |
| `-i`, `--in`                 | Specifies the input file to verify.                                 | Yes      | File   |
| `-l`, `--rimel`              | Support RIM (PC Client RIM only). See [below](#-l-option-pc-client-rim-only).    | No[^1]   | Path   |
| `-p`, `--public-certificate` | The signing certificate used to verify the RIM.                    | No[^2]   | File   |
| `-k`, `--public-key-file`    | RSA PEM public key for PC Client RIMs, or JSON Web Key for COSE.    | No[^2]   | File   |
| `-t`, `--truststore`         | PEM CA certificates for validating embedded PC Client signing certificates.[^3] | No | File |
| `-d`, `--detached`           | Reads the separate COSE payload; `-i` holds the signature envelope. | No       | File   |
| `-e`, `--embed-cert`         | Uses the embedded certificate for COSE verification.[^4]            | No       |        |


[^1]: A Support RIM file is only used for a [TCG PC Client RIM :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/wp-content/uploads/TCG-PC-Client-Reference-Integrity-Manifest-Specification-Version-1.1-Revision-11_pub.pdf). See `-l` option [documentation](#-l-option-pc-client-rim-only).
[^2]: Provide `-p` or `-k`. For COSE, `-e` can instead use an embedded certificate. For PC Client RIMs, use `-k` alone or the certificate path with `-p`; embedded certificates are processed automatically on that path.
[^3]: Required for certificate-chain validation when a PC Client RIM contains an embedded signing certificate. With no embedded certificate, verification uses the supplied key or certificate without checking a certificate chain.
[^4]: The embedded certificate supersedes `-p`. It is checked against the protected-header thumbprint; this does not validate a certificate chain.

### `-l` option (PC Client RIM only)

The default behavior for the `verify` command for a [TCG PC Client RIM](../RIMs/tcg-pc-client-rim.md) is to *ignore* the file attribute that specifies the name of the Support RIM file, as well as the hash associated with that file.

When verifying with a certificate (`-p`), this parameter enables verification of the Support RIM file's hash as follows:

- `-l ""`: uses the `<Directory>` element path (if present) associated with the corresponding `<File>` element to find the Support RIM.

- `-l <path>`: uses the supplied path, along with the file attribute, to find the Support RIM file.

PC Client verification with a public key alone (`-k`) checks only the XML signature; it does not check Support RIM hashes, even if `-l` is supplied.

Support RIM hash verification currently supports SHA-256 only.

??? info "Further Details"
    [NISTIR 8060 :fontawesome-solid-external-link:](https://doi.org/10.6028/NIST.IR.8060), section 4.6.1 states:
    > Files are described using the `<File>` element, and folders are described using the `<Directory>` element.

    When processing a PC Client RIM with a certificate and `-l ""`, the `RIM-Tool` combines the `<Directory>` element's `location` attribute (if present) with the `<File>` element's `name` attribute in the Base RIM's payload to locate the Support RIM.

See below [Examples](#examples) section for sample usage of the `-l` option.

## Examples

!!! example "Before Starting"
    Before running these examples, please ensure that you change into the `data` directory (requires [installation](../getting-started/setup.md#installation)):
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
