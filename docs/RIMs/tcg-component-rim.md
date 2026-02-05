---
title: TCG Component RIM
---

# TCG Component RIM
A Component Reference Integrity Manifest[^1], or *Component RIM*, as defined by the Trusted Computing Group (TCG) is similar in concept to a [PC Client RIM](tcg-pc-client-rim.md), but is designed for hardware components of a platform
(such as graphics adapters, microprocessors, etc.). 

## Details

The TCG Component RIM Binding specification[^2] defines two formats/encoding for Component RIMs: SWID (XML) and [CoSWID](coswid.md) (CBOR). The 
attributes defined in the binding specification closely mimic those defined by the [TCG PC Client RIM](tcg-pc-client-rim.md) specification.

It also provides extensions which support attestation of the DMTF's Security Protocols and Data Model (SPDM) specification[^3].

It is further envisioned that this specification can support devices adhering to the TCG DICE[^4] set of specifications which support devices that do not contain a Trusted Platform Module (TPM). DICE also contains definitions for [CoRIMs](corim.md), so ether format may present in the future. 

[^1]: See the TCG Component Reference Integrity Manifest Information Model specification [here :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/wp-content/uploads/TCG-Component-Reference-Integrity-Manifest-Information-Model-Version-1.0_pub.pdf).
[^2]: See the TCG Component RIM Binding (for SWID/CoSWID) specification [here :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/wp-content/uploads/TCG-Component-RIM-Binding-for-SWID-and-CoSWID-Version-1.0-RC-2_16April25.pdf).
[^3]: See details about SPDM [here :fontawesome-solid-external-link:](https://www.dmtf.org/standards/spdm).
[^4]: See details about TCG DICE [here :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/work-groups/dice-architectures/).