package rimtool.commands;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.io.File;

/**
 * This class validates a file which has been input by the user via command line.
 * Validates whether the file path is valid and whether the file is empty.
 */
public class ValidatorArgFile implements IParameterValidator {

    /**
     * This method validates an input file.
     *
     * @param name ARG_IN
     * @param value private key file directory
     *
     * @throws ParameterException if there is any error in validating the input file
     */
    public void validate(final String name, final String value) throws ParameterException {
        if (value == null) {
            throw new ParameterException("Error with " + name
                    + " parameter: File path cannot be null.");
        }

        try {
            File file = new File(value);
            if (!file.isFile()) {
                throw new ParameterException("Error with " + name
                        + " parameter: Invalid file path: " + value + ". Please verify file path.");
            }
            if (file.length() == 0) {
                throw new ParameterException("Error with " + name
                        + " parameter: File " + value + " is empty.");
            }
        } catch (SecurityException e) {
            throw new ParameterException("Error with " + name
                    + " parameter: Read access denied for " + value + ", please verify permissions.");
        }
    }
}
