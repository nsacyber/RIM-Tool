package rimtool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hirs.utils.signature.cose.CoseAlgorithm;
import hirs.utils.rim.unsignedRim.GenericRim;
import hirs.utils.rim.unsignedRim.cbor.ietfCorim.CoRimBuilder;
import hirs.utils.rim.unsignedRim.xml.pcclientrim.PcClientRim;
import hirs.utils.rim.unsignedRim.xml.tcgCompRimSwid.TcgComponentRimSwid;
import hirs.utils.rim.unsignedRim.cbor.ietfCoswid.CoswidBuilder;
import hirs.utils.rim.unsignedRim.cbor.ietfCoswid.CoswidConfig;
import com.beust.jcommander.JCommander;
import hirs.utils.rim.unsignedRim.cbor.ietfCoswid.CoswidParser;
import hirs.utils.rim.unsignedRim.cbor.tcgCompRimCoswid.TcgCompRimCoswidBuilder;
import hirs.utils.rim.unsignedRim.cbor.tcgCompRimCoswid.TcgCompRimCoswidConfig;
import hirs.utils.rim.unsignedRim.cbor.tcgCompRimCoswid.TcgCompRimCoswidParser;
import hirs.utils.signature.cose.CoseParser;
import hirs.utils.signature.cose.CoseSignature;
import hirs.utils.crypto.DefaultCrypto;
import hirs.utils.crypto.CryptoEngine;
import rimtool.commands.CommandCreate;
import rimtool.commands.CommandDefinitions;
import rimtool.commands.CommandGet;
import rimtool.commands.CommandMain;
import rimtool.commands.CommandPrint;
import rimtool.commands.CommandSign;
import rimtool.commands.CommandVerify;
import hirs.utils.HexUtils;

/**
 * Command-line application for processing TCG Event Logs.
 * Input arg: path to *.tcglp file
 */
final class Main {
    /**
     * Main Constructor for the RIM Tool command line application.
     * intializes the command line processor
     * @param args command line parameters.
     */
    public static void main(final String[] args) {

        CommandMain mainCom = new CommandMain();
        CommandCreate createCom = new CommandCreate();
        CommandSign signCom = new CommandSign();
        CommandVerify verifyCom = new CommandVerify();
        CommandPrint printCom = new CommandPrint();
        CommandGet getCom = new CommandGet();
        JCommander jc = JCommander.newBuilder()
                .addObject(mainCom)
                .addCommand(CommandDefinitions.CMD_CREATE, createCom)
                .addCommand(CommandDefinitions.CMD_SIGN, signCom)
                .addCommand(CommandDefinitions.CMD_VERIFY, verifyCom)
                .addCommand(CommandDefinitions.CMD_PRINT, printCom)
                .addCommand(CommandDefinitions.CMD_GET, getCom)
                .build();
        try {
            jc.parse(args);
        } catch (Exception e) {
            System.out.println("Error processing RIM command:");
            if (!processPoorqualityErrorMsg(e.getMessage())) {
                System.out.println(e.getMessage() + "; See usage:\n");
            }
            System.out.println(mainCom.printHelp());
            System.exit(1);
        }

        String starMsg = "\nAsterisk (*) represents required input:";
        // Process help for each command
        if (mainCom.isHelp()) {
            System.out.println(mainCom.printHelp()); return;
        }
        if (createCom.isHelp()) {
            jc.usage(CommandDefinitions.CMD_CREATE); return;
        }
        if (signCom.isHelp()) {
            jc.usage(CommandDefinitions.CMD_SIGN); return;
        }
        if (verifyCom.isHelp()) {
            jc.usage(CommandDefinitions.CMD_VERIFY); return;
        }
        if (printCom.isHelp()) {
            jc.usage(CommandDefinitions.CMD_PRINT); return;
        }
        if (getCom.isHelp()) {
            jc.usage(CommandDefinitions.CMD_GET); return;
        }

        if (mainCom.isVersion()) {
            parseVersionFromJar();
        } else if (mainCom.isVerbose()) {
            System.out.println("Rimtool in verbose mode.");
        }

        if (jc.getParsedCommand() == null) {
            System.out.println("Invalid input: Possibly did not include a command. See usage:\n");
            System.out.println(mainCom.printHelp());
            System.exit(1);
        }
        switch (jc.getParsedCommand()) {
            case CommandDefinitions.CMD_CREATE:
                create(createCom.getRimType(), createCom.getConfigFile(), createCom.getRimEventLog(),
                        createCom.getPrivateKey(), createCom.getPublicCertificate(), createCom.getAlgorithm(),
                        createCom.getDetachedSignature(), createCom.isEmbedded(),
                        createCom.getProtectedKid(), createCom.getUnProtectedKid(),
                        createCom.getOutFile(), createCom.isUnsigned());
                break;
            case CommandDefinitions.CMD_SIGN:
                boolean savePayload = true;
                if (signCom.getDetachedSignature().isEmpty()) {
                    savePayload = false;
                }
                sign(signCom.getRimType(), signCom.getInFile(), signCom.getPrivateKey(),
                        signCom.getPublicCertificate(), signCom.getAlgorithm(),
                        signCom.getDetachedSignature(), signCom.isEmbedded(),
                        signCom.getProtectedKid(), signCom.getUnProtectedKid(),
                        signCom.getOutFile(), savePayload);
                break;
            case CommandDefinitions.CMD_VERIFY:
                verify(verifyCom.getRimType(), verifyCom.getInFile(), verifyCom.getRimEventLog(),
                        verifyCom.getPublicKey(), verifyCom.getPublicCertificate(), verifyCom.getTruststore(),
                        verifyCom.getDetachedSignature(), verifyCom.isEmbedded());
                break;
            case CommandDefinitions.CMD_PRINT:
                print(printCom.getRimType(), printCom.getInFile());
                break;
            case CommandDefinitions.CMD_GET:
                get(getCom.getRimType(), getCom.getInFile(), getCom.isUnsigned(), getCom.getOutFile());
                break;
            default:
                System.out.println(" Error: No command given.");
        }
    }

    /**
     * Checks for specific poor-quality error messages and prints a new more applicable message.
     * @param jcommanderErrorMsg the error message
     * @return true if a poor-quality error message was encountered
     */
    private static boolean processPoorqualityErrorMsg(final String jcommanderErrorMsg) {

        String commandWithNoOptions = "Cannot invoke \"Object.toString()\" because the return value of "
                + "\"java.util.Map.get(Object)\" is null";
        String poorqualityCatchall = "Cannot invoke";
        if (jcommanderErrorMsg.contains(commandWithNoOptions)) {
            System.out.println("Invalid input: Possibly did not include required options. See usage:\n");
            return true;
        } else if (jcommanderErrorMsg.contains(poorqualityCatchall)) {
            System.out.println("Invalid input: See documentation on usage for valid input options. "
                    + "See usage:\n");
            return true;
        }
        return false;
    }

    /**
     * Retrieves the payload from a RIM Object and puts it in a separate file.
     * @param rimType
     * @param inFile
     * @param isUnsigned
     * @param outFile
     */
    private static void get(final String rimType, final String inFile, final boolean isUnsigned,
                            final String outFile) {
        String sigType = "";
        Path path = Paths.get(inFile);
        Path outFilePath = Paths.get(outFile);
        byte[] data = null;
        byte[] payload = null;
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            System.out.println("Error reading file " + inFile + " : " + e.getMessage());
            System.exit(1);
        }
        try {
            if (isUnsigned) {
                System.out.println("Get payload for an unsigned " + rimType + " is not currently supported");
                System.exit(1);
            } else { // signed
                sigType = GenericRim.getSigType(rimType);
                if (sigType.compareTo(GenericRim.SIGTYPE_COSE) == 0) {
                    payload = new CoseParser(data).getPayload();
                } else { // XML DSIG
                    System.out.println("Get payload for a signed " + rimType + " is not currently supported");
                    System.exit(1);
                }
            }
            Files.write(outFilePath, payload);
        } catch (IOException e) {
            System.out.println("Error writing file " + outFile + " : " + e.getMessage());
            System.exit(1);
        } catch (RuntimeException e) {
            System.out.println("Error parsing payload from " + inFile + " : " + e.getMessage());
            System.exit(1);
        }
        System.out.println("Get payload for  " + inFile + " has been successfully exported to " + outFile);
    }
    /**
     * Prints a human read-able form of the RIM file.
     * @param rimType RIM type (e.g. ietf_coswid, tcg_pcRim, etc.)
     * @param inFile path + name of the RIM file to print
     */
    private static void print(final String rimType, final String inFile) {

        Path path = Paths.get(inFile);
        String print = "RIM Contents (" + rimType + ") of file " + inFile + ": \n";
        byte[]  data = null;
        try {
            data = Files.readAllBytes(path);

            switch (rimType) {
                //signature types
                case GenericRim.SIGTYPE_COSE:
                    print += new CoseParser(data).toString("pretty"); break;
                //RIM types
                case GenericRim.RIMTYPE_PCRIM:
                    print = new String(data, StandardCharsets.UTF_8); break;
                case GenericRim.RIMTYPE_COSWID:
                    print += new CoswidParser(data).toString("pretty"); break;
                case GenericRim.RIMTYPE_COMP_SWID:
                    print +=  new String(data, StandardCharsets.UTF_8); break;
                case GenericRim.RIMTYPE_COMP_COSWID:
                    print += new TcgCompRimCoswidParser(data).toString("pretty"); break;
                case GenericRim.RIMTYPE_CORIM_COMID:
                    print += new CoseParser(data).toString("pretty"); break;

                default:
                    System.out.println("Print for " + rimType
                            + " rim type is not currently supported, exiting");
                    System.exit(1);
            }
        } catch (IOException e) {
            System.out.println("Error Reading file: " +  inFile + ". Error = " + e.getMessage());
            System.exit(1);
        }
        System.out.println(print);
    }

    /**
     * Creates a RIM file using user supplied parameters.
     * @param rimType         type of RIM to create
     * @param configFile      type json formatted file from which the RIM will draw parameters from
     * @param eventLog        path to the TPM event log file
     * @param keyFile         key used to sign the rim
     * @param certPath        reference to an x.509 certificate file used to determine algorithm to sign with
     * @param algorithm       algorithm to use if no certificate is supplied
     * @param detachedSignatureFile detached file (for swid/xml RIMs it's a detached signature,
     *                              for cose it's a detached payload)
     * @param isEmbedded      true if embedded x.509 signing certificate in COSE protected header
     * @param protectedKid    kid in protected header
     * @param unProtectedKid  kid in unprotected header
     * @param outFile         file to write the rim to
     * @param isUnsigned      true if user wants to create unsigned RIM
     */
    private static void create(final String rimType, final String configFile, final String eventLog,
                               final String keyFile, final String certPath, final String algorithm,
                               final String detachedSignatureFile, final boolean isEmbedded,
                               final String protectedKid, final String unProtectedKid,
                               final String outFile, final boolean isUnsigned) {
        try {
            if (!isUnsigned) {  // Signed RIMs (the default)
                switch (rimType) {
                    case GenericRim.RIMTYPE_PCRIM:
                        PcClientRim pcRim = new PcClientRim();
                        pcRim.create(configFile, eventLog, certPath, keyFile, false, outFile);
                        break;
                    case GenericRim.RIMTYPE_COSWID:
                        CoswidConfig sconf = new CoswidConfig(configFile);
                        CoswidBuilder scBuild = new CoswidBuilder(sconf);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        scBuild.createCoswidData(out);
                        sign(rimType, out.toByteArray(), keyFile, certPath, algorithm, detachedSignatureFile,
                                isEmbedded, protectedKid, unProtectedKid, outFile, true);
                        break;
                    case GenericRim.RIMTYPE_COMP_SWID:
                        TcgComponentRimSwid tcgCompRim = new TcgComponentRimSwid();
                        tcgCompRim.create(configFile, eventLog, certPath, keyFile, false,
                                outFile);
                        break;
                    case GenericRim.RIMTYPE_COMP_COSWID:
                        TcgCompRimCoswidConfig tcconf = new TcgCompRimCoswidConfig(configFile);
                        TcgCompRimCoswidBuilder tcBuild = new TcgCompRimCoswidBuilder(tcconf);
                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                        tcBuild.initTcgRim(out2);
                        sign(rimType, out2.toByteArray(), keyFile, certPath, algorithm, detachedSignatureFile,
                                isEmbedded, protectedKid, unProtectedKid, outFile, true);
                        break;
                    case GenericRim.RIMTYPE_CORIM_COMID:
                        byte[] corimBytesUnsigned = CoRimBuilder.build(configFile);
                        Path outFileSignedPath = Paths.get(outFile);
                        Files.write(outFileSignedPath, CoRimBuilder.createSignedCorim(corimBytesUnsigned,
                                keyFile, certPath, algorithm, isEmbedded));
                        break;
                    default :
                        System.out.println("Error: Create for rim type of " + rimType
                                + " is not currently supported.");
                        System.exit(1);
                }
            } else {  // Unsigned RIMs
                switch (rimType) {
                    case GenericRim.RIMTYPE_PCRIM:
                        PcClientRim pcRim = new PcClientRim();
                        pcRim.create(configFile, eventLog, certPath, keyFile, false, outFile);
                        break;
                    case GenericRim.RIMTYPE_COSWID:
                        CoswidConfig sconf = new CoswidConfig(configFile);
                        CoswidBuilder cBuild = new CoswidBuilder(sconf);
                        cBuild.createCoswidData(outFile);
                        break;
                    case GenericRim.RIMTYPE_COMP_COSWID:
                        TcgCompRimCoswidConfig tconf = new TcgCompRimCoswidConfig(configFile);
                        TcgCompRimCoswidBuilder tBuild = new TcgCompRimCoswidBuilder(tconf);
                        tBuild.createTcgComponentRim(outFile);
                        break;
                    case GenericRim.RIMTYPE_CORIM_COMID:
                        byte[] corimBytes = CoRimBuilder.build(configFile);
                        Path outFilePath = Paths.get(outFile);
                        Files.write(outFilePath, corimBytes);
                        break;
                    default:
                        System.out.println("Error: Create for a \"signed\" rim type of " + rimType
                                + " is not currently supported.");
                        System.exit(1);
                }
            }
        } catch (IOException e) {
            System.out.println("Error Reading file: " +  configFile + ". Error = " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Error Creating Signature: " +  configFile + ". Error = " + e.getMessage());
            System.exit(1);
        }
        System.out.println("Created a RIM of type  " + rimType + ", using configuration file "
                + configFile + " ");
        if (isUnsigned) {
            System.out.println(rimType + " rim was written to " + outFile);
        }
    }

    /**
     * Signs the contents of a file.
     * @param rimType         type of RIM (pcclient, coswid, etc)
     * @param inFile          file holding the data to sign
     * @param keyFile         key used to sign the RIM
     * @param certPath        reference to an x.509 certificate file used to determine algorithm to sign with
     * @param algorithm       algorithm to use if no certificate is supplied
     * @param detachedSignatureFile detached file (for swid/xml RIMs it's a detached signature,
     *                              for cose it's a detached payload)
     * @param embedded        true if embedded x.509 signing certificate in COSE protected header
     * @param protectedKid    kid in protected header
     * @param unProtectedKid  kid in unprotected header
     * @param outFile         file to write the RIM to
     * @param savePayload     true if saving paylaod
     */
    private static void sign(final String rimType, final String inFile, final String keyFile,
                             final String certPath, final String algorithm,
                             final String detachedSignatureFile, final boolean embedded,
                             final String protectedKid, final String unProtectedKid,
                             final String outFile, final boolean savePayload) throws RuntimeException {

        File payloadFile = new File(inFile);

        try {
            byte[] payloadData = Files.readAllBytes(payloadFile.toPath());

            sign(rimType, payloadData, keyFile, certPath, algorithm, detachedSignatureFile, embedded,
                    protectedKid, unProtectedKid, outFile, false);
        } catch (Exception e) {
            System.out.println("Error creating Cose Signature on " + inFile + ": ");
            System.out.println(e.toString());
            System.exit(1);
        }
    }
    /**
     * Signs the contents of a byte array.
     * @param rimType         type of RIM (pcclient, coswid, etc)
     * @param payloadData     byte array holding the data to sign
     * @param keyFile         key used to sign the RIM
     * @param certPath        reference to an x.509 certificate file used to determine algorithm to sign with
     * @param algorithm       algorithm to use if no certificate is supplied
     * @param detachedSignatureFile detached file (for swid/xml RIMs it's a detached signature,
     *                              for cose it's a detached payload)
     * @param embedded        true if embedded x.509 signing certificate in COSE protected header
     * @param protectedKid    kid in protected header
     * @param unProtectedKid  kid in unprotected header
     * @param outFile         file to write the RIM to
     * @param savePayload     true if saving paylaod
     */
    private static void sign(final String rimType, final byte[] payloadData, final String keyFile,
                             final String certPath, final String algorithm,
                             final String detachedSignatureFile, final boolean embedded,
                             final String protectedKid, final String unProtectedKid,
                             final String outFile, final boolean savePayload) throws RuntimeException {
        X509Certificate cert = null;
        byte[] signedRim = null;
        byte[] kid = null;
        boolean useUnprotectdKid = false;
        //File payloadFile = new File(inFile);
        DefaultCrypto cryptoSigner = new DefaultCrypto();
        try {
            if (!certPath.isEmpty()) {
                try (FileInputStream is = new FileInputStream(certPath)) {
                    CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                    cert = (X509Certificate) certFactory.generateCertificate(is);
                }
            }

            //byte[] payloadData = Files.readAllBytes(payloadFile.toPath());
            cryptoSigner.loadPrivateKey(keyFile, cert, algorithm);

            CoseSignature coseSign = new CoseSignature();
            //byte[] toBeSigned = coseSign.createToBeSigned(cryptoSigner.getAlgorithm(),
            //       cryptoSigner.getKid(), payloadData, cert, embedded);
            int alg = CoseAlgorithm.getAlgId(cryptoSigner.getAlgorithm());

            if (!protectedKid.isEmpty()) {
                kid = HexUtils.hexStringToByteArray(protectedKid);
            } else if (!unProtectedKid.isEmpty()) {
                useUnprotectdKid = true;
                kid = HexUtils.hexStringToByteArray(unProtectedKid);
            } else {
                kid = cryptoSigner.getKid().getBytes(StandardCharsets.UTF_8);
            }

            byte[] toBeSigned = coseSign.createToBeSigned(alg, kid,
                    payloadData, cert, useUnprotectdKid, embedded, rimType);
            byte[] signature = cryptoSigner.sign(toBeSigned);

            coseSign.addSignature(signature);
            if (detachedSignatureFile.isEmpty()) {
                signedRim = coseSign.getSignedData();
                // Write the data to the output file
                Files.write(new File(outFile).toPath(), signedRim);
                System.out.println("Signed Cose Object written to " + outFile);
            } else {
                // Detached signatures require a nil payload (referred to as "detached content" in rfc 9052)
                coseSign.setNilPayload();
                signedRim = coseSign.getSignedData();
                if (savePayload) { // create needs to save both the payload and the signature
                    // Write the data to the output file
                    Files.write(new File(outFile).toPath(), signedRim);
                    Files.write(new File(detachedSignatureFile).toPath(), payloadData);
                    System.out.println("Signed Cose Object written to " + outFile);
                    System.out.println("Payload Object written to " + detachedSignatureFile);
                } else {
                    // Sign needs to save only payload & signature since payload was given as parameter
                    Files.write(new File(outFile).toPath(), signedRim);
                    System.out.println("Signed Cose Object written to " + detachedSignatureFile);
                }
            }
        }  catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    /**
     * Verifies the signature on a RIM file.
     * @param rimType         type of RIM to verify
     * @param certPath        reference to an x.509 certificate file used to determine algorithm to sign with
     * @param trustPath       path to a pem file holding one or more certificates used to verify signature
     * @param publicKeyFile   optional public key file used to verify the signature
     * @param inFile          file holding the RIM to verify
     * @param embedded        true if verifying embedded signing cert per RFC 9360 (for signed COSE objects)
     * @param supportRim      path to the support rim file
     * @param detachedFile    path to the detached signature
     */
    private static void verify(final String rimType, final String inFile, final String supportRim,
                               final String publicKeyFile, final String certPath, final String trustPath,
                               final String detachedFile, final boolean embedded) {
        // Get Cert used for cert verification (need cert ID for signature encoding)
        X509Certificate cert = null;
        boolean verified = false;
        PublicKey pk = null;
        String alg = "";
        String sigType = "none";
        switch (rimType) {
            case GenericRim.RIMTYPE_PCRIM: sigType = "xmlDsig"; break;
            // case GenericRim.RIMTYPE_TCG_PC_RIM_DSIG: sigType = "xmlDsig"; break;
            // case GenericRim.RIMTYPE_TCG_COMPONENT_RIM_SWID: sigType = "xmlDsig"; break;
            case GenericRim.RIMTYPE_COMP_SWID: sigType = "xmlDsig"; break;
            case GenericRim.RIMTYPE_COMP_COSWID:  sigType = "cose"; break;
            case GenericRim.RIMTYPE_COSWID: sigType = "cose"; break;
            case GenericRim.RIMTYPE_CORIM_COMID: sigType = "cose"; break;
            case GenericRim.SIGTYPE_COSE: sigType = "cose"; break;
            case GenericRim.SIGTYPE_DSIG: sigType = "xmlDsig"; break;
            default:
                System.out.println("Error: verify for rim typ of " + rimType
                        + " is not currently supported.");
                System.exit(1);
        }
        try {
            if (sigType.compareTo("xmlDsig") == 0) {
                PcClientRim pcRim = new PcClientRim();
                verified = pcRim.validate(inFile, certPath, supportRim, trustPath);
            } else if (sigType.compareTo("cose") == 0) {
                // Set up the crypto device used for signing
                CryptoEngine cryptoSigner = new DefaultCrypto();
                if (!certPath.isEmpty()) {
                    try (FileInputStream is = new FileInputStream(certPath)) {
                        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                        cert = (X509Certificate) certFactory.generateCertificate(is);
                        pk = cert.getPublicKey();
                    }
                }
                if (!publicKeyFile.isEmpty()) {
                    cryptoSigner.loadPrivateKey(publicKeyFile, cert, "");
                    pk = cryptoSigner.getPublicKey();
                }
                // read the data file in
                File coseFile = new File(inFile);
                byte[] coseFileBytes = Files.readAllBytes(coseFile.toPath());
                CoseSignature verifySignature = new CoseSignature();
                byte[] toBeVerified;
                byte[] signatureData;
                if (!detachedFile.isEmpty()) { // Read in detached signature if provided
                    File payloadFile = new File(detachedFile);
                    byte[] payload = Files.readAllBytes(payloadFile.toPath());
                    toBeVerified = verifySignature.getToBeVerified(coseFileBytes, payload);
                    signatureData = verifySignature.getSignature();
                } else {  // Signature is attached
                    toBeVerified = verifySignature.getToBeVerified(coseFileBytes);
                    signatureData = verifySignature.getSignature();
                }
                // Check for embedded cert/thumbprint
                if (embedded) {
                    X509Certificate embeddedCert = verifySignature.getEmbeddedCert(toBeVerified);
                    if (embeddedCert != null) {
                        cert = embeddedCert;
                        pk = cert.getPublicKey();
                    } else {
                        System.out.println("Error: Embedded cert failed to validate. Exiting...");
                        System.exit(1);
                    }
                }
                alg = CoseAlgorithm.getAlgName(verifySignature.getAlgId());
                verified = cryptoSigner.verify(cert, pk, alg, toBeVerified, signatureData);
            } else {
                System.out.println("Error: Verify for RIM Type of " + rimType + " is not supported.");
            }
        } catch (IOException | CertificateException | RuntimeException e) {
            System.out.println("Error processing Cose Signature on " + inFile + ": ");
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (verified) {
            System.out.println("Signature verified :-)");
        } else {
            System.out.println("Signature failed to verify :-(");
            System.exit(1);
        }
    }

    /**
     * This method parses the version number from the jar filename in the absence of
     * the VERSION file expected with an rpm installation.
     */
    private static void parseVersionFromJar() {
        System.out.println("Installation file VERSION not found.");
        String filename = new File(Main.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath()).getName();
        Pattern pattern = Pattern.compile("(?<=rim-tool-)[0-9]\\.[0-9]\\.[0-9]");
        Matcher matcher = pattern.matcher(filename);
        if (matcher.find()) {
            System.out.println("TCG rimtool version: " + matcher.group());
        }
    }
}
