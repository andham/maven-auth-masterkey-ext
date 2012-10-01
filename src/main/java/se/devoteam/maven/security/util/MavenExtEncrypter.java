/*
 * se.devoteam.maven.security.util.MavenExtEncrypter
 * 
 * Version:  1.0 
 *
 * Date:     2012-09-27
 * 
 * Copyright: 
 */
package se.devoteam.maven.security.util;


import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.plexus.components.cipher.PlexusCipher;
import org.sonatype.plexus.components.cipher.PlexusCipherException;

import se.devoteam.maven.crypto.EncryptException;
import se.devoteam.maven.crypto.Encrypter;
import se.devoteam.maven.security.SecretKey;

/**
 * This component encrypts a string.
 * 
 * @author Karin Karlsson
 *
 */
@Component(role = Encrypter.class, hint = "PlexusEncrypt")
public final class MavenExtEncrypter implements Encrypter {
	
	@Requirement
	private PlexusCipher cipher;
	
	@Requirement
	private SecretKey secretKey;
	
	/**
	 * {@inheritDoc}
	 */
	public String encryptAndDecorate(final String str, final String phrase) throws EncryptException {
		try {
			return cipher.encryptAndDecorate(str, phrase);
		} catch (PlexusCipherException e) {
			throw new EncryptException(e);
		}
	}

	public String encryptAndDecorate(String str) throws EncryptException {
		 try {
			return cipher.encryptAndDecorate(str, cipher.decryptDecorated(secretKey.getKey(), secretKey.getPassPhrase()));
		} catch (PlexusCipherException e) {
			throw new EncryptException(e);
		}
	}


}
