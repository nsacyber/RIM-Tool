package rimtool.commands;

import com.beust.jcommander.IParametersValidator;
import com.beust.jcommander.ParameterException;
import lombok.Getter;
import org.bouncycastle.asn1.x509.Extension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;

/**
 * This class validates a certificate which has been input by the user via command line.
 * Validates whether the certificate can be parsed properly, and whether certain fields exist.
 */
public class ValidatorArgCredential implements IParametersValidator {

    /**
     * String to hold any error associated with certificate validation dates.
     */
    @Getter
    private String dateValidError = "";

    /**
     * Retrieve the original X509 certificate from a file and recreate that certificate to prove it parses.
     *
     * @param parameters the map of parameter pairs (keys and values)
     *
     * @throws ParameterException if there is any error with parsing the cert and locating certain attributes
     */
    public void validate(final Map<String, Object> parameters) throws ParameterException {

        // Since CredentialArgumentValidator is 'global' due to requiring multiple parameters to check,
        // (both certificate parameter and ignoreValidators parameter), it will be run on every command.
        // Need to first check if command contains a certificate as parameter. If not, can return immediately.
        Object certObject = parameters.get(CommandDefinitions.ARG_PUBLICCERTIFICATE);
        if (certObject == null) {
            return;
        }

        // check if user has requested ignoreValidators mode (to allow certain restrictions to be overridden)
        boolean ignoreValidators = false;
        if (ValidatorCommon.isValueNotNull(parameters, CommandDefinitions.ARG_IGNOREVALIDATORS)) {
            ignoreValidators = true;
        }

        //get certificate path
        String certPath = parameters.get(CommandDefinitions.ARG_PUBLICCERTIFICATE).toString();

        // check whether certificate parses
        X509Certificate parsedX509Cert;
        try {
            parsedX509Cert = getParsedCert(certPath);
        } catch (IOException | CertificateException e) {
            throw new ParameterException(e);
        }

        // check for the existence of a few optional parameters within the certificate
        byte[] extension = parsedX509Cert.getExtensionValue(Extension.subjectKeyIdentifier.getId());
        if (Arrays.toString(extension).isEmpty()) {
            throw new ParameterException("Error with -p parameter: Can not retrieve certificate skid.");
        }

        // check valid certificate dates
        if (!isValidDateRange(parsedX509Cert)) {
            if (ignoreValidators) {
                System.out.println("**Note** User selected TEST MODE, so overriding:"
                        + "\n   -p parameter: Invalid certificate date: " + dateValidError);
            } else {
                throw new ParameterException("Error with -p parameter: Invalid certificate date: "
                        + dateValidError);
            }
        }
    }

    /**
     * This method parses the certificate.
     *
     * @param certPath the path to the X509 certificate
     *
     * @return the parsed X509 certificate
     *
     * @throws IOException if certificate file cannot be read
     * @throws CertificateException if there's an issue parsing the certificate
     */
    private X509Certificate getParsedCert(final String certPath) throws IOException, CertificateException {

        X509Certificate parsedCert;
        byte[] certificateBytes;
        try {
            certificateBytes = Files.readAllBytes(Paths.get(certPath));
            ByteArrayInputStream certInputStream = new ByteArrayInputStream(certificateBytes);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            parsedCert = (X509Certificate) cf.generateCertificate(certInputStream);
        } catch (IOException e) {
            throw new IOException("Error with -p parameter: Cannot read file " + certPath
                    + " as byte array", e);
        } catch (CertificateException cEx) {
            throw new CertificateException("Error with -p parameter: "
                    + "Cannot construct X509Certificate from the input stream", cEx);
        }

        return parsedCert;
    }

    /**
     * This method checks to see if the certificate date range is valid.
     *
     * @param parsedCert the X509 parsed certificate
     *
     * @return true if dates are valid
     */
    private boolean isValidDateRange(final X509Certificate parsedCert) {
        try {
            parsedCert.checkValidity();
        } catch (CertificateExpiredException | CertificateNotYetValidException e) {
            dateValidError = e.toString();
            return false;
        }
        return true;
    }
}
