package se.devoteam.maven;

/*
 * se.devoteam.maven.DecrypterTest
 * 
 * Version:  1.0 
 *
 * Date:     2012-09-18
 * 
 * Copyright: 
 */

import java.io.File;
//import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.building.SettingsProblem;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.apache.maven.settings.crypto.SettingsDecryptionRequest;
import org.apache.maven.settings.crypto.SettingsDecryptionResult;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

/**
 * Test the {@link se.devoteam.maven.Decrypter} class.
 * 
 * @author Karin Karlsson
 *
 */
public class DecrypterTest {
	
	//TODO: figure out a way to create a test case with a plexus container
	//TODO: inject the Decrypter with a test secret key
	
	private static final Properties settings = new Properties();
	//private static final String TMP_DIR_PROPERTY_NAME = "test.tmp.dir";
	
	/**
	 * Loads the <code>test.settings.properties</code> and sets the <code>test.tmp.dir</code> property.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		
		InputStream is = null;
		try {
			is = DecrypterTest.class.getResourceAsStream("/test.settings.properties");
			if (is == null) {
				is = DecrypterTest.class.getResourceAsStream("test.settings.properties");
			}
			Assert.assertNotNull(is);
			settings.load(is);
			
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * Test {@link se.devoteam.maven.Decrypter#decrypt(SettingsDecryptionRequest)}.
	 * <p/>It sends in an empty request to the decrypt method and should get an empty result back.
	 */
	@Test
	public void testDecryptEmptyRequest() {
		SettingsDecrypter decrypter = new Decrypter();
		SettingsDecryptionResult result = decrypter.decrypt(getEmptyRequest());
		Assert.assertNotNull(result);
		Assert.assertTrue(result.getServers().isEmpty());
		Assert.assertTrue(result.getProxies().isEmpty());
		Assert.assertTrue(result.getProblems().isEmpty());	
	}
	
	
	/**
	 * Test {@link se.devoteam.maven.Decrypter#decrypt(SettingsDecryptionRequest)}.
	 * <p/>The test provokes an error by not setting the 
	 * {@link #Decrypter.SYSTEM_PROPERTY_SEC_LOCATION) property.
	 * 
	 * The test is successful if the result object has one problem attached to it
	 * that declares it failed to decrypt the password for server {@link #getOneServerPasswordEncrypted()}
	 */
	@Test
	public void testDecryptPasswordProblem() {
		SettingsDecrypter decrypter = new Decrypter();
		
		System.clearProperty(Decrypter.SYSTEM_PROPERTY_SEC_LOCATION);

		SettingsDecryptionRequest req = getOneServerPasswordEncrypted();
		SettingsDecryptionResult result = decrypter.decrypt(req);
		List<SettingsProblem> problems = result.getProblems();
		Assert.assertTrue(problems.size() == 1);
		Assert.assertTrue(problems.get(0).getMessage().startsWith("Failed to decrypt password for server " + req.getServers().get(0).getId()));

		
	}
	
	/**
	 * Test {@link se.devoteam.maven.Decrypter#decrypt(SettingsDecryptionRequest)}.
	 * <p/>The test creates a temporary file formatted as the <code>settings-security.xml</code>
	 * It retrieves the content of the file from the test properties.
	 * 
	 * <p/>The temporary file is stored in the <code>test.tmp.dir</code> directory.
	 * The file name pattern is m<i>System.currentTimeMillis()</i>.tmp
	 * 
	 * @see #createContent()
	 * @see #createTmpFile(String, String)
	 * @see #getOneServerPasswordEncrypted()
	 */
	@Test
	public void testDecryptPasswordTmpFile() {
		
		SettingsDecrypter decrypter = new Decrypter();
		SettingsDecryptionResult result = decrypter.decrypt(getOneServerPasswordEncrypted());
		Assert.assertFalse(result.getServer().getPassword().startsWith(settings.getProperty("encryption.start")));
		Assert.assertFalse(result.getServer().getPassword().endsWith(settings.getProperty("encryption.end")));
		Assert.assertTrue(result.getServer().getPassword().equals(settings.getProperty("password.plain.text")));	
	}
	
	/**
	 * Test {@link se.devoteam.maven.Decrypter#decrypt(SettingsDecryptionRequest)}.
	 * <p/>This test sends in a server object where the server properties are not set.
	 * The test is successful if the decrypter instance ignores the empty server object.
	 */
	@Test
	public void testPwdIsNull() {
		SettingsDecrypter decrypter = new Decrypter();
		SettingsDecryptionResult result = decrypter.decrypt(getServerPasswordNotSetRequest());
		Assert.assertNull(result.getServer().getPassword());
		
	}
	
	/**
	 * Creates a new <code>SettingsDecryptionRequest</code> instance that has one server instance. 
	 * The Server instance properties are defined in the <code>test.settings.properties</code>.
	 * 
	 * @return a new <code>SettingsDecryptionRequest</code> instance that has one server and no proxies
	 * Note that the methods <code>setProxies(List)</code> and <code>setServers(List)</code>
	 * throws <code>UnsupportedOperationException</code>.
	 * 
	 * @see #createContent()
	 */
	private static SettingsDecryptionRequest getOneServerPasswordEncrypted() {
		return new SettingsDecryptionRequest() {
			
			@SuppressWarnings("unchecked")
			public List<Proxy> getProxies() {
				return Collections.EMPTY_LIST;
			}

			public List<Server> getServers() {
				return getOneServerEncryptedPwd();
			}

			/**
			 * Not supported. The proxy list is empty.
			 */
			public SettingsDecryptionRequest setProxies(List<Proxy> arg0) {
				throw new UnsupportedOperationException();
			}

			/**
			 * Not supported. 
			 */
			public SettingsDecryptionRequest setServers(List<Server> arg0) {
				throw new UnsupportedOperationException();
			}
			

			private List<Server> getOneServerEncryptedPwd() {
				final List<Server> servers = new ArrayList<Server>(1);
				Server s = new Server();
				s.setId(settings.getProperty("org.apache.maven.settings.Server.id"));
				s.setUsername(settings.getProperty("org.apache.maven.settings.Server.username"));
				s.setPassword(settings.getProperty("org.apache.maven.settings.Server.password"));
				servers.add(s);
				return servers;
			}
			
		};
	}
	
	/**
	 * 
	 * @return a new <code>SettingsDecryptionRequest</code> instance that has one server and one proxy.
	 * The server password is not set.
	 * Note that the methods <code>setProxies(List)</code> and <code>setServers(List)</code>
	 * throws <code>UnsupportedOperationException</code>.
	 */
	private static SettingsDecryptionRequest getServerPasswordNotSetRequest() {
		return new SettingsDecryptionRequest() {
			
			@SuppressWarnings("unchecked")
			public List<Proxy> getProxies() {
				return Collections.EMPTY_LIST;
			}

			public List<Server> getServers() {
				return getOneServerPwdNotSet();
			}

			/**
			 * Not supported. The proxy list is empty.
			 */
			public SettingsDecryptionRequest setProxies(List<Proxy> arg0) {
				throw new UnsupportedOperationException();
			}

			/**
			 * Not supported. 
			 */
			public SettingsDecryptionRequest setServers(List<Server> arg0) {
				throw new UnsupportedOperationException();
			}
			

			private List<Server> getOneServerPwdNotSet() {
				final List<Server> servers = new ArrayList<Server>(1);
				Server s = new Server();
				servers.add(s);
				return servers;
			}
			
		};		
	}
	
	/**
	 * 
	 * @return a new <code>SettingsDecryptionRequest</code> instance that has no
	 * servers or proxies.
	 * Note that the methods <code>setProxies(List)</code> and <code>setServers(List)</code>
	 * throws <code>UnsupportedOperationException</code> since this is an empty request 
	 * (i.e. the server list and proxy lists are empty).
	 */
	private static SettingsDecryptionRequest getEmptyRequest() {
		return new SettingsDecryptionRequest() {

			@SuppressWarnings("unchecked")
			public List<Proxy> getProxies() {
				return getEmptyList();
			}

			@SuppressWarnings("unchecked")
			public List<Server> getServers() {
				return getEmptyList();
			}

			/**
			 * Not supported. The proxy list is empty.
			 */
			public SettingsDecryptionRequest setProxies(List<Proxy> arg0) {
				throw new UnsupportedOperationException();
			}

			/**
			 * Not supported. The server list is empty.
			 */
			public SettingsDecryptionRequest setServers(List<Server> arg0) {
				throw new UnsupportedOperationException();
			}
			
			@SuppressWarnings("rawtypes")
			private List getEmptyList() {
				return Collections.EMPTY_LIST;
			}
			
		};
	}

}
