package rimtool.commands;

import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.ParameterException;

import java.util.Map;

/**
 * This class validates the input parameter requirements for command Get.
 */
public class ValidatorCmdGet implements IParametersValidator {

    /**
     * True if creating a signed RIM, false if user specifies an unsigned RIM; default is signed.
     */
    private boolean signed = true;

    /**
     * This method validates the input parameter map.
     *
     * @param parameters Name-value-pairs of all parameters (e.g. "-host":"localhost").
     * @throws ParameterException
     */
    @Override
    public void validate(final Map<String, Object> parameters) throws ParameterException {

    }
}
