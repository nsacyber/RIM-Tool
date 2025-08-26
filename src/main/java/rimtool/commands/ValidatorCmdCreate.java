package rimtool.commands;

import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.ParameterException;
import hirs.utils.rim.unsignedRim.GenericRim;

import java.util.Map;

/**
 * This class validates the input parameter requirements for command Create.
 */
public class ValidatorCmdCreate implements IParametersValidator {

    /**
     * True if creating a signed RIM, false if user specifies an unsigned RIM; default is signed.
     */
    private boolean signed = true;
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

        String rimType = parameters.get(CommandDefinitions.ARG_RIMTYPE).toString();
        if (ValidatorCommon.isValueNotNull(parameters, CommandDefinitions.ARG_UNSIGNED)) {
            signed = false;
        }
        if (ValidatorCommon.isValueNotNull(parameters, CommandDefinitions.ARG_PUBLICCERTIFICATE)) {
            publicCertProvided = true;
        }

        // when creating a signed RIM of any type, first check that user included private key
        if (signed) {
            if (!ValidatorCommon.isValueNotNull(parameters, CommandDefinitions.ARG_PRIVATEKEYFILE)) {
                errorMessage += "Error: For creating a signed RIM, need to include a private key file.\n";
                throw new ParameterException(errorMessage);
            }
        }

        // perform specific checks on specific RIM types
        switch (rimType) {
            case GenericRim.RIMTYPE_PCRIM:
                validatePcrim(parameters);
                break;
            default:
                break;
        }

        if (!errorMessage.isEmpty()) {
            throw new ParameterException(errorMessage);
        }
    }

    /**
     * This method validates input specific to the pcrim.
     *
     * @param parameters Name-value-pairs of all parameters (e.g. "-host":"localhost").
     */
    private void validatePcrim(final Map<String, Object> parameters) {

        // a support RIM is required for a pcrim (both signed and unsigned)
        if (!ValidatorCommon.isValueNotNull(parameters, CommandDefinitions.ARG_RIMEL)) {
            errorMessage += "Error: For creating a PCRIM, need to include a support RIM (event log file).\n";
        }

        if (signed) {
            boolean keyFileIncludesCert = false;
            if (!publicCertProvided && !keyFileIncludesCert) {
                errorMessage +=
                        "Error: No certificate was provided, nor does the key file contain a certificate.";
            }
        }
    }
}
