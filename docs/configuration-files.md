---
title: Configuration Files
---

# Configuration Files

The RIM Tool configuration files are JSON-formatted configuration files that use specification defined variables that are associated 
with the [RIM type](./RIMs/index.md). These files need to be created prior to using the [`create`](./commands/create.md) command.

!!! tip

    Use of a JSON linting tool is highly recommended in order to ensure formatting issues do not prevent the RIM Tool from functioning.

For convenience, a set of pre-existing JSON files can be found in the `/data/` folder of the [GitHub project :fontawesome-solid-external-link:](https://github.com/nsacyber/hirs-coswid), each listed under subfolders with a corresponding [RIM type](./RIMs/index.md).

## Example

As an example, a sample PC Client RIM configuration file can be found at `/data/pcrim/rim_fields.json`. This file can be copied and edited to provide custom attributes. 
