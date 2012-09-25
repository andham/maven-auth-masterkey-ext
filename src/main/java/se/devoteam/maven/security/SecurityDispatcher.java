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
import org.sonatype.plexus.components.cipher.PlexusCipher;
import org.sonatype.plexus.components.cipher.PlexusCipherException;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcherException;

/**
 * 
 * @author Karin
 * @see #decrypt(String)
 */
@Component( role = SecDispatcher.class, hint = "devoteamSecDisp" )
public final class SecurityDispatcher implements SecDispatcher {
	
	/**
	 * Used when encrypting and decrypting the secret key.
	 */
	private static final String SYSTEM_PROPERTY_SEC_LOCATION = "settings.security";
	
	//TODO: add loggning
	
	@Requirement
	protected PlexusCipher cipher;
	
	//TODO: how is this supposed to work - pair password with a specific decryptor
	//@Requirement
	//protected Map decryptors;
	
	@Requirement
	private SecretKey secretKey;
	
	public SecurityDispatcher() {
		System.out.println(this.getClass().getName());
	}

	/**
	 * {@inheritDoc}
	 */
	public String decrypt(final String str) throws SecDispatcherException {
		
		if (cipher.isEncryptedString(str)) {
			try {
				return cipher.decrypt(undecorateString(str), decryptSecretKey());
			} catch (PlexusCipherException e) {
				throw new SecDispatcherException(e);
			}
		}

		return null;
	}
	
	
	/**
	 * Removes the decorations from the string.
	 * @param str the string
	 * @return the string where the {@link PlexusCipher#ENCRYPTED_STRING_DECORATION_START} and
	 * {@link PlexusCipher#ENCRYPTED_STRING_DECORATION_STOP} are removed 
	 * @throws SecDispatcherException
	 */
	private String undecorateString(final String str) throws SecDispatcherException {
		try {
			return cipher.unDecorate(str);
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
				return cipher.decryptDecorated(secretKey.getKey(), SYSTEM_PROPERTY_SEC_LOCATION);
			} catch (PlexusCipherException e) {
				throw new SecDispatcherException(e);
			}
        }
        throw new SecDispatcherException("Secret key not set.");
        
    }


}
