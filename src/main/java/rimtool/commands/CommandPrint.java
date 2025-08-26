package rimtool.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import hirs.utils.rim.unsignedRim.GenericRim;
import lombok.Getter;

/**
 * This class includes fields for the user input of the Print command.
 * Contains variables along with their validator parameters.
 */
@Parameters(parametersValidators = ValidatorCmdPrint.class)
@Getter
public class CommandPrint {

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
}
