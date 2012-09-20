package se.devoteam.maven;

import java.io.IOException;
import java.io.InputStream;

/**
 * Temporary class. Change to card, cert....??
 * @author Karin
 */
class KeyReader {
	
	private final String key;

	KeyReader() throws IOException {
		InputStream is = null;
		try {
			is = this.getClass().getResourceAsStream("/key.txt");
			StringBuilder builder = new StringBuilder();
			int ch;
			while ((ch = is.read()) > -1) {
				builder.append((char)ch);
			}
			key = builder.toString();
		} finally {
			if (is != null) {
				is.close();
			}
		}
		
	}
	
	String getKey() {
		return key;
	}
	

}
