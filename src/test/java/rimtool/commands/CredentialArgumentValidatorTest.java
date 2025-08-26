package rimtool.commands;

import com.beust.jcommander.ParameterException;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class for testing TCG RIM Tool's validator class for a credential input by the user.
 */
public class CredentialArgumentValidatorTest {

    /**
     * Temporary directory used for storing certificate files during tests.
     */
    @TempDir
    @Getter
    @Setter
    private Path tempDir;

    /**
     * Tests the validation of a valid certificate that has been included by the user as a command line input.
     * <p>
     * This test verifies that the validator correctly accepts a valid certificate and does not throw an
     * exception.
     *
     * @throws IOException if an I/O error occurs while writing the certificate file
     */
    @Test
    public final void testCertificateParses() throws IOException {

        ValidatorArgCredential certValidator = new ValidatorArgCredential();

        Path tempCertPath = tempDir.resolve("tempFile.crt");
        byte[] tempCertBytes = this.getClass().getClassLoader()
                .getResourceAsStream("certificates/certificate_valid2035.crt").readAllBytes();
        Files.write(tempCertPath, tempCertBytes);

        Map<String, Object> commandLineInputs = new HashMap<String, Object>();
        commandLineInputs.put(CommandDefinitions.ARG_PUBLICCERTIFICATE, tempCertPath.toString());

        // certificate is good and validation should pass
        assertDoesNotThrow(() -> {
            certValidator.validate(commandLineInputs);
        }, "Method should not throw an exception");

        // include the override user input which should not affect the validation of a good certificate
        commandLineInputs.put(CommandDefinitions.ARG_IGNOREVALIDATORS, true);

        // certificate is good and validation should pass
        assertDoesNotThrow(() -> {
            certValidator.validate(commandLineInputs);
        }, "Method should not throw an exception");
    }

    /**
     * Tests the validation of an expired certificate.
     * <p>
     * This test verifies that the validator correctly detects an expired certificate and throws an exception.
     * It also verifies that the override flag allows the expired certificate to pass validation.
     *
     * @throws IOException if an I/O error occurs while writing the certificate file
     */
    @Test
    public final void testCertificateExpired() throws IOException {

        ValidatorArgCredential certValidator = new ValidatorArgCredential();

        Path tempCertPath = tempDir.resolve("tempFile.crt");
        byte[] tempCertBytes = this.getClass().getClassLoader()
                .getResourceAsStream("certificates/certificate_expired.crt").readAllBytes();
        Files.write(tempCertPath, tempCertBytes);

        Map<String, Object> commandLineInputs = new HashMap<String, Object>();
        commandLineInputs.put(CommandDefinitions.ARG_PUBLICCERTIFICATE, tempCertPath.toString());

        // certificate is out of date and validation should not pass
        Exception exception = assertThrows(ParameterException.class, () -> {
            certValidator.validate(commandLineInputs);
        }, "Method should throw an exception");

        // verify that the exception was due to expired certs
        assertTrue(exception.getMessage()
                .startsWith("Error with -p parameter: Invalid certificate date"));

        // include the override user input which should allow an out-of-date certificate to pass
        commandLineInputs.put(CommandDefinitions.ARG_IGNOREVALIDATORS, true);

        // certificate is out of date but override is enabled so validation should pass
        assertDoesNotThrow(() -> {
            certValidator.validate(commandLineInputs);
        }, "Method should not throw an exception");
    }
}
