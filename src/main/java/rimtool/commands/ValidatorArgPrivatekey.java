package rimtool.commands;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import lombok.Getter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static hirs.utils.crypto.DefaultCrypto.containsAll;

/**
 * This class validates a private key which has been input by the user via command line.
 * Validates whether the key is encrypted, and whether a password is needed to decrypt.
 */
public class ValidatorArgPrivatekey implements IParameterValidator {

    // non-encrypted headers
    private static final String PKCS1_HEADER = "-----BEGIN RSA PRIVATE KEY-----";
    private static final String PKCS8_HEADER = "-----BEGIN PRIVATE KEY-----";
    private static final String ECC_KEY_HEADER = "-----BEGIN EC PRIVATE KEY-----";
    private static final String[] JSON_WEB_KEY_HEADER = {"keys", ":", "{", "}"};

    // encrypted headers
    private static final String PKCS8_ENCRYPTED_HEADER = "-----BEGIN ENCRYPTED PRIVATE KEY-----";

    /**
     * Human-readable error message if arguments do not validate.
     */
    @Getter
    private String errorMessage = "";

    /**
     * This method validates a private key file.
     *
     * @param name ARG_PRIVATEKEYFILE
     * @param value private key file directory
     *
     * @throws ParameterException if there is any error in validating the private key
     */
    public void validate(final String name, final String value) throws ParameterException {

        // get key file content as string
        String privateKeyFileContents;
        privateKeyFileContents = getkeyFileContentAsString(value);

        // validate the key file content has one of the allowable headers
        validateHeader(privateKeyFileContents);
    }

    /**
     * This method reads the private key file and converts to string.
     *
     * @param keyPath the path to the private key file
     * @return the private key file in string form (includes headers + key)
     */
    private String getkeyFileContentAsString(final String keyPath) {

        byte[] privateKeyFileBytes;
        String privateKeyFile = "";
        try {
            privateKeyFileBytes = Files.readAllBytes(Paths.get(keyPath));
            privateKeyFile = new String(privateKeyFileBytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            errorMessage = "Error with -k parameter: Cannot read file " + keyPath
                    + " as byte array, Exception: " + e;
            throw new ParameterException(errorMessage);
        }

        return privateKeyFile;
    }

    /**
     * This method reads the private key file determines if a valid header is found.
     *
     * @param keyFileContents the contents of the private key file
     */
    private void validateHeader(final String keyFileContents) {

        // check encrypted key file
        if (keyFileContents.contains(PKCS8_ENCRYPTED_HEADER)) {
            errorMessage = "Error with -k parameter: Currently not supporting encrypted private key files.";
            throw new ParameterException(errorMessage);
        }
        // check non-encrypted key file
        if (keyFileContents.contains(PKCS1_HEADER)
                || keyFileContents.contains(PKCS8_HEADER)
                || keyFileContents.contains(ECC_KEY_HEADER)
                || containsAll(keyFileContents, JSON_WEB_KEY_HEADER)) {
            return;
        }
        errorMessage = "Error with -k parameter: Could not find private key header; "
                + "please use PEM-formatted key file or json web key.";
        throw new ParameterException(errorMessage);
    }
}
