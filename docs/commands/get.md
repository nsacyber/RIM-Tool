---
title: Get
---

# `get` Command

Retrieves the payload from a signed object and saves the contents to a file.

!!! Note

    `get` is only compatible with signed COSE objects at present. See [RIM Types](../RIMs/index.md) for details.

## Usage

```shell
rim get -rt <string> -i <file> -o <file>
```

## Parameters

| Parameter           | Description                                         | Required | Type   |
|---------------------|-----------------------------------------------------|----------|--------|
| `-rt`, `--rim-type` | Specifies the [RIM type](../RIMs/index.md).         | Yes      | String |
| `-i`, `--in`        | The signed input file to retrieve the payload from. | Yes      | File   |
| `-o`, `--out`       | The output file to write the payload to.            | Yes      | File   |

## Examples

Retrieve a payload from a signed [CoRIM](../RIMs/corim.md):

```shell
get -rt corim_comid --in corim_1.corim.cose --out corim_1_payload.cose
```

Retrieve a payload from a signed [CoSWID tag](../RIMs/coswid.md):

```shell
get -rt corim_comid --in test1.coswid.cose --out test1_coswid_payload.cose
```