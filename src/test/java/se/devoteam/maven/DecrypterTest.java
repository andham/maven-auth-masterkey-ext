package se.devoteam.maven;

/*
 * se.devoteam.mave.DecrypterTest
 * 
 * Version:  1.0 
 *
 * Date:     2012-09-18
 * 
 * Copyright: 
 */

import java.util.Collections;
import java.util.List;

import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.apache.maven.settings.crypto.SettingsDecryptionRequest;
import org.apache.maven.settings.crypto.SettingsDecryptionResult;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Test the {@link se.devoteam.maven.Decrypter}
 * 
 * @author Karin Karlsson
 *
 */
public class DecrypterTest extends TestCase {
	
	
	private SettingsDecryptionRequest emptyRequest;
	
	protected void setUp() throws Exception {
		super.setUp();
		emptyRequest = getEmptyRequest();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test {@link se.devoteam.maven.Decrypter#decrypt(SettingsDecryptionRequest)}.
	 * It sends in an empty request to the decrypt method and should get an empty result back.
	 */
	public void testDecryptEmptyRequest() {
		SettingsDecrypter decrypter = new Decrypter();
		SettingsDecryptionResult result = decrypter.decrypt(emptyRequest);
		Assert.assertNotNull(result);
		Assert.assertTrue(result.getServers().isEmpty());
		Assert.assertTrue(result.getProxies().isEmpty());
		Assert.assertTrue(result.getProblems().isEmpty());	
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
