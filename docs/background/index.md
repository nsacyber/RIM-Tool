---
title: Background
---

# Background

Firmware validation complements the hardware validation for Supply Chain acceptance testing by providing an
automated means to verify the firmware and boot software for the platform and its components before an 
Attestation Certificate will be issued. While the hardware validation uses a Platform Certificate, 
the firmware validation uses a RIM.

A RIM for a PC or PC component is created by the manufacturer during the manufacturing process. The RIM 
serves as a manifest of expected firmware values. When a device is delivered to a customer, the customer 
can use a Verifier (a system that analyzes evidence from a platform or platform component to determine 
its state) to compare measured values to the expected values. By comparing expected and measured firmware 
values, the Verifier can detect differences in measured firmware relative to trusted reference values.

The project [Host Integrity at Runtime and Startup :fontawesome-solid-external-link:](https://github.com/nsacyber/hirs) 
(HIRS) contains a server-side Verifier application referred to as the Attestation Certificate Authority (ACA). 
HIRS also contains a client-side Provisioner, which captures the measured firmware values and other 
necessary artifacts. Refer to the [HIRS project's documentation :fontawesome-solid-external-link:](https://github.com/nsacyber/HIRS)
for setup, artifact requirements, and instructions for running the ACA and Provisioner.
