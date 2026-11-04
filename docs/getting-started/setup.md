---
title: Setup
---

# Setup

Install `RIM-Tool` from a Linux package or build it from source using the instructions below.

## Installation

!!! info

    Currently, installation from a package is only supported on Linux.

Packages for this tool can be found on the [GitHub release page :fontawesome-solid-external-link:](https://github.com/nsacyber/rim-tool/releases). 

Only x86-64 Linux packages in `.rpm` (Red Hat Enterprise Linux 9, Rocky Linux 9) and `.deb` (Debian, Ubuntu) formats are currently supported.

Install the Java runtime required by your release and ensure `java` is available on `PATH`. Packages built from the current source require Java 25; check the release requirements when installing an older package.

### Linux

To install from a package, download from the above link, then run the following *(replace the asterisk with the full name of the downloaded package)*:

=== "RHEL 9/Rocky 9"
    ``` shell
    sudo dnf install ./rim-tool*.rpm
    ```
=== "Debian/Ubuntu"
    ``` shell
    sudo apt-get install ./rim-tool*.deb
    ```

After installation, the package will create a command-line shortcut. This can be invoked using the `rim` command. See the [command reference](../reference/index.md) for further details.

Return to [Getting Started](index.md#before-starting) to create a test PC Client RIM bundle using the Linux helper script.

## Building
The `RIM-Tool` is written using Java, and requires JDK 25 to be installed.

The following sections also assume the `RIM-Tool` [source code :fontawesome-solid-external-link:](https://github.com/nsacyber/rim-tool) has been cloned (using Git) to a local directory.

!!! note
    Please clone using `git clone --recursive` to initialize any submodules.

Upon successful build, a `rim-tool-X.X.jar` file will be placed in `build/libs/` (`build\libs\` on Windows), relative to the repository root. Run it from the repository root using:
``` shell
java -jar build/libs/rim-tool-X.X.jar -h
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
    .\gradlew.bat clean build
    ```
!!! note
    When building inside Windows Subsystem for Linux (WSL), follow the Linux instructions.

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
The output package can be found under the `build/distributions/` folder relative to the repository root.
