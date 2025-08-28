package rimtool.commands;

import com.beust.jcommander.ParameterException;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class for testing TCG RIM Tool's validator class for a private key file input by the user.
 */
public class PrivateKeyValidatorTest {

    /**
     * Set up temporary directory for temporary file to hold the private key.
     */
    @TempDir
    @Getter
    @Setter
    private Path tempDir;

    /**
     * Tests the validation of valid private keys that are not encrypted.
     *
     * @throws IOException
     */
    @Test
    public final void testNonencryptedPrivateKeyValidHeader() throws IOException {

        ValidatorArgPrivatekey privateKeyValidator = new ValidatorArgPrivatekey();

        // PKCS8 key
        Path tempKeyFilePathPKCS8 = tempDir.resolve("tempFile.key");
        byte[] tempCertBytesPKCS8 = this.getClass().getClassLoader()
                .getResourceAsStream("keys/PrivateKeyPKCS8_COMP_OEM1_rim_signer_rsa_3k_sha384.key")
                .readAllBytes();
        Files.write(tempKeyFilePathPKCS8, tempCertBytesPKCS8);

        // key is good and validation should pass
        assertDoesNotThrow(() -> {
            privateKeyValidator.validate(CommandDefinitions.ARG_PRIVATEKEYFILE,
                    tempKeyFilePathPKCS8.toString());
        }, "Method should not throw an exception");

        // EC key
        Path tempKeyFilePathEC = tempDir.resolve("tempFile2.key");
        byte[] tempCertBytesEC = this.getClass().getClassLoader()
                .getResourceAsStream("keys/PrivateKeyEC_COMP_OEM1_rim_signer_ecc_512_sha384.key")
                .readAllBytes();
        Files.write(tempKeyFilePathEC, tempCertBytesEC);

        // key is good and validation should pass
        assertDoesNotThrow(() -> {
            privateKeyValidator.validate(CommandDefinitions.ARG_PRIVATEKEYFILE,
                    tempKeyFilePathEC.toString());
        }, "Method should not throw an exception");
    }

    /**
     * Tests the validation of a private key that is encrypted.
     *
     * @throws IOException
     */
    @Test
    public final void testEncryptedPrivateKeyValidHeader() throws IOException {

        ValidatorArgPrivatekey privateKeyValidator = new ValidatorArgPrivatekey();

        // PKCS8 encrypted key
        Path tempKeyFilePathPKCS8enc = tempDir.resolve("tempFile.key");
        byte[] tempCertBytesPKCS8enc = this.getClass().getClassLoader()
                .getResourceAsStream("keys/PrivateKeyPKCS8Encrypted_HIRS_aca_tls_rsa_3k_sha384.key")
                .readAllBytes();
        Files.write(tempKeyFilePathPKCS8enc, tempCertBytesPKCS8enc);

        // key is encrypted, not supported currently so should not pass
        assertThrows(ParameterException.class, () -> {
            privateKeyValidator.validate(CommandDefinitions.ARG_PRIVATEKEYFILE,
                    tempKeyFilePathPKCS8enc.toString());
        }, "Method should throw an exception");
    }

    /**
     * Tests the validation of a key that does not have a valid header.
     *
     * @throws IOException
     */
    @Test
    public final void testPrivateKeyInvalidHeader() throws IOException {

        ValidatorArgPrivatekey privateKeyValidator = new ValidatorArgPrivatekey();

        // PKCS8 key with bad header
        Path tempKeyFilePathBadHeader = tempDir.resolve("tempFile.key");
        final String keyFileBadHeader =
                "keys/PrivateKeyPKCS8badheader_COMP_OEM1_rim_signer_rsa_3k_sha384.key";
        byte[] tempCertBytesBadHeader = this.getClass().getClassLoader()
                .getResourceAsStream(keyFileBadHeader).readAllBytes();
        Files.write(tempKeyFilePathBadHeader, tempCertBytesBadHeader);

        // key has bad header so should not pass
        assertThrows(ParameterException.class, () -> {
            privateKeyValidator.validate(CommandDefinitions.ARG_PRIVATEKEYFILE,
                    tempKeyFilePathBadHeader.toString());
        }, "Method should throw an exception");
    }
}
