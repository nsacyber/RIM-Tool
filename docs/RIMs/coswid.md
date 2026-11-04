---
title: CoSWID Tag
---

# CoSWID Tag

*CoSWID* (Concise Software Identification) tags are defined in RFC 9393[^1] and are based on the SWID specification. NIST IR 8060[^2] provides public guidance for creating interoperable SWID tags. CoSWID includes metadata such as `edition`, `colloquial-version`, and `product`.

In contrast to SWID tags, which use XML encoding, CoSWID tags use CBOR[^3] encoding.

## Details

The CoSWID specification uses the Concise Data Definition Language (CDDL), defined in RFC 8610[^4], to define CoSWID structures in terms of arrays and maps (which hold key/value pairs). Most CoSWID fields are optional.

CoSWID tags can be signed using a COSE[^5] signature envelope. RFC 9393 defines the COSE content type `"application/swid+cbor"` to identify the payload as a CoSWID tag.

[^1]: See RFC 9393 [here :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/html/rfc9393).
[^2]: See NIST IR 8060 [here :fontawesome-solid-external-link:](https://nvlpubs.nist.gov/nistpubs/ir/2016/NIST.IR.8060.pdf).
[^3]: Stands for Concise Binary Object Representation, defined in [RFC 8949 :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/html/rfc8949).
[^4]: See RFC 8610 [here :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/html/rfc8610).
[^5]: Stands for CBOR Object Signing and Encryption, defined in [RFC 9052 :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/rfc9052/).
