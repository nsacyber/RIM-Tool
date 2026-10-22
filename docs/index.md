---
title: Welcome
---

# Welcome to `RIM-Tool`

`RIM-Tool` is an open-source command-line tool that can be used for a variety of tasks related to the authorship and
testing of Reference Integrity Manifests[^1] (RIMs), including creation, printing, verification, and signing.

[^1]: Defined in the Trusted Computing Group [specification :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/resource/tcg-reference-integrity-manifest-rim-information-model/).

Follow [Getting Started](getting-started.md) to create a test PC Client RIM bundle with the Linux helper script.
See [Setup](setup.md#installation) for installation instructions, or [Commands](commands/index.md) for command reference.

!!! note

    In addition to TCG PC Client RIMs, the tool supports TCG Component RIMs, Concise Software Identification (CoSWID) tags,
    and Concise Reference Integrity Manifests (CoRIMs). See the [Supported RIM Types](./RIMs/index.md) page for details.

## Compatibility

The [Host Integrity at Runtime and Startup :fontawesome-solid-external-link:](https://github.com/nsacyber/hirs) (HIRS) project uses
[TCG PC Client RIMs](./RIMs/tcg-pc-client-rim.md) for firmware validation. Support for other RIM formats depends on the HIRS version; consult its documentation before choosing a format for use with HIRS.

## About
The source code can be found on [GitHub :fontawesome-solid-external-link:](https://github.com/nsacyber/rim-tool).

### License
`RIM-Tool` is distributed under the [Apache 2.0 :fontawesome-solid-external-link:](https://www.apache.org/licenses/LICENSE-2.0) license.
