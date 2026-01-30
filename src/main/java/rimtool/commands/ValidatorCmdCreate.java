package rimtool.commands;

import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.ParameterException;
import hirs.utils.rim.unsignedRim.GenericRim;
import hirs.utils.swid.SwidTagConstants;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
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

    /**
     * This method checks the values of File.name fields in the json config file
     * for file separator characters ('/', '\').
     *
     * @param parameters map containing the config file
     */
    private void checkSupportRimNamesForSeparators(final Map<String, Object> parameters) {
        String configFile = (String) parameters.get(CommandDefinitions.ARG_CONFIG);
        JsonObject configFileContents = null;
        try {
            InputStream is = new FileInputStream(configFile);
            JsonReader reader = Json.createReader(is);
            configFileContents = reader.readObject();
            reader.close();
        } catch (FileNotFoundException e) {
            errorMessage += String.format("Error reading %s to check for file separators", configFile);
        }

        if (configFileContents != null) {
            List<JsonObject> supportRims = configFileContents.getJsonObject(SwidTagConstants.PAYLOAD)
                    .getJsonObject(SwidTagConstants.DIRECTORY)
                    .getJsonArray(SwidTagConstants.FILE)
                    .getValuesAs(JsonObject.class);
            Iterator itr = supportRims.iterator();
            while (itr.hasNext()) {
                JsonObject supportRim = (JsonObject) itr.next();
                String supportRimName = supportRim.getString(SwidTagConstants.NAME);
                if (supportRimName.contains(File.separator)) {
                    errorMessage += String.format("Support RIM %s has file separator "
                            + "characters in its name, please remove and retry", supportRimName);
                }
            }
        }
    }
}
