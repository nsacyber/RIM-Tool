package rimtool.commands;

/**
 * This class includes definitions for the Command package.
 */
@SuppressWarnings("JavadocVariable")
public class CommandDefinitions {

    // command line input commands - definitions
    public static final String CMD_CREATE = "create";
    public static final String CMD_PRINT = "print";
    public static final String CMD_SIGN = "sign";
    public static final String CMD_VERIFY = "verify";
    public static final String CMD_GET = "get";

    // command line input options - definitions
    public static final String ARG_ALGORITHM = "--algorithm";
    public static final String ARG_ALGORITHM_SHORT = "-a";
    public static final String ARG_CONFIG = "--config";
    public static final String ARG_CONFIG_SHORT = "-c";
    public static final String ARG_DETACHED = "--detached";
    public static final String ARG_DETACHED_SHORT = "-d";
    public static final String ARG_EMBEDCERT = "--embed-cert";
    public static final String ARG_EMBEDCERT_SHORT = "-e";
    public static final String ARG_HELP = "--help";
    public static final String ARG_HELP_SHORT = "-h";
    public static final String ARG_IGNOREVALIDATORS = "--ignore-validators";
    public static final String ARG_IN = "--in";
    public static final String ARG_IN_SHORT = "-i";
    public static final String ARG_OUT = "--out";
    public static final String ARG_OUT_SHORT = "-o";
    public static final String ARG_PRIVATEKEYFILE = "--private-key-file";
    public static final String ARG_PRIVATEKEYFILE_SHORT = "-k";
    public static final String ARG_PROTECTED_KID = "--protected-kid";
    public static final String ARG_PROTECTED_KID_SHORT = "-pk";
    public static final String ARG_PUBLICCERTIFICATE = "--public-certificate";
    public static final String ARG_PUBLICCERTIFICATE_SHORT = "-p";
    public static final String ARG_PUBLICKEYFILE = "--public-key-file";
    public static final String ARG_RIMEL = "--rimel";
    public static final String ARG_RIMEL_SHORT = "-l";
    public static final String ARG_RIMTYPE = "--rim-type";
    public static final String ARG_RIMTYPE_SHORT = "-r";
    public static final String ARG_TIMESTAMP = "--timestamp";
    public static final String ARG_TRUSTSTORE = "--truststore";
    public static final String ARG_TRUSTSTORE_SHORT = "-t";
    public static final String ARG_UNPROTECTED_KID = "--unprotected-kid";
    public static final String ARG_UNPROTECTED_SHORT = "-uk";
    public static final String ARG_UNSIGNED = "--unsigned";
    public static final String ARG_UNSIGNED_SHORT = "-u";
    public static final String ARG_VERBOSE = "--verbose";
    public static final String ARG_VERSION = "--version";

    // command line input options - descriptions
    public static final String PARAM_DESCR_ALGORITHM = "The IANA COSE Algorithm Name.";
    public static final String PARAM_DESCR_CONFIG = "The configuration file holding json attributes "
            + "to populate the base RIM with. An example file can be found in /opt/rimtool/data.";
    public static final String PARAM_DESCR_DETACHED = "Use a detached signature file.";
    public static final String PARAM_DESCR_EMBEDCERT =
            "For DSIG: the provided certificate is embedded into the signed swidtag; "
                    + "for COSE: the provided certificate and the thumbprint are embedded into protected header.";
    public static final String PARAM_DESCR_HELP = "Print this help text.";
    public static final String PARAM_DESCR_IGNOREVALIDATORS =
            "Ignores the validator checks for testing purposes.";
    public static final String PARAM_DESCR_IN = "The path of the file to print";
    public static final String PARAM_DESCR_PRIVATEKEYFILE =
            "The private key used to sign the base RIM created by this tool.";
    public static final String PARAM_DESCR_PROTECTED_KID =
            "A hexadecimal string that represents the Key Identifier to place in the COSE protected header."
                    + " SKID of verification certificate by default.";
    public static final String PARAM_DESCR_PUBLICCERTIFICATE =
            "The public key certificate to be used to verify the RIM.";
    public static final String PARAM_DESCR_PUBLICKEYFILE =
            "The public key used to verify the base RIM created by "
                    + "this tool.";
    public static final String PARAM_DESCR_RIMEL = "The TCG eventlog file to use as a support RIM.";
    public static final String PARAM_DESCR_RIMTYPE =
            "RIM Type (e.g. ietf_coswid, tcg_component-rim-swid_cose, etc.)";
    public static final String PARAM_DESCR_TIMESTAMP = "Add a timestamp to the signature. "
            + "Currently only RFC3339 and RFC3852 are supported:\n"
            + "\tRFC3339 [yyyy-MM-ddThh:mm:ssZ]\n\tRFC3852 <counterSignature.bin>";
    public static final String PARAM_DESCR_TRUSTSTORE =
            "The truststore to sign the base RIM created or to validate "
                    + "the signed base RIM.";
    public static final String PARAM_DESCR_OUT = "The file to write the RIM out to. "
            + "The RIM will be written to stdout by default.";
    public static final String PARAM_DESCR_UNPROTECTED_KID =
            "The Key Identifier to place in the COSE un-protected header."
                    + "SKID of verification certificate by default.";
    public static final String PARAM_DESCR_UNSIGNED =
            "Do not sign the RIM file (no signature or signature envelope).";
    public static final String PARAM_DESCR_VERBOSE = "Control output verbosity.";
    public static final String PARAM_DESCR_VERSION = "Output the current version.";

    /**
     * Protected constructor.
     */
    protected CommandDefinitions() {

    }
}
