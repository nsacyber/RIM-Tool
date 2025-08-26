package rimtool.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import hirs.utils.rim.unsignedRim.GenericRim;
import lombok.Getter;

/**
 * This class includes fields for the user input of the Get command.
 * Contains variables along with their validator parameters.
 */
@Parameters(parametersValidators = {ValidatorCmdGet.class})
@Getter
public class CommandGet {

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

    // other
    @Parameter(names = {CommandDefinitions.ARG_UNSIGNED_SHORT, CommandDefinitions.ARG_UNSIGNED},
            description = CommandDefinitions.PARAM_DESCR_UNSIGNED)
    private boolean unsigned = false;

    // output files
    @Parameter(names = {CommandDefinitions.ARG_OUT_SHORT, CommandDefinitions.ARG_OUT},
            required = true,
            description = CommandDefinitions.PARAM_DESCR_OUT)
    private String outFile;
}
