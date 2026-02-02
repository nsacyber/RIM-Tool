package rimtool.commands;

import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.ParameterException;
import hirs.utils.rim.unsignedRim.GenericRim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class validates the input parameter requirements for command Verify.
 */
public class ValidatorCmdVerify implements IParametersValidator {

    /*
     * True if user provided a public cert; default is false.
     */
//    private boolean publicCertProvided = false;
    /**
     * Human-readable error message if arguments do not validate.
     */
    private String errorMessage = "";

    /**
     * This method validates the input parameter map.
     *
     * @param parameters Name-value-pairs of all parameters (e.g. "-host":"localhost").
     * @throws ParameterException
     */
    @Override
    public void validate(final Map<String, Object> parameters) throws ParameterException {

        String rimType = parameters.get(CommandDefinitions.ARG_RIMTYPE).toString();
/*
        if (ValidatorCommon.isValueNotNull(parameters, CommandDefinitions.ARG_PUBLICCERTIFICATE)) {
            publicCertProvided = true;
        }

        boolean certEmbeddedInSig = false;
        if (!publicCertProvided && !certEmbeddedInSig) {
            errorMessage +=
                    "Error: No certificate was provided, nor was the certificate included in the signature.";
        }
*/
        switch (rimType) {
            case GenericRim.RIMTYPE_PCRIM:
                checkSupportRimNamesForSeparators(parameters);
                break;
            default:
                break;
        }

        if (!errorMessage.isEmpty()) {
            throw new ParameterException(errorMessage);
        }
    }

    /**
     * This method looks in the given base RIM file for <File name="...">.
     * If the name attribute value contains file separators then an error is recorded.
     * @param parameters to check
     */
    private void checkSupportRimNamesForSeparators(final Map<String, Object> parameters) {
        String baseRim = (String) parameters.get(CommandDefinitions.ARG_IN);
        Path baseRimPath = FileSystems.getDefault().getPath(baseRim);
        Pattern filePattern = Pattern.compile("^\\V*<[a-z0-9]*:File.*name=(.*?)\\s");
        try (BufferedReader reader = Files.newBufferedReader(baseRimPath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher fileMatch = filePattern.matcher(line);
                if (fileMatch.find()) {
                    String filename = fileMatch.group(1);
                    if (filename.contains(File.separator)) {
                        errorMessage += String.format("In %s, support RIM filename %s should not "
                                + "have file separators", baseRim, filename);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            errorMessage += String.format("Unable to access %s, please confirm path and permissions",
                    baseRim);
        } catch (IOException e) {
            errorMessage += String.format("Error while reading %s: %s", baseRim, e.getMessage());
        }
    }
}
