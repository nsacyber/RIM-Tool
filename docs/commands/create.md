---
title: Create
---

# `create` Command
Creates a RIM, CoRIM, or CoSWID tag from a given configuration input file, and writes to a specified output file.

Can optionally sign as well as create. Please see the [Sign](sign.md) command page for further details.

## Usage

=== "Unsigned"
    ```shell
    rim create -rt <string> -a <file> -u -o <file> [-l <file>]
    ```
=== "Signed"
    ```shell
    rim create -rt <string> -a <file> -k <file> -p <file> -o <file> [-l <file>] [-al <string>] [-d] [-e] [-pk <file>] [-uk <file>]
    ```

## Parameters

=== "Unsigned"
    | Parameter                    | Description                                                             | Required    | Type   |
    |------------------------------|-------------------------------------------------------------------------|-------------|--------|
    | `-rt`, `--rim-type`          | Specifies the [RIM type](../RIMs/index.md).                             | Yes         | String |
    | `-a`, `--attributes`         | Specifies the [configuration file](../configuration-files.md) for building the RIM. | Yes         | File   |
    | `-l`, `--rimel`              | Support RIM (PC Client RIM only).                                       | No[^1]      | File   |
    | `-u`, `--unsigned`           | Specifies that the RIM is unsigned.                                     | Yes         |        |
    | `-o`, `--out`                | The path of the file to write the RIM to.                               | Yes         | File   |
=== "Signed"
    | Parameter                    | Description                                                             | Required    | Type   |
    |------------------------------|-------------------------------------------------------------------------|-------------|--------|
    | `-rt`, `--rim-type`          | Specifies the [RIM type](../RIMs/index.md).                             | Yes         | String |
    | `-a`, `--attributes`         | Specifies the [configuration file](../configuration-files.md) for building the RIM. | Yes         | File   |
    | `-l`, `--rimel`              | Support RIM (PC Client RIM only).                                       | No[^1]      | File   |
    | `-k`, `--private-key-file`   | The private key used to sign the Base RIM created by this tool.         | Yes         | File   |
    | `-p`, `--public-certificate` | The public key certificate to be used to verify the RIM.                | Yes[^2]     | File   |
    | `-al`, `--algorithm`         | The algorithm used[^3]. By default, uses the certificate algorithm.     | No          | String |
    | `-d`, `--detached`           | Uses a detached signature file. By default, embeds the signature.       | No          | File   |
    | `-e`, `--embed-cert`         | Embeds a certificate, depending on output type.[^4]                     | No          |        |
    | `-pk`, `--protected-kid`     | A hexadecimal string that represents the key identifier to place in the COSE protected header.[^5]  | No          | String |
    | `-uk`, `--unprotected-kid`   | A hexadecimal string that represents the key identifier to place in the COSE unprotected header.[^5]| No          | String |
    | `-o`, `--out`                | The path of the file to write the RIM to.                               | Yes         | File   |

[^1]: A Support RIM file is only used for a [TCG PC Client RIM :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/wp-content/uploads/TCG-PC-Client-Reference-Integrity-Manifest-Specification-Version-1.1-Revision-11_pub.pdf). If this type is used, this option is required.
[^2]: Some key files may include a certificate, in which case this option may not be needed.
[^3]: Must be an [IANA-registered COSE algorithm :fontawesome-solid-external-link:](https://www.iana.org/assignments/cose/cose.xhtml).
[^4]: For [XML-DSig :fontawesome-solid-external-link:](https://www.w3.org/TR/xmldsig-core/): the provided certificate is embedded into the signed SWID tag. For [COSE :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/html/rfc8152): the provided certificate and its thumbprint are embedded into the protected header.
[^5]: Applicable to COSE types only (CoSWIDs and CoRIMs). By default, uses SKID of verification certificate.

## Examples
Create a signed [TCG PC Client Base RIM](../RIMs/tcg-pc-client-rim.md):

```shell
rim create -rt pcrim -a rim_fields.json -l TPMEventLog.1.rimel -p RimSignCert.pem -k rimKey.pem --out test.1.swidtag 
```

Create an unsigned [CoSWID tag](../RIMs/coswid.md):

```shell
rim create -rt coswid -u -a coswid_rim.1.json --out test.1.coswid
```

Create a [CoRIM](../RIMs/corim.md) using CoMID:

```shell
rim create -rt corim-comid -a corim_1.json -k rimKey.pem -p RimSignCert.pem --out corim-test.cose 
```