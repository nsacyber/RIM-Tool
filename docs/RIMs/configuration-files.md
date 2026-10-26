---
title: Configuration Files
---

# Configuration Files

The RIM Tool uses JSON configuration files to populate fields associated with the chosen
[RIM type](index.md). Create or copy a configuration before using the [`create`](../reference/create.md) command.

!!! tip

    Use of a JSON linting tool is highly recommended in order to ensure formatting issues do not prevent the RIM Tool from functioning.

Example JSON files are in the [repository's `data/` folder :fontawesome-solid-external-link:](https://github.com/nsacyber/RIM-Tool/tree/main/data), grouped by [RIM type](index.md). Linux packages install these examples under `/opt/rimtool/data/`. Field names and structures differ between formats; start with a configuration for the type you intend to create.

## Example

For a PC Client RIM, copy and edit `data/pcrim/rim_fields.json` from the repository, or `/opt/rimtool/data/pcrim/rim_fields.json` from an installed package. The [`create` examples](../reference/create.md#examples) show how to use it. The helper in [Getting Started](../getting-started/index.md) instead populates a configuration from the current device and records the chosen event log's hash.

## PC Client RIM Payload Hashes

A PC Client RIM (`pcrim`) with an indirect payload references a separate Support RIM, such as a TPM event log. The `hash` field in each `Payload.Directory.File` entry records the digest of that Support RIM's contents. A hash provided with an example configuration applies to the example file, so changing only the manufacturer or model does not adapt the configuration to a different Support RIM.

For your own Support RIM, update `File.name` to its filename (without directory separators), then use one of the following approaches. These examples use SHA-256 and show only the `Payload` portion of the configuration; keep the other configuration fields when making your edits.

### Hash Calculation (Manual)

Calculate the SHA-256 digest of the intended Support RIM:

=== "Linux"
    ```bash
    sha256sum /path/to/support.rimel
    ```

Copy the hexadecimal digest from the first output column into `hash`, replacing the placeholder below:

```json
{
  "Payload": {
    "Directory": {
      "File": [
        {
          "name": "support.rimel",
          "hash": "<SHA-256 digest>"
        }
      ]
    }
  }
}
```

### Hash Calculation (Automatic)

Remove the `hash` field from the file entry:

```json
{
  "Payload": {
    "Directory": {
      "File": [
        {
          "name": "support.rimel"
        }
      ]
    }
  }
}
```

When running [`create`](../reference/create.md), supply the intended Support RIM with `-l /path/to/support.rimel`. The tool calculates its hash when the field is absent. The `-l` argument is required for PC Client RIM creation even when you supply the hash yourself.

!!! note "Existing Hashes"

    A populated `hash` is kept as supplied; `-l` does not overwrite it. Recalculate or remove the field whenever you change the Support RIM. For multiple distinct Support RIM files, provide each file's digest explicitly: automatic calculation uses the same `-l` input for every entry without a hash.

After creation, use [`verify` with `-l <directory>`](../reference/verify.md#-l-option-pc-client-rim-only) to check the referenced Support RIM's hash as well as the Base RIM signature.
