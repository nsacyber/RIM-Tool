---
title: Sign
---

# `sign` Command
Signs a file using specific algorithm and formatting options. The signature can either be *attached* or *detached*:

- Attached: the signature and payload (content) are contained in the same output file.
- Detached: the signature is output to a separate file, which will not contain the payload.

The resultant signed output will be a CBOR-encoded file.

!!! info

    Currently, the `sign` command only supports CBOR-encoded types on the [RIM types](../RIMs/index.md) page. 

## Usage
```shell
rim sign -r <string> -i <file> -k <file> -p <file> -o <file> [-l <file>] [-al <string>] [-d] [-e] [-pk <file>] [-uk <file>]
```

## Parameters

| Parameter                    | Description                                                                                          | Required | Type   |
|------------------------------|------------------------------------------------------------------------------------------------------|----------|--------|
| `-r`, `--rim-type`          | Specifies the [RIM type](../RIMs/index.md).                                                          | Yes      | String |
| `-i`, `--in`                 | Specifies the input file to sign.                                                                    | Yes      | File   |
| `-k`, `--private-key-file`   | The private key used to sign the specified file.                                                     | Yes      | File   |
| `-p`, `--public-certificate` | The public key certificate to be used during the signing process.                                    | Yes[^1]  | File   |
| `-al`, `--algorithm`         | The algorithm used[^2]. By default, uses the certificate algorithm.                                  | No       | String |
| `-d`, `--detached`           | Uses a detached signature file. By default, embeds the signature.                                    | No       | File   |
| `-e`, `--embed-cert`         | Embeds a certificate, depending on output type.[^3]                                                  | No       |        |
| `-pk`, `--protected-kid`     | A hexadecimal string that represents the key identifier to place in the COSE protected header.[^4]   | No       | String |
| `-uk`, `--unprotected-kid`   | A hexadecimal string that represents the key identifier to place in the COSE unprotected header.[^4] | No       | String |
| `-o`, `--out`                | The path of the file to write the signed file to.                                                    | Yes      | File   |

[^1]: Some key files may include a certificate, in which case this option may not be needed.
[^2]: Must be an [IANA-registered COSE algorithm :fontawesome-solid-external-link:](https://www.iana.org/assignments/cose/cose.xhtml).
[^3]: For [XML-DSig :fontawesome-solid-external-link:](https://www.w3.org/TR/xmldsig-core/): the provided certificate is embedded into the signed SWID tag. For [COSE :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/html/rfc8152): the provided certificate and its thumbprint are embedded into the protected header.
[^4]: Applicable to COSE types only (CoSWIDs and CoRIMs). By default, uses SKID of verification certificate.

## Examples

!!! example "Before Starting"
Before running these examples, please ensure that you change into the `data` directory (requires [install](../getting-started.md/#installation)):
=== "Linux"
```shell
cd /opt/rimtool/data
```

Sign an unsigned [CoSWID tag](../RIMs/coswid.md):
=== "Linux"
```shell
rim sign -r coswid --in coswid/coswid_rim_1.coswid -k keys/COMP_OEM1_rim_signer_ecc_512_sha384.key -p certs/COMP_OEM1_rim_signer_ecc_512_sha384.pem --out ~/coswid_rim_1.signed.coswid.cose
```

Sign an unsigned [CoRIM](../RIMs/corim.md):
=== "Linux"
```shell
rim sign -r corim_comid --in corim/corim_1.corim.cose -k keys/COMP_OEM1_rim_signer_ecc_512_sha384.key -p certs/COMP_OEM1_rim_signer_ecc_512_sha384.pem --out ~/corim_1.signed.corim.cose
```
