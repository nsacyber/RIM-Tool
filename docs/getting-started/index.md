---
title: Getting Started
---

# Getting Started

This tutorial walks you through creating a test PC Client RIM bundle for a Linux device using the `quick_pcrim.sh` helper. The bundle can also be used with HIRS for testing firmware validation.

The script captures a TPM event log, fills in the device's manufacturer and model, generates test signing certificates, and creates and verifies the bundle.

!!! tip "Need to install the tool?"

    Follow [Setup](setup.md#installation) to install the Linux package, then return here to create your first bundle.

## Before Starting

- Have the `RIM-Tool` Linux package installed.
- Have Bash, OpenSSL, `jq`, and `uuidgen` available.
- Run on a Linux device with readable manufacturer/model information under `/sys/devices/virtual/dmi/id/`.
- Have a readable TPM event log at `/sys/kernel/security/tpm0/binary_bios_measurements`, or supply an existing event log (see below).

The script requires root privileges to read device information and write its output under `/opt/rimtool/data/local/`.

## Create the Test Bundle

Run the following to create the test bundle:

=== "Linux"
    ```bash
    sudo bash /opt/rimtool/scripts/linux/quick_pcrim.sh
    ```

!!! info

    If your installed package predates these instructions, use the updated helper from this repository's root instead: `sudo bash scripts/linux/quick_pcrim.sh`. The tool and sample data must still be installed.

On success, verification reports a confirmed Support RIM hash and finishes with:

```text
Overall base RIM validation passed.
Signature verified :-)
```

### Use an Existing Event Log

To optionally use an existing event log instead of capturing the current device's log, supply its absolute path. Keep this input outside `/opt/rimtool/data/local/`, where the helper replaces its previous output:

=== "Linux"
    ```bash
    sudo bash /opt/rimtool/scripts/linux/quick_pcrim.sh -l /absolute/path/to/event-log.bin
    ```

The script still uses the current device's manufacturer and model.  Use `--help` for options or `-v` for more output.

## Results

The script prints the generated filenames. Its output directory, `/opt/rimtool/data/local/`, contains:

| File | Purpose |
|---|---|
| `<manufacturer>.<model>.1.swidtag` | Signed Base RIM describing the reference data. |
| `<manufacturer>.<model>.1.rimel` | Support RIM containing the copied event log. |
| `rim_fields.json` | Configuration populated for the device. |
| `RIM_Test_RSA_Root.pem` | Test root CA certificate. |
| `RIM_Test_Signer.pem` | Test signer certificate embedded in the Base RIM. |

!!! warning "Running Again"

    The directory also contains private keys used to create the test certificates. Repeating the command replaces the bundle and configuration while reusing existing certificates and keys; save any earlier results you need first.

## HIRS Support

You can use the Base RIM, Support RIM, and test certificate chain with [HIRS :fontawesome-solid-external-link:](https://github.com/nsacyber/HIRS) firmware validation. Follow the HIRS documentation's [Getting Started :fontawesome-solid-external-link:](https://nsacyber.github.io/HIRS/started/) instructions to configure validation and provide the files. For support across HIRS versions and RIM formats, see [Compatibility](../about.md#compatibility).

This bundle records the selected event log as a test baseline. Successful bundle verification checks its signature, certificate chain, and referenced file hash. Note that this remains a test, which does not establish device trustworthiness.

To customize the bundle or use another format, choose a [RIM format](../RIMs/index.md) and prepare its [configuration file](../RIMs/configuration-files.md). See the [command reference](../reference/index.md) for more options and examples.
