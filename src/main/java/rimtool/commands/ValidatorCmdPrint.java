package rimtool.commands;

import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.ParameterException;

import java.util.Map;

/**
 * This class validates the input parameter requirements for command Print.
 */
public class ValidatorCmdPrint implements IParametersValidator {

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
