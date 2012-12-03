package se.devoteam.maven.crypto;

import org.apache.maven.security.crypto.SecretKey;
import org.apache.maven.security.crypto.CryptoException;
import org.codehaus.plexus.component.annotations.Component;

/**
 * This class replaces the default secret key implementation.
 * 
 * @author Karin Karlsson
 *
 */
@Component (role = SecretKey.class)
public final class ExtSecretKey implements SecretKey {
    
    /**
     * {@inheritDoc}
     */
    public String getValue() throws CryptoException {
        return "{GiTE7JtdcWUHNZYC+p5P3AZVLDzI7ygb0EOFiFZZ7P0=}";
    }
    
}
