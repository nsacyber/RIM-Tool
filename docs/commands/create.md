---
title: Create
---

# `create` Command
Creates a RIM, CoRIM, or CoSWID tag from a given configuration input file, and writes to a specified output file.

Can optionally sign as well as create. Please see the [Sign](sign.md) command page for further details.

## Usage

=== "Unsigned"
    ```shell
    rim create -r <string> -c <file> -u -o <file> [-l <file>] [-a <string>]
    ```
=== "Signed"
    ```shell
    rim create -r <string> -c <file> -k <file> -o <file> [-p <file>] [-l <file>] [-a <string>] [-d <payload-file>] [-e] [-pk <hex>] [-uk <hex>]
    ```

## Parameters

=== "Unsigned"
    | Parameter                    | Description                                                             | Required    | Type   |
    |------------------------------|-------------------------------------------------------------------------|-------------|--------|
    | `-r`, `--rim-type`           | Specifies the [RIM type](../RIMs/index.md).                             | Yes         | String |
    | `-c`, `--config`             | Specifies the [configuration file](../configuration-files.md) for building the RIM.   | Yes    | File   |
    | `-l`, `--rimel`              | Support RIM (PC Client RIM only).                                       | Yes[^1]     | File   |
    | `-a`, `--algorithm`          | Support RIM hash algorithm (PC Client RIM only), SHA256 by default.[^3]  | No          | String |
    | `-u`, `--unsigned`           | Specifies that the RIM is unsigned.                                     | Yes         |        |
    | `-o`, `--out`                | The path of the file to write the RIM to.                               | Yes         | File   |
=== "Signed"
    | Parameter                    | Description                                                             | Required    | Type   |
    |------------------------------|-------------------------------------------------------------------------|-------------|--------|
    | `-r`, `--rim-type`           | Specifies the [RIM type](../RIMs/index.md).                             | Yes         | String |
    | `-c`, `--config`             | Specifies the [configuration file](../configuration-files.md) for building the RIM.   | Yes    | File   |
    | `-l`, `--rimel`              | Support RIM (PC Client RIM only).                                       | Yes[^1]     | File   |
    | `-k`, `--private-key-file`   | The private key used to sign the Base RIM created by this tool.         | Yes         | File   |
    | `-p`, `--public-certificate` | The public key certificate to be used to verify the RIM.                | Yes[^2]     | File   |
    | `-a`, `--algorithm`          | Support RIM hash algorithm for PC Client RIMs; signing algorithm for COSE.[^3] | No     | String |
    | `-d`, `--detached`           | For CoSWIDs, writes the detached payload to this path; `-o` holds the COSE signature envelope. | No | File |
    | `-e`, `--embed-cert`         | Embeds a certificate, depending on output type.[^4]                     | No          |        |
    | `-pk`, `--protected-kid`     | A hexadecimal string that represents the key identifier to place in the COSE protected header.[^5]  | No          | String |
    | `-uk`, `--unprotected-kid`   | A hexadecimal string that represents the key identifier to place in the COSE unprotected header.[^5]| No          | String |
    | `-o`, `--out`                | The path of the file to write the RIM to.                               | Yes         | File   |

[^1]: Required when creating a [TCG PC Client RIM](../RIMs/tcg-pc-client-rim.md), including when the configuration already declares a file hash. Optional for other RIM types.
[^2]: Required for signed XML RIMs and CoRIMs. For CoSWIDs, a JSON Web Key can supply the signing algorithm without a separate certificate.
[^3]: For PC Client RIMs, use `SHA256` (default), `SHA384`, or `SHA512`. For signed COSE output, use an [IANA-registered COSE algorithm :fontawesome-solid-external-link:](https://www.iana.org/assignments/cose/cose.xhtml); the certificate or JSON Web Key supplies the algorithm when provided.
[^4]:
    For PC Client [XML-DSig :fontawesome-solid-external-link:](https://www.w3.org/TR/xmldsig-core/): the provided certificate is embedded into the signed SWID tag. Component SWID creation does not currently embed the certificate.
    
    For [COSE :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/html/rfc8152): the provided certificate and its thumbprint are embedded into the protected header.
[^5]: Used for CoSWID COSE headers. If both are supplied, the protected KID takes precedence.

## Examples

!!! example "Before Starting"
    Before running these examples, please ensure that you change into the `data` directory (requires [installation](../setup.md#installation)):
    === "Linux"
        ```shell
        cd /opt/rimtool/data
        ```

Create a signed [TCG PC Client Base RIM](../RIMs/tcg-pc-client-rim.md):

=== "Linux"
    ```shell
    rim create -r pcrim -c pcrim/rim_fields.json -l pcrim/laptop.default.1.rimel -p pcrim/RimSignCert.pem -k pcrim/rimKey.pem --out ~/laptop.default.1.swidtag
    ```

Create an unsigned [CoSWID tag](../RIMs/coswid.md):

=== "Linux"
    ```shell
    rim create -r coswid -u -c coswid/coswid_rim_1.json --out ~/coswid_rim_1.coswid
    ```

Create a [CoRIM](../RIMs/corim.md) using CoMID:

=== "Linux"
    ```shell
    rim create -r corim_comid -c corim/corim_1.json -k keys/COMP_OEM1_rim_signer_ecc_512_sha384.key -p certs/COMP_OEM1_rim_signer_ecc_512_sha384.pem --out ~/corim_1.corim.cose
    ```
