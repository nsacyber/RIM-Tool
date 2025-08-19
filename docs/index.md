---
title: Welcome
---

# Welcome to `RIM-Tool`

`RIM-Tool` is an open-source command-line tool that can be used for a variety of tasks related to the authorship and
testing of Reference Integrity Manifests[^1] (RIMs), including creation, printing, verification, and signing.

[^1]: Defined in the Trusted Computing Group [specification :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/resource/tcg-reference-integrity-manifest-rim-information-model/).

Please see the [Getting Started](getting-started.md) page for setup and installation details, or the [Commands](commands/index.md) page 
for the list of available commands.

!!! note

    In addition to TCG PC Client RIMs, the tool supports TCG Component RIMs, Concise Software Identification (CoSWID) tags,
    and Concise Reference Integrity Manifests (CoRIMs). See the [Supported RIM Types](./RIMs/index.md) page for details.

## Compatibility

RIMs produced by this tool are compatible with the [Host Integrity at Runtime and Startup :fontawesome-solid-external-link:](https://github.com/nsacyber/hirs) (HIRS) project. 
Currently, HIRS only processes [TCG-defined PC Client RIMs](./RIMs/tcg-pc-client-rim.md), but future versions should be able to process all RIMs produced by this tool.

## About
The source code can be found on [GitHub :fontawesome-solid-external-link:](https://github.com/nsacyber/rim-tool).

### License
`RIM-Tool` is distributed under the [Apache 2.0 :fontawesome-solid-external-link:](https://www.apache.org/licenses/LICENSE-2.0) license.