---
title: CoSWID Tag
---

# CoSWID Tag

*CoSWID* (Concise Software Identification) tags are defined in RFC 9393[^1]. These are based upon the SWID specification. NIST has also created NIST IR 8060[^2] as an open stand-in for the specification, defining
a few extra meta-fields that are contained within CoSWID tags (`edition`, `colloquial-version`, `product`, etc.).

In contrast to SWID tags, which use XML encoding, CoSWID tags use CBOR[^3] encoding.

## Details

The CoSWID specification uses the Concise Data Definition Language (CDDL), defined in RFC 8610[^4], to define CoSWID structures in terms of arrays and maps (a type of array limited to key/value pairs). Most of these items are optional.

CoSWID tags are signed using a COSE[^5] signature envelope. RFC 9393 defines a context attribute of `"application/swid+cbor"` to provide a hint to the parser that the payload is a CoSWID-defined object.

At present, while there are technically few implementations of systems requiring CoSWID tags, support is making its way into related specifications mentioned on this page.

[^1]: See RFC 9393 [here :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/html/rfc9393).
[^2]: See NIST IR 8060 [here :fontawesome-solid-external-link:](https://nvlpubs.nist.gov/nistpubs/ir/2016/NIST.IR.8060.pdf).
[^3]: Stands for Concise Binary Object Representation, defined in [RFC 8949 :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/html/rfc8949).
[^4]: See RFC 8610 [here :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/html/rfc8610).
[^5]: Stands for CBOR Object Signing and Encryption, defined in [RFC 9052 :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/rfc9052/).