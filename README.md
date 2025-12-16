# RIM Tool 
A Reference Integrity Manifest (RIM) contains structures that a Verifier uses to validate expected values (Assertions) against actual values (Evidence).  The RIM Tool provides a capability to Create, Print, and Verify RIM files that target integrity of firmware. 

Functionality provided by this tool includes the following:
* Creates, formats, and digitally signs [TCG PC Client Base RIMs](https://trustedcomputinggroup.org/resource/tcg-pc-client-reference-integrity-manifest-specification/) 
* Creates, formats, and digitally signs [TCG Component RIMs](https://trustedcomputinggroup.org/wp-content/uploads/TCG-Component-RIM-Binding-for-SWID-and-CoSWID-Version-1.0-RC-2_16April25.pdf) (both SWID and CoSWID variants) 
* Creates, formats, and digitally signs [IETF CoRIMs](https://datatracker.ietf.org/doc/draft-ietf-rats-corim/) 
* Validates the digital signature of RIMs using X.509 public key certificates 
* Prints human readable contents of RIMs

Please see the [documentation](https://nsacyber.github.io/RIM-Tool/) for detailed installation and usage.

## Building

In order to build from source, please first clone the project using `git clone --recursive`. This will properly initialize any submodules.

### Linux 
To build this tool, navigate to the `RIM-Tool` directory cloned earlier and use the following command: 
```
./gradlew clean build
```
The `rim-tool-X.X.jar` file should have been placed in the `/build/libs/tools/` (Linux) folder. 
### Windows
Several options exist for building on Windows 11.

*Windows Command Prompt (CMD.exe):* Navigate to the `RIM-Tool` folder and run the Windows Gradle wrapper: 
~~~
gradlew.bat clean build 
~~~
*Windows PowerShell with Windows Subsystem for Linux (WSL) enabled:* Navigate to the `RIM-Tool` folder and run the Linux Gradle wrapper: 
~~~
./gradlew clean build 
~~~

In both cases, the `rim-tool-X.X.jar` file should have been placed in the `\build\libs\tools\` folder.  

## Packaging 

Packages for this tool can be found on the [releases page](https://github.com/nsacyber/RIM-Tool/releases).
Currently, only packaging for Linux is supported. 

To create an RPM package on a RHEL or Rocky Linux device, use the following command in the same directory: 
~~~
./gradlew buildRpm 
~~~
or for a Debian or Ubuntu Linux distro: 
~~~
./gradlew buildDeb 
~~~
The RIM Tool package can be found under the `/build/distributions/` folder.

## Installing
Currently, only install packages for Linux are supported. 

To install this tool on a RHEL or Rocky Linux distro, use the following command from the same directory: 
~~~
sudo dnf install build/distributions/rim-tool*.rpm 
~~~
or for a Debian or Ubuntu Linux distro: 
~~~
sudo apt-get install build/distributions/rim-tool*.deb 
~~~
## Usage 
The RIM Tool RPM will create a `rim` command line shortcut. This can be invoked from a command line: 
~~~
rim -h 
~~~
The RIM Tool also can be invoked using Java from a build directory (if following the [Building](#building) section earlier):
~~~
java -jar build/libs/tools/rim-tool-X.X.jar -h 
~~~
Current options for the tool can be found using the `-h` option. 

### Commands
`rim <command> <options>`

The RIM Tool provides the following commands and options. *Note that clustering of options is not currently supported.*

* **create**: Creates a RIM based upon provided options  
* **verify**: Verifies the signature of a RIM. 
* **sign**: Signs a file using specific format option 
* **print**: Provide a human readable representation of a supported RIM object 
* **get**: Retrieves the payload from a signed object and saves it to a file

For details on RIM Tool commands, please refer to the [documentation](https://nsacyber.github.io/RIM-Tool/commands/).

## Quick Links 

* [Host Integrity at Runtime and Startup (HIRS)](https://github.com/nsacyber/HIRS) uses RIMs for firmware validation. Compatible with PC Client RIMs created by this tool. 
* [Trusted Computing Group PC Client RIM specification](https://trustedcomputinggroup.org/resource/tcg-pc-client-reference-integrity-manifest-specification/)
* [CoSWID Specification (RFC 9393)](https://www.rfc-editor.org/rfc/rfc9393.html)
* [IETF CoRIM Specification (draft-rats-ietf-corim)](https://datatracker.ietf.org/doc/draft-ietf-rats-corim/08/)
* [CBOR Playground (useful for viewing/parsing/debugging CBOR data) ](https://cbor.nemo157.com/)
* [COSE Working Group test patterns that validate with this tool (COSE_Sign1 only)](https://github.com/cose-wg/Examples/tree/master/sign1-tests)
* [Gluecose test patterns that validate with this tool (COSE_Sign1 only)](https://github.com/cose-wg/Examples/tree/master/sign1-tests)
* [Microsoft's COSE Tool](https://github.com/microsoft/CoseSignTool/blob/main/docs/CoseSignTool.md) (useful for testing detached signatures and embedded certificates) 
* [EDK2's Trusted Boot Chain Overview](https://tianocore-docs.github.io/edk2-TrustedBootChain/release-1.00/2_Overview.html)
