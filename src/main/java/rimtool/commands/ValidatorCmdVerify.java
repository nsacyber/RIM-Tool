package rimtool.commands;

import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.ParameterException;

import java.util.Map;

/**
 * This class validates the input parameter requirements for command Verify.
 */
public class ValidatorCmdVerify implements IParametersValidator {

    /**
     * True if user provided a public cert; default is false.
     */
    private boolean publicCertProvided = false;
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

        if (ValidatorCommon.isValueNotNull(parameters, CommandDefinitions.ARG_PUBLICCERTIFICATE)) {
            publicCertProvided = true;
        }

        boolean certEmbeddedInSig = false;
        if (!publicCertProvided && !certEmbeddedInSig) {
            errorMessage +=
                    "Error: No certificate was provided, nor was the certificate included in the signature.";
        }
    }
}
