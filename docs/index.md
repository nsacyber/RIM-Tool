---
title: Welcome
hide:
  - toc
---

# Welcome to `RIM-Tool`

`RIM-Tool` is an open-source command-line tool that can be used for a variety of tasks related to the authorship and
testing of Reference Integrity Manifests[^1] (RIMs), including creation, printing, verification, and signing.

[^1]: Defined in the Trusted Computing Group [specification :fontawesome-solid-external-link:](https://trustedcomputinggroup.org/resource/tcg-reference-integrity-manifest-rim-information-model/).

<div class="grid cards" markdown>

-   :material-file-plus-outline:{ .lg .middle } **Create Your First RIM Bundle**

    ---

    Create and verify a test PC Client RIM bundle on Linux.

    [:octicons-arrow-right-24: Getting Started](getting-started/index.md)

-   :material-download:{ .lg .middle } **Set Up the Tool**

    ---

    Install a Linux package or build from source.

    [:octicons-arrow-right-24: Setup](getting-started/setup.md)

-   :material-console:{ .lg .middle } **Look Up a Command**

    ---

    Find command options and usage examples.

    [:octicons-arrow-right-24: Command reference](reference/index.md)

-   :material-file-document-outline:{ .lg .middle } **Choose a RIM Format**

    ---

    Compare supported formats and prepare their JSON configuration files.

    [:octicons-arrow-right-24: RIM Formats](RIMs/index.md)

</div>

!!! note

    In addition to TCG PC Client RIMs, the tool supports TCG Component RIMs, Concise Software Identification (CoSWID) tags,
    and Concise Reference Integrity Manifests (CoRIMs). See [RIM Formats](./RIMs/index.md) for details.

For an introduction to firmware validation and how RIMs fit together, see [Background](background/index.md). For license and compatibility, see [About](./about.md).
