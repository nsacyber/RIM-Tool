---
title: RIM Types
---

# Supported RIM Types

The `RIM-Tool` supports the following RIM types as options when using various [commands](../commands/index.md). Please see each type's linked page for further discussion.

| RIM Type (`-r` Option)                 | Description                       | Encoding | Applicable Specification(s)                                 | Signature |
|----------------------------------------|-----------------------------------|----------|-------------------------------------------------------------|-----------|
| [`pcrim`](tcg-pc-client-rim.md)        | TCG PC Client RIM                 | XML      | TCG PC Client RIM[^1]                                       | XML-DSig  |
| [`comp_swid`](tcg-component-rim.md)    | TCG Component RIM (SWID option)   | XML      | TCG Component RIM[^2], ISO/IEC 19770-2:2015[^3]             | XML-DSig  |
| [`comp_coswid`](tcg-component-rim.md)  | TCG Component RIM (CoSWID option) | CBOR     | TCG Component RIM, RFC 9393[^4], RFC 9052[^5], RFC 9360[^6] | COSE      |
| [`coswid`](coswid.md)                  | CoSWID tag                        | CBOR     | RFC 9393, RFC 9052, RFC 9360                                | COSE      |
| [`corim_comid`](corim.md)              | CoRIM with CoMID tag(s)           | CBOR     | draft-rats-ietf-corim[^7], RFC 9052, RFC 9360               | COSE      |
| [`corim_coswid`](corim.md)             | CoRIM with CoSWID tag(s)          | CBOR     | draft-rats-ietf-corim, RFC 9393, RFC 9052, RFC 9360         | COSE      |

[^1]: See TCG PC Client RIM specification [here :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/resource/tcg-pc-client-reference-integrity-manifest-specification/).
[^2]: See TCG Component RIM Binding (for SWID/CoSWID) specification [here :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/wp-content/uploads/TCG-Component-RIM-Binding-for-SWID-and-CoSWID-Version-1.0-Revision-22_9October24.pdf).
[^3]: See ISO/IEC 19770-2:2015 specification [here :fontawesome-solid-external-link:](https://www.iso.org/standard/65666.html).
[^4]: See RFC 9393 [here :fontawesome-solid-external-link:](https://www.rfc-editor.org/rfc/rfc9393.html).
[^5]: See RFC 9052 [here :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/rfc9052/).
[^6]: See RFC 9360 [here :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/rfc9360/).
[^7]: See IETF CoRIM specification [here :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/draft-ietf-rats-corim/).