---
title: Print
---

# `print` Command

Print the contents of a RIM, CoRIM, or CoSWID tag to the command line.

## Usage

```shell
rim print -rt <string> -i <file>
```

## Parameters

| Parameter                    | Description                                 | Required | Type   |
|------------------------------|---------------------------------------------|----------|--------|
| `-rt`, `--rim-type`          | Specifies the [RIM type](../RIMs/index.md). | Yes      | String |
| `-i`, `--in`                 | Specifies the input file to print.          | Yes      | File   |

## Examples

Print a [TCG PC Client Base RIM](../RIMs/tcg-pc-client-rim.md):

```shell
print -rt pcrim --in test.1.swidtag
```

Print a [CoRIM](../RIMs/corim.md) containing a CoMID:

```shell
print -rt corim_comid --in corim_1.corim.cose
```