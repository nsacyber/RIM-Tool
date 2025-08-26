package rimtool.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import hirs.utils.rim.unsignedRim.GenericRim;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class includes fields for the user input of the Sign command.
 * Contains variables along with their validator parameters.
 */
@Parameters(parametersValidators = {ValidatorCmdSign.class, ValidatorArgCredential.class})
@Getter
public class CommandSign {

    // command parameters
    @Parameter(names = {CommandDefinitions.ARG_RIMTYPE_SHORT, CommandDefinitions.ARG_RIMTYPE},
            required = true,
            description = CommandDefinitions.PARAM_DESCR_RIMTYPE)
    private String rimType =  GenericRim.RIMTYPE_PCRIM;
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

    // certificate/ keys
    @Parameter(names = {CommandDefinitions.ARG_PRIVATEKEYFILE_SHORT, CommandDefinitions.ARG_PRIVATEKEYFILE},
            validateWith = ValidatorArgFile.class,
            description = CommandDefinitions.PARAM_DESCR_PRIVATEKEYFILE)
    private String privateKey;
    @Parameter(names = {CommandDefinitions.ARG_PUBLICCERTIFICATE_SHORT,
            CommandDefinitions.ARG_PUBLICCERTIFICATE},
            validateWith = ValidatorArgFile.class,
            description = CommandDefinitions.PARAM_DESCR_PUBLICCERTIFICATE)
    private String publicCertificate = "";
    @Parameter(names = {CommandDefinitions.ARG_ALGORITHM_SHORT, CommandDefinitions.ARG_ALGORITHM},
            description = CommandDefinitions.PARAM_DESCR_ALGORITHM)
    private String algorithm = "";
    @Parameter(names = {CommandDefinitions.ARG_DETACHED_SHORT, CommandDefinitions.ARG_DETACHED},
            validateWith = ValidatorArgFile.class,
            description = CommandDefinitions.PARAM_DESCR_DETACHED)
    private String detachedSignature = "";
    @Parameter(names = {CommandDefinitions.ARG_EMBEDCERT_SHORT, CommandDefinitions.ARG_EMBEDCERT},
            description = CommandDefinitions.PARAM_DESCR_EMBEDCERT)
    private boolean embedded = false;
    @Parameter(names = {CommandDefinitions.ARG_PROTECTED_KID_SHORT, CommandDefinitions.ARG_PROTECTED_KID},
            validateWith = ValidatorArgKid.class,
            description = CommandDefinitions.PARAM_DESCR_PROTECTED_KID)
    private String protectedKid = "";
    @Parameter(names = {CommandDefinitions.ARG_UNPROTECTED_SHORT, CommandDefinitions.ARG_UNPROTECTED_KID},
            validateWith = ValidatorArgKid.class,
            description = CommandDefinitions.PARAM_DESCR_UNPROTECTED_KID)
    private String unProtectedKid =  "";

    // other
    @Parameter(names = {CommandDefinitions.ARG_TIMESTAMP},
            variableArity = true,
            description = CommandDefinitions.PARAM_DESCR_TIMESTAMP)
    private List<String> timestamp = new ArrayList<String>(2);
    @Parameter(names = {CommandDefinitions.ARG_IGNOREVALIDATORS},
            description = CommandDefinitions.PARAM_DESCR_IGNOREVALIDATORS)
    private boolean ignoreValidators = false;

    // output files
    @Parameter(names = {CommandDefinitions.ARG_OUT_SHORT, CommandDefinitions.ARG_OUT},
            required = true,
            description = CommandDefinitions.PARAM_DESCR_OUT)
    private String outFile;
}

