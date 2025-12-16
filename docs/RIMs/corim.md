---
title: CoRIM
---

# CoRIM

IETF sponsors the Remote ATtestation ProcedureS (RATS) Working Group[^1] which standardizes formats that support attestation. A *Concise RIM*[^2] (CoRIM), as defined by the IETF RATS WG, is a device Reference Integrity Manifest (RIM) to be published by a device vendor.

## Details

A CoRIM comprises metadata that establishes information about the CoRIM itself and various attributes (including creator, signer, etc.), in addition to a list of tags.

As shown in a diagram[^3] of a CoRIM produced by the RATS WG, a CoRIM tag list can contain one or more CoSWID, Concise Module Identifier (CoMID), or Concise Tag List (CoTL) tags. For the RIM tool's [`create`](../commands/create.md) command, the use of the `--rim-type` parameter is used to denote which CoRIM tag option is used.

!!! Note

    Currently, the RIM tool only supports CoSWID (`corim_coswid`) and CoMID (`corim_comid`) options for CoRIMs.

### Signing

CoRIMs are signed using COSE[^4] digital signatures, requiring specific fields in the COSE protected header. 

Of interest is the context parameter, which is defined as `"application/rim+cbor"` for CoRIMs (as opposed to `"application/swid+cbor"` when signing a standalone CoSWID tag). This helps the parser determine the type of data found in the payload.

[^1]: See IETF RATS WG charter [here :fontawesome-solid-external-link:](https://datatracker.ietf.org/group/rats/about/).
[^2]: See IETF CoRIM specification [here :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/draft-ietf-rats-corim/).
[^3]: See CoRIM text diagram on ietf-rats-wg GitHub [here :fontawesome-solid-external-link:](https://github.com/ietf-rats-wg/draft-ietf-rats-corim/blob/main/pics/corim.txt).
[^4]: Stands for CBOR Object Signing and Encryption, defined in [RFC 9052 :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/rfc9052/).