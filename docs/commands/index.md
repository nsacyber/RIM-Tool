---
title: Commands
---

# Commands
The `RIM-Tool` provides the following main commands.

| Command               | Description                                                                            |
|-----------------------|----------------------------------------------------------------------------------------|
| [`create`](create.md) | :magic_wand: Creates a RIM based on provided options.                                  |
| [`verify`](verify.md) | :material-check: Verifies the signature of a RIM.                                      |
| [`print`](print.md)   | :material-printer: Provides a human-readable representation of a supported RIM object. |
| [`sign`](sign.md)     | :fontawesome-solid-file-signature: Signs a file using specific format options.         |
| [`get`](get.md)       | :material-download: Retrieves the payload from a signed object and saves it to a file. |

Each command is paired with one or more required (or optional) parameters. Please visit each command's respective page 
for more information on usage and examples.

!!! note

    Clustering of parameters is not currently supported.

## Special Parameters

### Help
At any time, a help parameter (`-h`) can be used for information about available commands:
```shell
rim -h
```
