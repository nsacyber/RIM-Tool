---
title: RIM Formats
---

# RIM Formats

The `RIM-Tool` supports the following RIM types as options when using various [commands](../reference/index.md). Please see each type's linked page for further discussion.

Choose a format below, then prepare its [JSON configuration file](configuration-files.md) with the metadata and payload references needed by the [`create` command](../reference/create.md).

| RIM Type (`-r` Option)                 | Description                       | Encoding | Applicable Specification(s)                                 | Signature |
|----------------------------------------|-----------------------------------|----------|-------------------------------------------------------------|-----------|
| [`pcrim`](tcg-pc-client-rim.md)        | TCG PC Client RIM                 | XML      | TCG PC Client RIM[^1]                                       | XML-DSig  |
| [`comp_swid`](tcg-component-rim.md)    | TCG Component RIM (SWID option)   | XML      | TCG Component RIM[^2], ISO/IEC 19770-2:2015[^3]             | XML-DSig  |
| [`comp_coswid`](tcg-component-rim.md)  | TCG Component RIM (CoSWID option) | CBOR     | TCG Component RIM, RFC 9393[^4], RFC 9052[^5], RFC 9360[^6] | COSE      |
| [`coswid`](coswid.md)                  | CoSWID tag                        | CBOR     | RFC 9393, RFC 9052, RFC 9360                                | COSE      |
| [`corim_comid`](corim.md)              | CoRIM with CoMID tag(s)           | CBOR     | draft-ietf-rats-corim[^7], RFC 9052, RFC 9360               | COSE      |

!!! info

    Currently, `comp_swid` creation requires signing, and `--unsigned` is not currently implemented for that type.

    Creation of CoRIMs containing CoSWID or CoTL tags is not implemented at this time; use `corim_comid` for the supported CoRIM workflow.

[^1]: See TCG PC Client RIM specification [here :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/resource/tcg-pc-client-reference-integrity-manifest-specification/).
[^2]: See TCG Component RIM Binding (for SWID/CoSWID) specification [here :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/resource/tcg-component-rim-binding-for-swid-and-coswid/).
[^3]: See ISO/IEC 19770-2:2015 specification [here :fontawesome-solid-external-link:](https://www.iso.org/standard/65666.html).
[^4]: See RFC 9393 [here :fontawesome-solid-external-link:](https://www.rfc-editor.org/rfc/rfc9393.html).
[^5]: See RFC 9052 [here :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/rfc9052/).
[^6]: See RFC 9360 [here :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/rfc9360/).
[^7]: See IETF CoRIM specification [here :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/draft-ietf-rats-corim/).
