---
title: Validation of PC
---

# Firmware Validation for a PC

For a PC, the RIM is created as a Software Identification (SWID) tag that adheres to the
[PC Client RIM specification :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/resource/tcg-pc-client-reference-integrity-manifest-specification/). 
The `RIM-Tool` supports the [TCG PC Client RIM](../RIMs/tcg-pc-client-rim.md).

## UEFI Boot

Firmware measurements for a UEFI compatible PC are captured by UEFI during the boot phase of 
a device. During the boot process, firmware records cryptographic hashes to the Trusted Platform 
Module (TPM). These hashes capture firmware modules, firmware configuration, expansion component 
firmware, expansion component firmware configurations, and the bootloader. TPM-aware bootloaders 
can continue logging hashes to describe the kernel, initial file system, and any modules. Kernels, 
applications, and drivers can also log runtime hashes to the TPM.

Measurements are extended into the TPM’s Platform Configuration Registers (PCRs), as shown in
Figure 1. PCRs are grouped into banks, each associated with a hash algorithm. PC Client TPMs provide
24 PCR indices per bank; the supported algorithms and active banks depend on the TPM and platform
configuration. PCR values are computed via a series of one-way hashes where
each measurement hash is appended to the current PCR value, and then the combination is hashed 
and becomes the new PCR value (referred to as “extending the PCR”).

![Secure Boot checks and TPM measurements across UEFI boot phases](../images/background_pc-rim_TpmMeasurementsSecureBootChecks.JPG "Secure Boot checks and TPM measurements")
**Figure 1.** The interaction of Secure Boot and TPM with UEFI boot phases
<!-- Hide for now
-->

UEFI also records measurement hashes, along with information about these hashes, in the TPM Event 
Log. The TPM Event Log is an audit log that can be used for verification later, after the system boots.  

On Linux systems with TPM Event Log support enabled and `securityfs` mounted, the log is commonly available at
`/sys/kernel/security/tpm0/binary_bios_measurements`.

On Windows 11, Measured Boot logs are stored as binary `.log` files under
`%SystemRoot%\Logs\MeasuredBoot\` (typically `C:\Windows\Logs\MeasuredBoot\`) when log archival is enabled.
See Microsoft's [guidance  :fontawesome-solid-external-link:](https://learn.microsoft.com/en-us/troubleshoot/windows-client/windows-security/decode-measured-boot-logs-to-track-pcr-changes) for details.

## PC RIM Bundles
A Verifier will take these UEFI measurements and compare them to a set of expected values in the RIM. 
For the PC Client, there are two different types of RIM files: the Base RIM and the Support RIMs. This 
is designated by the TCG as the “RIM Bundle”. 

### The Base RIM

The PC Client RIM defines the Base RIM as an
[ISO/IEC 19770-2 Software Identification :fontawesome-solid-external-link:](https://www.iso.org/standard/65666.html)
(SWID) standard compatible file. The Base RIM provides a verifiable identity of the RIM creator and also 
integrity information of Support RIMs. The Base RIM contains:

1. Cryptographically verifiable identification of the creator of the RIM and Support RIMs
2. A unique identifier (tagId) for a set of RIM Bundles
3. A reference to the binding specification that defines the Support RIMs
4. Cryptographic hashes (digests) of all payload references including Support RIMs
5. A digital signature of the RIM signed by the RIM’s creator

The OEM creates the RIM at the end of the production process. The OEM then stores the Base RIM 
onto the device or optionally provides a Uniform Resource Identifier (URI) to access the Base RIM. 

### The Support RIM

A Support RIM contains assertions about the state or configuration of the platform to which
the RIM applies. There are three types of Support RIMs at the time of writing, but this page describes the TCG Event Log Assertion Support RIM workflow used by HIRS.

### TCG Event Log Assertion Support RIM 

The TCG Event Log Assertion Support RIM is a binary file containing reference measurements for
the boot cycle, including expected PCR measurement hashes and their associated event details. Some
examples of events are POST_CODE, VARIABLE_DRIVER_CONFIG, EV_IPL, etc. 

!!! note

    While TCG Event Log Assertion Support RIM is similar to the TPM Event Log, the big difference
    is that the TCG Event Log Assertion Support RIM is the expected data from the OEM, while 
    the TPM Event Log is the measured data from a client PC.

When an OEM reaches the end of the production process for a PC, it captures a hash of the TCG 
Event Log Assertion Support RIM and inserts that hash into the Base RIM before the Base RIM is 
signed. Later, a Verifier can use that hash to verify that the TCG Event Log Assertion Support 
RIM has not been altered at any point. The Base RIM's signature and certificate chain are
verified separately.

The HIRS ACA uses the TCG Event Log Assertion Support RIM during its validation process if the 
Firmware Validation Policy option is selected. During the HIRS validation process, the TCG Event 
Log Assertion Support RIM is used during the Reference values check: 

1. **Real-time values check**: When the computer boots, UEFI creates the TPM Event Log (the UEFI “audit” log)
which captures the firmware events and their corresponding digests. The Provisioner collects this Event
Log and sends it to the ACA. The ACA uses the digest values within the Event Log to calculate a composite 
hash, which the ACA compares against the composite hash value in the TPM Quote. If the Quote is valid
and the reconstructed PCR values match, the log digests are consistent with the quoted PCR state. (The Quote has already
been validated based on the signature and nonce; refer to the HIRS User Guide for more information 
on this.) Should TPM Quote verification fail, the TPM Event Log file is needed to provide details 
on the individual hashes within each PCR. Basically, this first check verifies that our measured 
data is valid.

2. **Reference values check**: Now the measured data needs to be compared to the expected data. Each 
event in the TPM Event Log (supplied by the client, represents the measured data) is compared 
against the events listed in the TCG Event Log Assertion Support RIM (supplied by the OEM, 
represents the expected data) to ensure they are the same. In the case of events failing comparison, 
the Verifier can provide details on which event(s) failed. 

## PC RIM Lifecycle

The SWID specification defines four types of SWID tags: corpus, primary, patch, and supplemental. All 
four tag types come into play at various points in the software lifecycle, and support software 
management processes depend on the ability to accurately determine where each software product is 
in its lifecycle.

### Corpus Base RIM

Corpus Tags are associated with a software installation package. RIM support for corpus is currently 
undefined and not supported by this tool. 

### Primary Base RIM

A Primary Base RIM belongs to the initial RIM Bundle. For TCG Event Log Assertions, the referenced Support RIM must cover at least PCRs 0–7.

### Patch Base RIM

Patch Base RIM is intended to accompany firmware updates and include any reference measurements 
necessary to cover the changes provided by the firmware update. 

The patch links to the preceding Base RIM using a `Link` with `rel="patches"` and
`href="swid:<previous-tagId>"`. The `rimLinkHash` metadata records the digest of that preceding Base RIM.

### Supplemental Base RIM

Supplemental Base RIM: Supplemental RIMs are intended to support RIMs created by organizations 
other than the OEM (e.g. System Integrators or Value-Added Resellers (VARs)). The signature on the 
Base RIM is expected to be signed by the organization that created the Supplemental Base RIM. The 
Supplemental uses the Href element to point to the Primary or Patch RIM for which the Supplemental 
RIM is adding reference measurements. 

## Timestamps

A PC Client RIM may include a simple timestamp or a countersignature timestamp. The 
`RIM-Tool` does not currently add these timestamps. Timestamp services are typically provided by a commercial Time Stamp
Authority (TSA). Most TSAs support the RFC 3161 Time-Stamp Protocol but the timestamp itself may 
be encoded using a number of methods. 

### RFC 3339 Timestamps

An RFC 3339 timestamp uses a date and time format such as:

```shell
2026-01-01T12:00:00Z
```

### RFC 3852 Timestamps

RFC 3852 includes support for a countersignature. Creating or importing these countersignatures
is not currently supported by the `RIM-Tool`.

## Layered Endorsements (Detached Signatures)

In some cases, there may be a need for an entity that is not the tag creator to provide a secondary 
signature for the RIM (e.g., a layered endorsement). The PC Client RIM utilizes the W3C-defined 
"Detached Signature" for providing secondary signatures to be applied to an existing signed PC Client 
RIM. The initial Base RIM uses the enveloped signature provided by the tagCreator. Each secondary 
signature will be a sibling element of the BaseRIMs SoftwareIdentity element. The secondary signature 
uses the Base RIM’s tagId as the SignedInfo Reference.

The `RIM-Tool` does not currently support secondary XML signatures for layered endorsements.

## Reference Integrity Manifest Specifications for a PC

* The
  [TCG Reference Integrity Information Model :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/resource/tcg-reference-integrity-manifest-rim-information-model/)
  (RIM IM) defines structures that a Verifier uses to validate expected values (Assertions) against
  actual values (Evidence).
* The
  [PC Client RIM specification :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/resource/tcg-pc-client-reference-integrity-manifest-specification/)
  is considered a RIM IM "binding specification" specifically for PC Clients and Servers.
* The
  [TCG PC Client Platform Firmware Profile :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/resource/pc-client-specific-platform-firmware-profile-specification/),
  which is referred to as the "PFP", defines the TPM Event Log. 
