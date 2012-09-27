/*
 * se.devoteam.maven.crypto.EncryptException
 * 
 * Version:  1.0 
 *
 * Date:     2012-09-27
 * 
 * Copyright: 
 */
package se.devoteam.maven.crypto;

/**
 * @author Karin Karlsson
 */
public final class EncryptException extends Exception {

	private static final long serialVersionUID = 1L;

	public EncryptException() {
	}

	public EncryptException(String message) {
		super(message);
	}

	public EncryptException(Throwable cause) {
		super(cause);
	}

	public EncryptException(String message, Throwable cause) {
		super(message, cause);
	}

}
