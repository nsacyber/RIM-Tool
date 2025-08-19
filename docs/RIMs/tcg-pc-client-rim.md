---
title: TCG PC Client RIM
---

# TCG PC Client RIM

The Trusted Computing Group has defined the TCG PC Client Reference Integrity Manifest, or *TCG PC Client RIM*, specification[^1].
The intent of this specification is to define a structure that can capture integrity measurements (in the form of cryptographic message digests) for firmware and boot software taken by organizations that participate in the production process (OEMs, system integrators, value-added resellers, etc.).

These structures (i.e., RIMs) provide cryptographically verifiable firmware integrity measurements that an end verifier can use to provide an automated means to verify the firmware and boot software for the platform.

## Details

The TCG PC Client RIM defines a *RIM bundle* which includes at least one Base RIM and one Support RIM file.
The *Base RIM* is a SWID tag with a set of meta-attributes defined by the specification. The payload consists of directories, files, and file content hashes which refer to the *Support RIM* files.
The Support RIM files can be:

1. A TPM event log[^2] file, defined by the TCG PC Client Specific Platform Firmware Profile specification[^3].

2. A TPM PCR assertion file defined in the TCG PC Client Reference Integrity Manifest specification[^1].

3. Another Base RIM (referred as a composite or nested RIM).

4. A [Component RIM](tcg-component-rim.md) specified by any type of valid RIM (defined by any standards body).

The third and fourth options above open the door to providing nested structures (of multiple files) containing different RIM formats, signed by different entities.
PC Client vendors may choose to provide RIMs of components they include in their product (either signed by them or signed by the component vendor).

These options specify flexibility as well as complexity,
allowing the industry to select the best approach for providing end customers the ability to attest the firmware integrity of devices in their ecosystem.    

[^1]: See the TCG PC Client RIM specification [here :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/resource/tcg-pc-client-reference-integrity-manifest-specification/).
[^2]: See the HIRS GitHub wiki article on [TPM event logs :fontawesome-solid-external-link:](https://github.com/nsacyber/HIRS/wiki/TPM-Event-Logs).
[^3]: See the TCG PC Client Specific Platform Firmware Profile specification [here :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/resource/pc-client-specific-platform-firmware-profile-specification/).
