---
title: Configuration Files
---

# Configuration Files

The RIM Tool uses JSON configuration files to populate fields associated with the chosen
[RIM type](./RIMs/index.md). Create or copy a configuration before using the [`create`](./commands/create.md) command.

!!! tip

    Use of a JSON linting tool is highly recommended in order to ensure formatting issues do not prevent the RIM Tool from functioning.

Example JSON files are in the [repository's `data/` folder :fontawesome-solid-external-link:](https://github.com/nsacyber/RIM-Tool/tree/main/data), grouped by [RIM type](./RIMs/index.md). Linux packages install these examples under `/opt/rimtool/data/`. Field names and structures differ between formats; start with a configuration for the type you intend to create.

## Example

For a PC Client RIM, copy and edit `data/pcrim/rim_fields.json` from the repository, or `/opt/rimtool/data/pcrim/rim_fields.json` from an installed package. The [`create` examples](commands/create.md#examples) show how to use it. The helper in [Getting Started](getting-started.md) instead populates a configuration from the current device and records the chosen event log's hash.
