package se.devoteam.maven;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.codehaus.plexus.component.annotations.Component;

/*
 * se.devoteam.maven.MavenExtSecretKey
 * 
 * Version:  1.0 
 *
 * Date:     2012-09-20
 * 
 * Copyright: 
 */

/**
 * This class handles the master key that is used by maven when decrypting a server/proxy password.
 * 
 * @author Karin Karlsson
 *
 */
@Component(role = SecretKey.class) 
public class MavenExtSecretKey implements SecretKey {

	
	//TODO: error handling
	
	private static final long serialVersionUID = 1L;
	
	private final String tmpDirName = ".mvndevo";
	private final String fileName = "m" + System.currentTimeMillis();
	private final String fileExt = ".tmp";
	private final boolean set;
	
	
	public MavenExtSecretKey() {
		
		KeyReader keyReader = null;

		try {
			//TODO: get the secret key from a another place (card, cert???)
			keyReader = new KeyReader();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (keyReader != null) {
			StringBuilder securitySettingsKey = setSecretKey(keyReader.getKey());
			set = makeAvaialble(securitySettingsKey);
		} else {
			set = false;
		}
		
	}

	//TODO: handle other settings as well
	
	private StringBuilder setSecretKey(final String secretKey) {

		final StringBuilder builder = new StringBuilder();
		builder.append("<settingsSecurity>");
		builder.append("<master>").append(secretKey).append("</master>");
		builder.append("</settingsSecurity>");
		return builder;
	}

	public boolean isSet() {
		return set;
	}
	
	
	//TODO: fix error handling
	
	private boolean makeAvaialble(final StringBuilder builder) {
	
		
		File tmpDir = new File(System.getProperty("java.io.tmpdir"),tmpDirName);
		FileOutputStream fout = null;
		
		boolean success = false;
		
		try {
		
			if (!tmpDir.exists()) {
				tmpDir.mkdir();
			}
			//TODO: what if deleteOnExit doesn't work
			tmpDir.deleteOnExit();
			
			File keyFile = File.createTempFile(fileName, fileExt, tmpDir);
			fout = new FileOutputStream(keyFile);
			fout.write(builder.toString().getBytes());
			
			System.setProperty(SYSTEM_PROPERTY_SEC_LOCATION,keyFile.getCanonicalPath());
			
			//TODO: what if deleteOnExit doesn't work
			keyFile.deleteOnExit();
			
			success =  true;
		
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		
		return success;
		
	}


}
