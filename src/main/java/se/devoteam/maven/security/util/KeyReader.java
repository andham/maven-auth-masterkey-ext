/*
 * se.devoteam.maven.security.util.KeyReader
 * 
 * Version:  1.0 
 *
 * Date:     2012-09-20
 * 
 * Copyright: 
 */
package se.devoteam.maven.security.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Temporary class. Change to card, cert....??
 * @author Karin Karlsson
 */
class KeyReader {

	private final Properties props = new Properties();

	KeyReader() throws IOException {
		InputStream is = null;
		try {
			props.load(this.getClass().getResourceAsStream("/key.properties"));
		} finally {
			if (is != null) {
				is.close();
			}
		}
		
	}
	
	String getKey() {
		return props.getProperty("secretkey");
	}
	
	String getPassPhrase() {
		return props.getProperty("passPhrase");
	}

}
