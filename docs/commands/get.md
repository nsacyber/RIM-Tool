---
title: Get
---

# `get` Command

Retrieves the payload from a signed object and saves the contents to a file.

!!! Note

    `get` is only compatible with signed COSE objects at present. See [RIM Types](../RIMs/index.md) for details.

## Usage

```shell
rim get -r <string> -i <file> -o <file>
```

## Parameters

| Parameter           | Description                                         | Required | Type   |
|---------------------|-----------------------------------------------------|----------|--------|
| `-r`, `--rim-type`  | Specifies the [RIM type](../RIMs/index.md).         | Yes      | String |
| `-i`, `--in`        | The signed input file to retrieve the payload from. | Yes      | File   |
| `-o`, `--out`       | The output file to write the payload to.            | Yes      | File   |

## Examples

!!! example "Before Starting"
    Before running these examples, please ensure that you change into the `data` directory (requires [install](../getting-started.md/#installation)):
    === "Linux"
        ```shell
        cd /opt/rimtool/data
        ```

Retrieve a payload from a signed [CoRIM](../RIMs/corim.md):

=== "Linux"
    ```shell
    rim get -r corim_comid --in corim/corim_1.signed.corim.cose --out /tmp/corim_1.corim.cose
    ```

Retrieve a payload from a signed [CoSWID tag](../RIMs/coswid.md):

=== "Linux"
    ```shell
    rim get -r coswid --in coswid/coswid_rim_1.signed.coswid.cose --out /tmp/coswid_rim_1.coswid
    ```