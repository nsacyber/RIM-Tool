---
title: Reference
---

# Reference

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
Use `-h` or `--help` for information about available commands:
```shell
rim -h
```

For a specific command's options, place help after the command:

```shell
rim create --help
```

### Version

Use `--version` to print the tool's version:

```shell
rim --version
```

The version is recorded from the repository's `VERSION` file when the JAR is built.
