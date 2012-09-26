/*
 * se.devoteam.maven.security.SecurityDispatcher
 * 
 * Version:  1.0 
 *
 * Date:     2012-09-25
 * 
 * Copyright: 
 */
package se.devoteam.maven.security;

//import java.util.Map;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.sonatype.plexus.components.cipher.PlexusCipher;
import org.sonatype.plexus.components.cipher.PlexusCipherException;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcherException;

/**
 * This component encrypts and decrypts configured passwords (passwords to configured servers and proxies).
 * 
 * @author Karin Karlsson
 * @see #decrypt(String)
 */
@Component( role = SecDispatcher.class, hint = "devoteamSecDisp" )
public final class SecurityDispatcher implements SecDispatcher {
	
	
	@Requirement(optional = true)
    private Logger logger;
	
	@Requirement
	protected PlexusCipher cipher;
	
	//TODO: how is this supposed to work? - pair password with a specific decryptor
	//@Requirement
	//protected Map decryptors;
	
	@Requirement
	private SecretKey secretKey;
	
	public SecurityDispatcher() {
		if (logger != null && logger.isDebugEnabled()) {
			logger.debug(this.getClass().getName());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String decrypt(final String str) throws SecDispatcherException {
		
		if (cipher.isEncryptedString(str)) {
			return decryptPassword(str, decryptSecretKey());
		}

		return null;
	}
	
	
	/**
	 * Decryptes the password.
	 * 
	 * @param pwd the password
	 * @param decryptedSecretKey the decrypted secret key
	 * @return the decrypted password 
	 * @throws SecDispatcherException unable to decrypt the password
	 */
	private String decryptPassword(final String pwd, final String decryptedSecretKey) throws SecDispatcherException {
		try {
			return cipher.decryptDecorated(pwd, decryptedSecretKey);
		} catch (PlexusCipherException e) {
			throw new SecDispatcherException(e);
		}
	}
	
	/**
	 * Decrypts the secret key using the {@link #SYSTEM_PROPERTY_SEC_LOCATION} as pass phrase
	 * @return the decrypted secret key
	 * @throws SecDispatcherException
	 */
    private String decryptSecretKey() throws SecDispatcherException {
  
        if(secretKey.isSet()) {
        	try {
				return cipher.decryptDecorated(secretKey.getKey(), secretKey.getPassPhrase());
			} catch (PlexusCipherException e) {
				throw new SecDispatcherException(e);
			}
        }
        throw new SecDispatcherException("Secret key not set.");
        
    }


}
