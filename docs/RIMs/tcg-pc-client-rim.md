---
title: TCG PC Client RIM
---

# TCG PC Client RIM

The Trusted Computing Group has defined the TCG PC Client Reference Integrity Manifest, or *TCG PC Client RIM*, specification[^1].
The intent of this specification is to define a structure that can capture integrity measurements (in the form of cryptographic message digests) for firmware and boot software taken by organizations that participate in the production process (OEMs, system integrators, value-added resellers, etc.).

These structures (i.e., RIMs) provide cryptographically verifiable firmware integrity measurements that an end verifier can use to provide an automated means to verify the firmware and boot software for the platform.

## Details

The TCG PC Client RIM defines a *RIM bundle* which includes a single Base RIM and one or more Support RIM files.
The *Base RIM* is a SWID tag with a set of meta-attributes defined by the specification. The payload consists of directories, files, and file content hashes which refer to the *Support RIM* files.
The Support RIM files can be:

- A TPM Event Log[^2] or Partial TCG Event Log Assertion file, using the event format defined by the TCG PC Client Specific Platform Firmware Profile specification[^3].

- A TPM PCR Assertion file defined in the TCG PC Client Reference Integrity Manifest specification[^1].

The Base RIM payload can also reference other Base RIMs, including [Component RIMs](tcg-component-rim.md), to form a composite or nested RIM.
These nested structures can contain different RIM formats signed by different entities.
PC Client vendors may choose to provide RIMs of components they include in their product (either signed by them or signed by the component vendor).

These options specify flexibility as well as complexity,
allowing the industry to select the best approach for providing end customers the ability to attest the firmware integrity of devices in their ecosystem.    

[^1]: See the TCG PC Client RIM specification [here :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/resource/tcg-pc-client-reference-integrity-manifest-specification/).
[^2]: See the HIRS GitHub wiki article on [TPM event logs :fontawesome-solid-external-link:](https://github.com/nsacyber/HIRS/wiki/TPM-Event-Logs).
[^3]: See the TCG PC Client Specific Platform Firmware Profile specification [here :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/resource/pc-client-specific-platform-firmware-profile-specification/).
