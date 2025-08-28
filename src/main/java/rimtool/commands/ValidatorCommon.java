package rimtool.commands;

import java.util.Map;

/**
 * ValidatorCommon is a class for common functions used by the Validators for the RIM tool.
 */
public class ValidatorCommon {

    /**
     * This method checks the given key for a null value.
     * @param parameters map
     * @param key the key to check
     * @return true if not null, else false
     */
    public static boolean isValueNotNull(final Map<String, Object> parameters, final String key) {
        Object object = parameters.get(key);
        return object != null;
    }

    /**
     * Protected constructor.
     */
    protected ValidatorCommon() {

    }
}
