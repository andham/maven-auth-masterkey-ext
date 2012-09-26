/*
 * se.devoteam.maven.security.util.MavenExtSecretKey
 * 
 * Version:  1.0 
 *
 * Date:     2012-09-20
 * 
 * Copyright: 
 */
package se.devoteam.maven.security.util;


import java.io.IOException;

import org.codehaus.plexus.component.annotations.Component;

import se.devoteam.maven.security.SecretKey;


/**
 * This component handles the secret key.
 * 
 * @author Karin Karlsson
 *
 */
@Component(role = SecretKey.class, hint = "testKey") 
public class MavenExtSecretKey implements SecretKey {

	private static final long serialVersionUID = 1L;
	
	//TODO: error handling
	
	private String key;
	
	
	public MavenExtSecretKey() {
		
		KeyReader keyReader = null;
		
		try {
			//TODO: get the secret key from a another place (card, cert???)
			keyReader = new KeyReader();
			key = keyReader.getKey();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public boolean isSet() {
		return key != null;
	}
	
	/**
	 * {@inheritDoc}
	 * @return the key or null if not set.
	 */
	public String getKey() {
		return key;
	}


}
