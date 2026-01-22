package rimtool.commands;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class validates a directory which has been input by the user via command line.
 * Validates whether the path is valid and is searchable/traversable.
 */
public class ValidatorArgDirectory implements IParameterValidator {

    /**
     * This method validates an input directory.
     *
     * @param name ARG_IN
     * @param value path
     *
     * @throws ParameterException if there is any error in validating the input directory
     */
    public void validate(final String name, final String value) throws ParameterException {
        if (value == null) {
            throw new ParameterException("Error with " + name
                    + " parameter: path cannot be null.");
        }

        try {
            Path directory = Paths.get(value);
            if (Files.isDirectory(directory)) {
                if (!Files.isExecutable(directory)) {
                    throw new ParameterException(String.format("%s is not traversable/searchable, "
                            + "check permissions.", directory));
                }
            }
        } catch (InvalidPathException e) {
            throw new ParameterException("Unable to convert " + value + " to a file path: "
                    + e.getMessage());
        }
    }
}
