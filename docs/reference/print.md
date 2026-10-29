---
title: Print
---

# `print` Command

Print the contents of a RIM, CoRIM, or CoSWID tag to the command line.

For a signed COSE object, use `-r cose` to inspect its signature envelope. To inspect a CoSWID payload, first extract it with [`get`](get.md), then use `-r coswid`. The `corim_comid` print mode also displays the COSE envelope.

## Usage

```shell
rim print -r <string> -i <file>
```

## Parameters

| Parameter                    | Description                                 | Required | Type   |
|------------------------------|---------------------------------------------|----------|--------|
| `-r`, `--rim-type`           | Specifies the [RIM type](../RIMs/index.md). | Yes      | String |
| `-i`, `--in`                 | Specifies the input file to print.          | Yes      | File   |

## Examples

!!! example "Before Starting"
    Before running these examples, please ensure that you change into the `data` directory (requires [installation](../getting-started/setup.md#installation)):
    === "Linux"
        ```shell
        cd /opt/rimtool/data
        ```

Print a [TCG PC Client Base RIM](../RIMs/tcg-pc-client-rim.md):

=== "Linux"
    ```shell
    rim print -r pcrim --in pcrim/laptop.default.1.swidtag
    ```

Print a [CoRIM](../RIMs/corim.md) containing a CoMID:

=== "Linux"
    ```shell
    rim print -r corim_comid --in corim/corim_1.signed.corim.cose
    ```
