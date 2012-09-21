package se.devoteam.maven;

/*
 * se.devoteam.maven.MavenExtSecretKey
 * 
 * Version:  1.0 
 *
 * Date:     2012-09-20
 * 
 * Copyright: 
 */


import java.io.IOException;

import org.codehaus.plexus.component.annotations.Component;


/**
 * This class handles the master key that is used by maven when decrypting a server/proxy password.
 * 
 * @author Karin Karlsson
 *
 */
@Component(role = SecretKey.class) 
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
