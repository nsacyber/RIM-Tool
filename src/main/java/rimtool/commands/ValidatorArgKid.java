package rimtool.commands;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import hirs.utils.HexUtils;

/**
 * This class validates a key identifier (kid) which has been input by the user via command line.
 * Validates that the kid has only hexadecimal characters.
 */
public class ValidatorArgKid implements IParameterValidator {

    /**
     * This method validates an input key identifier.
     * Tests if the parameter supplied to the kid contains valid hexidecimal characters (0-9 and a-e)
     * @param name ARG_PROTECTED_KID or ARG_UNPROTECTED_KID
     * @param value data supplied by the user
     * @throws ParameterException if the parameter contains any non-hexadecimal characters.
     */
    @Override
    public void validate(final String name, final String value) throws ParameterException {
        try {
            byte[] test = HexUtils.hexStringToByteArray(value);
        } catch (NumberFormatException e) {
            throw new ParameterException("Error with the " + name
                    + " parameter: Invalid characters supplied: " + value
                    + ". \nPlease verify parameter contains only hexadecimal characters.\n");
        }
    }
}
