---
title: Getting Started
---

# Getting Started

To get started using the `RIM-Tool`, please follow the below instructions. The tool can be either installed from a package or built from source.

## Installation

!!! info

    Currently, installation from a package is only supported on Linux.

Packages for this tool can be found on the [GitHub release page :fontawesome-solid-external-link:](https://github.com/nsacyber/rim_tool/release). 

Only `.rpm` (Red Hat Enterpise Linux 9, Rocky Linux 9) and `.deb` (Debian, Ubuntu) packages are currently supported.

### Linux

To install from a package, download from the above link, then run the following *(replace the asterisk with the full name of the downloaded package)*:

=== "RHEL 9/Rocky 9"
    ``` shell
    sudo dnf install rim_tool*.rpm
    ```
=== "Debian/Ubuntu"
    ``` shell
    sudo apt-get install rim_tool*.deb 
    ```

After installation, the package will create a command-line shortcut. This can be invoked using the `rim` command. See [Commands](commands/index.md) for further details.

## Building
The `RIM-Tool` is written using Java, and requires JDK 17 to be installed.

The following sections also assume the `RIM-Tool` [source code :fontawesome-solid-external-link:](https://github.com/nsacyber/rim-tool) has been cloned (using Git) to a local directory.

Upon successful build, a `rim_tool-X.X.jar` file will be placed in either the `/build/libs/tools/` (Linux) or `\build\libs\tools\` (Windows) folders. This can 
then be run using:
``` shell
java -jar <build directory>/rim_tool-X.X.jar
```

### Linux
To build this tool, navigate to the source directory and use the following command:
``` shell
./gradlew clean build
```

### Windows
Several options exist for building on Windows 11. In either case, first navigate to the source directory, then run the following:
=== "Command Prompt"
    ``` shell
    gradlew.bat clean build
    ```
=== "PowerShell"
    ``` powershell
    ./gradlew clean build
    ```
!!! note
    The PowerShell option requires Windows Subsystem for Linux (WSL) to be enabled.

## Packaging
To create a Linux installation package for the `RIM-Tool`, run the following command in the source directory:
=== "RHEL 9/Rocky 9"
    ``` shell
    ./gradlew buildRpm
    ```
=== "Debian/Ubuntu"
    ``` shell
    ./gradlew buildDeb
    ```
The output package can be found under the `/build/distributions/` folder.