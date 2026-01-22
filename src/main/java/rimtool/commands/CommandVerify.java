package rimtool.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import hirs.utils.rim.unsignedRim.GenericRim;
import lombok.Getter;

/**
 * This class includes fields for the user input of the Verify command.
 * Contains variables along with their validator parameters.
 */
@Parameters(parametersValidators = {ValidatorCmdVerify.class, ValidatorArgCredential.class})
@Getter
public class CommandVerify {

    // command parameters
    @Parameter(names = {CommandDefinitions.ARG_RIMTYPE_SHORT, CommandDefinitions.ARG_RIMTYPE},
            required = true,
            description = CommandDefinitions.PARAM_DESCR_RIMTYPE)
    private String rimType = GenericRim.RIMTYPE_PCRIM;
    @Parameter(names = {CommandDefinitions.ARG_HELP_SHORT, CommandDefinitions.ARG_HELP},
            help = true,
            description = CommandDefinitions.PARAM_DESCR_HELP)
    private boolean help;

    // input files
    @Parameter(names = {CommandDefinitions.ARG_IN_SHORT, CommandDefinitions.ARG_IN},
            required = true,
            validateWith = ValidatorArgFile.class,
            description = CommandDefinitions.PARAM_DESCR_IN)
    private String inFile;
    @Parameter(names = {CommandDefinitions.ARG_RIMEL_SHORT, CommandDefinitions.ARG_RIMEL},
            validateWith = ValidatorArgDirectory.class,
            description = CommandDefinitions.PARAM_DESCR_RIMEL)
    private String rimEventLog = null;

    // certificate/ keys
    // both PRIVATEKEY and PUBLICKEY use the short arg '-k' or 'ARG_PRIVATEKEYFILE_SHORT'
    @Parameter(names = {CommandDefinitions.ARG_PRIVATEKEYFILE_SHORT,
            CommandDefinitions.ARG_PUBLICKEYFILE},
            validateWith = ValidatorArgFile.class,
            description = CommandDefinitions.PARAM_DESCR_PUBLICKEYFILE)
    private String publicKey = "";
    @Parameter(names = {CommandDefinitions.ARG_PUBLICCERTIFICATE_SHORT,
            CommandDefinitions.ARG_PUBLICCERTIFICATE},
            validateWith = ValidatorArgFile.class,
            description = CommandDefinitions.PARAM_DESCR_PUBLICCERTIFICATE)
    private String publicCertificate = "";
    @Parameter(names = {CommandDefinitions.ARG_TRUSTSTORE_SHORT, CommandDefinitions.ARG_TRUSTSTORE},
//                required = true,
            validateWith = ValidatorArgFile.class,
            description = CommandDefinitions.PARAM_DESCR_TRUSTSTORE)
    private String truststore;
    @Parameter(names = {CommandDefinitions.ARG_DETACHED_SHORT, CommandDefinitions.ARG_DETACHED},
            validateWith = ValidatorArgFile.class,
            description = CommandDefinitions.PARAM_DESCR_DETACHED)
    private String detachedSignature = "";
    @Parameter(names = {CommandDefinitions.ARG_EMBEDCERT_SHORT, CommandDefinitions.ARG_EMBEDCERT},
            description = CommandDefinitions.PARAM_DESCR_EMBEDCERT)
    private boolean embedded = false;

    // other
    @Parameter(names = {CommandDefinitions.ARG_IGNOREVALIDATORS},
            description = CommandDefinitions.PARAM_DESCR_IGNOREVALIDATORS)
    private boolean ignoreValidators = false;
}
