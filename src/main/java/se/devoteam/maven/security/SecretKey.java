/*
 * se.devoteam.maven.SecretKey
 * 
 * Version:  1.0 
 *
 * Date:     2012-09-20
 * 
 * Copyright: 
 */
package se.devoteam.maven.security;

import java.io.Serializable;


/**
 * 
 * @author Karin Karlsson
 */
public interface SecretKey extends Serializable {
	
	/**
	 * Checks if the secret key has been set or not.
	 * @return true if it is set, false otherwise
	 */
	public boolean isSet();
	
	/**
	 * @return an encrypted the secret key
	 */
	public String getKey();
}
