---
title: Validation of Component
---

# Firmware Validation for a PC Component

For a PC component, the RIM is created as a Concise Binary Object Representation (CBOR)-encoded file. 
The `RIM-Tool` supports the [CoSWID Tag](../RIMs/coswid.md), the [TCG Component RIM](../RIMs/tcg-component-rim.md), and the [CoRIM](../RIMs/corim.md).

## CoSWID

CoSWID is defined by [rfc 9393 :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/html/rfc9393). 
It is based on the SWID spec (ISO/IEC 19770-2:2015). NIST created 
[NIST IR 8060 :fontawesome-solid-external-link:](https://nvlpubs.nist.gov/nistpubs/ir/2016/NIST.IR.8060.pdf) 
as an open stand-in for the spec, which defines a few extra meta fields that are contained within CoSWID (edition, 
colloquial-version, product, etc.). A couple of items about the structure of CoSWID:

1. Encoding: CoSWID drops the xml encoding specified in SWID in favor of CBOR -
[rfc 8949 :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/html/rfc8949).

2. Structure: The CoSWID spec uses Concise Data Definition Language (CDDL) - 
[rfc 8610 :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/html/rfc8610). CDDL uses 
both arrays and maps to define data structures (a map type of array limited to key/value pairs). Most 
CoSWID items are optional.

3. Signature: CoSWID objects are signed using the COSE signature envelope. Rfc 9393 defines a context attribute of 
“application/swid+cbor” to provide a hint to the parser that the payload is a CoSWID object.

While there are technically few implementations of systems requiring CoSWID, it is making its way into 
other specifications listed on this page.

## TCG Component RIM Binding for SWID/CoSWID

The [TCG Component RIM Binding for SWID/CoSWID :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/wp-content/uploads/TCG-Component-RIM-Binding-for-SWID-and-CoSWID-Version-1.0-RC-2_16April25.pdf) 
defines 2 formats/encoding for component RIMs: SWID(XML) and CoSWID (CBOR). It provides extensions 
support attestation of the DMTF’s 
[Security Protocols and Data Model Specification :fontawesome-solid-external-link:](https://www.dmtf.org/standards/spdm). 
The attributes defined in this specification closely mimic those defined by the PC Client RIM 
specification. This specification is intended to support devices adhering the TCG 
[DICE specifications :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/work-groups/dice-architectures/), 
which support devices that do not contain a TPM. DICE has definitions for CoRIM as well, so DICE 
may use either TCG Component RIM or CoRIM format.

## CoRIM

[Concise RIM :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/draft-ietf-rats-corim/) (CoRIM) is defined by IETF. IETF 
sponsors the 
[Remote ATtestation ProcedureS (RATS) working Group :fontawesome-solid-external-link:](https://datatracker.ietf.org/group/rats/about/) 
which promotes specifications that support attestation. 

As shown in the 
[corim.txt :fontawesome-solid-external-link:](https://github.com/ietf-rats-wg/draft-ietf-rats-corim/blob/main/pics/corim.txt) 
diagram, the CoRIM can contain a CoSWID, CoMID, or a Concise Tag List (CoTL). For the `RIM-Tool`’s 
create command, the use of rim-type parameter is used to denote which option is uses. Note 
that currently only the CoSWID (corim_cosid) and CoMID (corim_comid) options are supported.

CoRIMs are digitally signed using 
[COSE :fontawesome-solid-external-link:](https://datatracker.ietf.org/doc/rfc9052/) 
with extra requirements placed upon the protected header.  Of interest is the context parameter defined 
as “application/rim+cbor” for CoRIM as opposed to “application/swid+cbor” when signing a CoSWID object. 
This helps the parser determine the type of data found in the payload.

## Reference Integrity Manifest Specifications for a PC Component

* The
[TCG Component Reference Integrity Manifest Information Model :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/resource/tcg-component-reference-integrity-manifest-information-model/)
complements the RIM Information Model by defining a Component Reference Integrity Manifest (RIM)
Information Model (IM) for components of a platform, e.g., a PC Client or Server platform.
* The [TCG Component RIM Binding for SWID/CoSWID :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/wp-content/uploads/TCG-Component-RIM-Binding-for-SWID-and-CoSWID-Version-1.0-Revision-22_9October24.pdf)
complies with the TCG Component RIM Information Model Specification and provides additional requirements
for a SWID/CoSWID RIM file.