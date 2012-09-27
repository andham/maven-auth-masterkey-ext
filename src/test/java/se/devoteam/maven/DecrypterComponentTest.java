package se.devoteam.maven;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;


import junit.framework.Assert;

import org.apache.maven.settings.building.SettingsProblem;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.apache.maven.settings.crypto.SettingsDecryptionRequest;
import org.apache.maven.settings.crypto.SettingsDecryptionResult;
import org.codehaus.plexus.PlexusTestCase;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;

import se.devoteam.maven.crypto.Decrypter;
import se.devoteam.maven.security.SecretKey;

/**
 * Tests the <code>Decrypter</code> component.
 * 
 * @author Karin
 * @see  se.devoteam.maven.crypto.Decrypter
 *
 */
public class DecrypterComponentTest extends PlexusTestCase {
	
	/**
	 * The properites are loaded from the 
	 * <code>test.settings.properties</code> file.
	 */
	private static final Properties settings = new Properties();
	
	private SettingsDecrypter decrypter = null;
	
	public DecrypterComponentTest() {
		
	}

	/**
	 * Loads the <code>test.settings.properties</code> and retrieves the decrypter instance
	 * from the plexus container.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();		
		InputStream is = null;
		try {
			is = getClass().getResourceAsStream("/test.settings.properties");
			if (is == null) {
				is = getClass().getResourceAsStream("test.settings.properties");
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
		
		//make sure the components are exits in the container
		Assert.assertNotNull(getContainer().getComponentDescriptor(SecDispatcher.class.getName(),"devoteamSecDisp"));
		Assert.assertNotNull(getContainer().getComponentDescriptor(SecretKey.class.getName(), "testKey"));
		//SettingsDecrypter hint = default
		Assert.assertNotNull(getContainer().getComponentDescriptorList(SettingsDecrypter.class.getName()));
		decrypter = getContainer().lookup(SettingsDecrypter.class);
		Assert.assertTrue(decrypter instanceof Decrypter);
	}

	/**
	 * Test {@link se.devoteam.maven.crypto.Decrypter#decrypt(SettingsDecryptionRequest)}.
	 * <p/>It sends in an empty request to the decrypt method and should get an empty result back.
	 */
	public void testDecryptEmptyRequest() {
		SettingsDecryptionResult result = decrypter.decrypt(DecrypterComponentTestUtil.getInstance().getEmptyRequest());
		Assert.assertNotNull(result);
		Assert.assertTrue(result.getServers().isEmpty());
		Assert.assertTrue(result.getProxies().isEmpty());
		Assert.assertTrue(result.getProblems().isEmpty());	
	}
	
	/**
	 * Test {@link se.devoteam.maven.crypto.Decrypter#decrypt(SettingsDecryptionRequest)}.
	 * <p/>The test provokes an error by trying to decrypt a password not encrypted with the secret key
	 * 
	 * The test is successful if the result object has one problem attached to it.
	 */
	public void testDecryptPasswordProblem() {
		SettingsDecryptionRequest req = DecrypterComponentTestUtil.getInstance().getErrorPassword(settings);
		SettingsDecryptionResult result = decrypter.decrypt(req);
		List<SettingsProblem> problems = result.getProblems();
		Assert.assertTrue(problems.size() == 1);
	}
	
	/**
	 * Test {@link se.devoteam.maven.crypto.Decrypter#decrypt(SettingsDecryptionRequest)}.
	 * <p/>The test is successful if it can decrypt the password found in the <code>test.settings.properties</code> file.
	 * 
	 * @see #getOneServerPasswordEncrypted()
	 */
	public void testDecryptPassword() {
		SettingsDecryptionResult result = decrypter.decrypt(DecrypterComponentTestUtil.getInstance().getOneServerPasswordEncrypted(settings));
		Assert.assertTrue(result.getProblems().isEmpty());
		Assert.assertFalse(result.getServer().getPassword().startsWith(settings.getProperty("encryption.start")));
		Assert.assertFalse(result.getServer().getPassword().endsWith(settings.getProperty("encryption.end")));
		Assert.assertTrue(result.getServer().getPassword().equals(settings.getProperty("password.plain.text")));	
	}
	
	/**
	 * Test {@link se.devoteam.maven.crypto.Decrypter#decrypt(SettingsDecryptionRequest)}.
	 * <p/>This test sends in a server object where the server properties are not set.
	 * The test is successful if the decrypter instance ignores the empty server object.
	 */
	public void testPasswordIsNull() {
		SettingsDecryptionResult result = decrypter.decrypt(DecrypterComponentTestUtil.getInstance().getServerPasswordNotSetRequest());
		Assert.assertTrue(result.getProblems().isEmpty());
		Assert.assertNull(result.getServer().getPassword());
		
	}	

}
