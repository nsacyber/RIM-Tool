---
title: Getting Started
---

# Getting Started

This tutorial walks you through creating a test PC Client RIM bundle for a Linux device using the `quick_pcrim.sh` helper. The bundle can also be used with HIRS for testing firmware validation.

The script captures a TPM event log, fills in the device's manufacturer and model, generates test signing certificates, and creates and verifies the bundle.

## Before Starting

- [Install the RIM-Tool Linux package](setup.md#installation).
- Have Bash and OpenSSL available.
- Run on a Linux device with readable manufacturer/model information.

The script requires root privileges to read device information and write its output under `/opt/rimtool/data/local/`.

## Create the Test Bundle

Run the following to create the test bundle:

=== "Linux"
    ```bash
    sudo bash /opt/rimtool/scripts/linux/quick_pcrim.sh
    ```

!!! info

    If your installed package predates these instructions, use the updated helper from this repository's root instead: `sudo bash scripts/linux/quick_pcrim.sh`. The tool and sample data must still be installed.

To use an existing event log instead of capturing the current device's log, supply its absolute path:

=== "Linux"
    ```bash
    sudo bash /opt/rimtool/scripts/linux/quick_pcrim.sh -l /absolute/path/to/event-log.bin
    ```

The script still uses the current device's manufacturer and model. Use `--help` for options or `-v` for more output.

On success, verification reports a confirmed Support RIM hash and finishes with:

```text
Overall base RIM validation passed.
Signature verified :-)
```

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

You can use the Base RIM, Support RIM, and test certificate chain with [HIRS :fontawesome-solid-external-link:](https://github.com/nsacyber/HIRS) firmware validation. Follow the HIRS documentation's [Getting Started :fontawesome-solid-external-link:](https://nsacyber.github.io/HIRS/started/) instructions to configure validation and provide the files.

This bundle records the selected event log as a test baseline. Successful bundle verification checks its signature, certificate chain, and referenced file hash. Note that this remains a test, which does not establish device trustworthiness.

For custom metadata or other RIM formats, see [configuration files](configuration-files.md) and the [`create` command](commands/create.md).
