package rimtool.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>CommandMain is a class that handles the command line arguments for the RIM tool.<br>
 * Implements the JCommander package for command line parsing and validating.<br>
 * The Validators will run in this order
 * <ul>
 *    <li>1) Individual member variable Validators (noted via @Parameter above the member variable)
 *    in forward order</li>
 *    <li>2) Overall class Validators (noted via @Parameters above the class declaration) in backwards
 *    order</li>
 * </ul>
 * Note: To enforce required arguments, these arguments cannot be given default values in the Command Classes,
 * even a default value of empty (ex cannot have: String a = "").<br>
 * <p>
 *  <table border="1">
 *      <caption>
 *          "RIM Tool Input Optional Combinations Table"
 *      </caption>
 *   <tr>
 *     <th>Cmd</th> <th>-r</th> <th>-h</th> <th>-c</th> <th>-i</th> <th>-l</th> <th>-k</th> <th>-p</th>
 *     <th>-t</th> <th>-a</th> <th>-d</th> <th>-e</th> <th>-pk</th> <th>-uk</th> <th>-u</th> <th>-o</th> </tr>
 *   <tr>
 *     <td><p>Create</p>(signed)</td> <td>R</td> <td>O</td> <td>R</td> <td> </td> <td>R*</td> <td>R</td>
 *     <td>R**</td>
 *     <td> </td> <td>O</td> <td>O</td> <td>O</td> <td>O</td> <td>O</td> <td> </td> <td>R</td> </tr>
 *   <tr>
 *     <td><p>Create</p>(unsigned)</td> <td>R</td> <td>O</td> <td>R</td> <td> </td> <td>R*</td> <td>R</td>
 *     <td> </td>
 *     <td> </td> <td> </td> <td> </td> <td> </td> <td> </td> <td> </td> <td>R</td> <td>R</td> </tr>
 *   <tr>
 *     <td>Get</td> <td>R</td> <td>O</td> <td> </td> <td>R</td> <td> </td> <td> </td> <td> </td>
 *     <td> </td> <td> </td> <td> </td> <td> </td> <td> </td> <td> </td> <td> </td> <td>R</td> </tr>
 *   <tr>
 *     <td><p>Sign</p>(signed)</td> <td>R</td> <td>O</td> <td> </td> <td>R</td> <td> </td> <td>R</td>
 *     <td>R**</td>
 *     <td> </td> <td>O</td> <td>O</td> <td>O</td> <td>O</td> <td>O</td> <td> </td> <td>R</td> </tr>
 *   <tr>
 *     <td>Print</td> <td>R</td> <td>O</td> <td> </td> <td>R</td> <td> </td> <td> </td> <td> </td>
 *     <td> </td> <td> </td> <td> </td> <td> </td> <td> </td> <td> </td> <td> </td> <td> </td> </tr>
 *   <tr>
 *     <td><p>Verify</p>(signed)</td> <td>R</td> <td>O</td> <td> </td> <td>R</td> <td> </td> <td> </td>
 *     <td>R**</td>
 *     <td>R</td> <td> </td> <td>O</td> <td> </td> <td> </td> <td> </td> <td> </td> <td> </td> </tr>
 * </table>
 *  <table border="1">
 *      <caption>
 *          "RIM Tool Input Optional Combinations Table Pt. 2"
 *      </caption>
 *   <tr>
 *     <th>Cmd</th> <th>--timestamp</th> <th>--ignore-validators</th> <th>--verbose</th> <th>--version</th>
 *     </tr>
 *   <tr>
 *     <td><p>Create</p>(signed)</td> <td> </td> <td>O</td> <td>O</td> <td>O</td> </tr>
 *   <tr>
 *     <td><p>Create</p>(unsigned)</td> <td> </td> <td>O</td> <td>O</td> <td>O</td> </tr>
 *   <tr>
 *     <td>Get</td> <td> </td> <td>O</td> <td>O</td> <td>O</td> </tr>
 *   <tr>
 *     <td><p>Sign</p>(signed)</td> <td> </td> <td>O</td> <td>O</td> <td>O</td> </tr>
 *   <tr>
 *     <td>Print</td> <td> </td> <td>O</td> <td>O</td> <td>O</td> </tr>
 *   <tr>
 *     <td><p>Verify</p>(signed)</td> <td>O*</td> <td>O</td> <td>O</td> <td>O</td> </tr>
 * </table>
 *
 *  * pcrim only<br>
 *  ** Some key files may include a certificate, in such case p option may not be needed. If a
 *     certificate is embedded in the signature the certificate may not be needed for verify.<br>
 */
//  --------------------------------------------------------------------------------------------------------
//  | Command    | -r  | -h  | -c  | -i  | -l  | -k  | -p  | -t  | -a  | -d  | -e  | -pk | -uk | -u  | -o  |
//  |------------|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|
//  | Create (s) |  R  |  O  |  R  |     |  R* |  R  | R** |     |  O  |  O  |  O  |  O  |  O  |     |  R  |
//  | Create (u) |  R  |  O  |  R  |     |  R* |     |     |     |     |     |     |     |     |  R  |  R  |
//  | Get        |  R  |  O  |     |  R  |     |     |     |     |     |     |     |     |     |     |  R  |
//  | Sign       |  R  |  O  |     |  R  |     |  R  | R** |     |  O  |  O  |  O  |  O  |  O  |     |  R  |
//  | Print      |  R  |  O  |     |  R  |     |     |     |     |     |     |     |     |     |     |     |
//  | Verify     |  R  |  O  |     |  R  |     |     | R** |  R  |     |  O  |     |     |     |     |     |
//  --------------------------------------------------------------------------------------------------------
//  ----------------------------------------------------------------------------------
//  | Command           | --timestamp | --ignore-validators | --verbose | --version  |
//  |-------------------|-------------|---------------------|-----------|------------|
//  | Create (signed)   |             |          O          |     O     |      O     |
//  | Create (unsigned) |             |          O          |     O     |      O     |
//  | Get               |             |          O          |     O     |      O     |
//  | Sign              |             |          O          |     O     |      O     |
//  | Print             |             |          O          |     O     |      O     |
//  | Verify            |      O*     |          O          |     O     |      O     |
//  ----------------------------------------------------------------------------------
@Parameters
@Getter
public class CommandMain {

    // parameters that can be used for any command
    @Parameter(description = "This parameter catches all unrecognized arguments.")
    private List<String> unknownOptions = new ArrayList<>();
    @Parameter(names = {CommandDefinitions.ARG_HELP_SHORT, CommandDefinitions.ARG_HELP},
            help = true,
            description = CommandDefinitions.PARAM_DESCR_HELP)
    private boolean help;
    @Parameter(names = {CommandDefinitions.ARG_VERSION},
            description = CommandDefinitions.PARAM_DESCR_VERSION)
    private boolean version = false;
    @Parameter(names = {CommandDefinitions.ARG_VERBOSE},
            description = CommandDefinitions.PARAM_DESCR_VERBOSE)
    private boolean verbose = false;

    /**
     * Method to print the command line options and give info for additional help.
     *
     * @return the string containing this info to print to screen
     */
    public String printHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append("Rim Tool: \n");
        sb.append("Create, Sign, Verify, Print, Get (payload) Reference Integrity Manifests. \n");
        sb.append("Currently supported RIM Formats include: \n");
        sb.append("      TCG PC Client RIM, IETF CoRim, TCG Component RIM (SWID format), "
                + "TCG Component Rim (Coswid) \n");

        sb.append("Current commands are create, sign, verify, print, and get.\n");
        sb.append("Usage:  rim <command> <options>\n");
        sb.append("  refer to the individual command help for more details.\n");
        sb.append("Example:\n");
        sb.append("   rim create " + CommandDefinitions.ARG_HELP + "\n");

        return sb.toString();
    }

    /**
     * Method to return whether user selected help.
     *
     * @return true if user selected help
     */
    public boolean isHelp() {
        return help;
    }
}
