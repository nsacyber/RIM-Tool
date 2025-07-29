# RIM Tool
A Reference Integrity Manifest (RIM) contains structures that a Verifier uses to validate expected values (Assertions) against actual values (Evidence).  The RIM Tool provides a capability to Create, Print, and Verify RIM files that target integrity of firmware.  Functionality provided by this tool includes:
* Creates, formats, and digitally signs [TCG PC Client Base RIMs](https://trustedcomputinggroup.org/resource/tcg-pc-client-reference-integrity-manifest-specification/) 
* Creates, formats, and digitally signs [TCG Component RIMs](https://trustedcomputinggroup.org/wp-content/uploads/TCG-Component-RIM-Binding-for-SWID-and-CoSWID-Version-1.0-RC-2_16April25.pdf) (both SWID and CoSWID variants) 
* Creates, formats, and digitally signs [IETF CoRIMs](https://datatracker.ietf.org/doc/draft-ietf-rats-corim/) 
* Validates the digital signature of RIMs using X.509 public key certificates 
* Prints human readable contents of RIMs 

There are various standards that describe different formats of RIMs that may be geared toward a particular technology or market. RIMs produced by this tool are compatible with the Host Integrity at Runtime and Startup (HIRS) project. Currently, HIRS only processes TCG-defined PC Client RIMs, but future versions should be able to process all RIMs produced by this tool. 

Supported RIMs: 
* TCG PC Client RIM (pcrim) 
* TCG Component RIM SWID Option  (comp_swid) 
* TCG Component RIM CoSWID Option (comp_coswid) 
* Concise Reference Integrity Manifest (CoRIM) CoMID option (corim_comid) 
* Concise Reference Integrity Manifest (CoRIM) CoSWID option (corim_coswid) 

# Building 

## Linux 
To build this tool navigate to the rim_tool directory and use the following command: 
```
./gradlew clean build
```
The rim_tool-X.X.jar file should have been placed in the build/libs/tools/ (Linux) folder. 
## Windows
Several options exist for building on Windows 11: 
Windows command shell (CMD.exe): 
Navigate to the rim_tool folder and run the windows gradle wrapper: 
~~~
gradlew.bat clean build 
~~~
Windows PowerShell with Windows Subsystem for Linux (WSL) enabled: 
Navigate to the rim_tool folder and run the Linux gradle wrapper: 
~~~
./gradlew clean build 
~~~
In both cases the rim_tool-X.X.jar file should have been placed in the build\libs\tools\  folder.  

## Packaging 

Packages for this tool can be found on the [releases page](https://github.com/nsacyber/RIM-Tool/releases).
Currently only a packaging for Linux is supported. 

To create an RPM package on a RHEL or Rocky Linux device, use the following command in the same directory: 
~~~
./gradlew buildRpm 
~~~
or for a Debian or Ubuntu Linux distro: 
~~~
./gradlew buildDeb 
~~~
The RIM Tool package can be found under the build/distributions/ folder.

## Installing
Currently, only install packages for Linux are supported. 
To install this tool on a RHEL or Rocky Linux distro use the following command from the same directory: 
~~~
sudo dnf install build/distributions/rim_tool*.rpm 
~~~
or for a Debian or Ubuntu Linux distro: 
~~~
sudo apt-get install build/distributions/rim_tool*.deb 
~~~
## Usage 
The rim_tool rpm will create a rim command line shortcut. This can be invoked from a command line: 
~~~
rim -h 
~~~
The rim_tool also can be invoked using java from the tcg_eventlog_tool directory: 
~~~
java -jar build/libs/tools/tcg_rim_tool-1.0.jar -h 
~~~
Current options for the tool can be found using the -h option. 

### Commands
rim <command> <options> 
The RIM Tool provides the following commands and options. Note Clustering of options is not currently supported: 

* **create**: Creates a RIM based upon provided options  
* **verify**: Verifies the signature of a RIM. 
* **sign**: Signs a file using specific format option 
* **print**: Provide a human readable representation of a supported RIM object 
* **get**: Retrieves the payload from a signed object and saves it to a file 

For Details on the rim-tool commands please refer to the commands wiki page.

# Quick Links 

* [Host Integrity at Runtime and Startup (HIRS)](https://github.com/nsacyber/HIRS) uses RIMs for firmware validation. Compatible with PC Client RIMs created by this tool. 
* [Trusted Computing Group PC Client RIM specification](https://trustedcomputinggroup.org/resource/tcg-pc-client-reference-integrity-manifest-specification/)
* [CoSWID Specification (RFC 9393)](https://www.rfc-editor.org/rfc/rfc9393.html)
* [IETF CoRIM Specification (draft-rats-ietf-corim)](https://datatracker.ietf.org/doc/draft-ietf-rats-corim/08/)
* [CBOR Playground (useful for viewing/parsing/debugging CBOR data) ](https://cbor.nemo157.com/)
* [COSE Working Group test patterns that validate with this tool (COSE_Sign1 only)](https://github.com/cose-wg/Examples/tree/master/sign1-tests)
* [Gluecose test patterns that validate with this tool (COSE_Sign1 only)](https://github.com/cose-wg/Examples/tree/master/sign1-tests)
* [Microsoft's COSE Tool](https://github.com/microsoft/CoseSignTool/blob/main/docs/CoseSignTool.md) (useful for testing detached signatures and embedded certificates) 
* [EDK2’s Trusted Boot Chain Overview](https://tianocore-docs.github.io/edk2-TrustedBootChain/release-1.00/2_Overview.html)
