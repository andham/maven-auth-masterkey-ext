/*
 * se.devoteam.maven.crypto.Encrypter
 * 
 * Version:  1.0 
 *
 * Date:     2012-09-27
 * 
 * Copyright: 
 */
package se.devoteam.maven.crypto;

/**
 * Encrypts a string.
 * 
 * @author Karin Karlsson
 */
public interface Encrypter {
	
	/**
	 * TODO: remove this
	 * Encrytps a string.
	 * 
	 * @param str the string to encrypt
	 * @param phrase used when encrypting/decrypting the string
	 * @return the encrypted string
	 * @throws EncryptException unable to encrypt the string
	 */
	public String encryptAndDecorate(String str, String phrase) throws EncryptException;
	
	/**
	 * Encrytps a string.
	 * 
	 * @param str the string to encrypt
	 * @return the encrypted string
	 * @throws EncryptException unable to encrypt the string
	 */
	public String encryptAndDecorate(String str) throws EncryptException;

}
