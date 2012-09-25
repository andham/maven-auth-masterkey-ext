package se.devoteam.maven;

/*
 * se.devoteam.maven.DecrypterTestUtil
 * 
 * Version:  1.0 
 *
 * Date:     2012-09-25
 * 
 * Copyright: 
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.crypto.SettingsDecryptionRequest;

public class DecrypterTestUtil {

	private static final DecrypterTestUtil ME = new DecrypterTestUtil();
	
	private DecrypterTestUtil() {
		//singleton
	}
	
	public static synchronized DecrypterTestUtil getInstance() {
		return ME;
	}
	
	
	/**
	 * 
	 * @return a new <code>SettingsDecryptionRequest</code> instance that has no
	 * servers or proxies.
	 * Note that the methods <code>setProxies(List)</code> and <code>setServers(List)</code>
	 * throws <code>UnsupportedOperationException</code> since this is an empty request 
	 * (i.e. the server list and proxy lists are empty).
	 */
	public SettingsDecryptionRequest getEmptyRequest() {
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

	/**
	 * Creates a new <code>SettingsDecryptionRequest</code> instance that has one server instance. 
	 * The Server instance where the password is not possible to decrypt.
	 * 
	 * @param settings the test settings
	 * 
	 * @return a new <code>SettingsDecryptionRequest</code> instance that has one server and no proxies
	 * Note that the methods <code>setProxies(List)</code> and <code>setServers(List)</code>
	 * throws <code>UnsupportedOperationException</code>.
	 * 
	 */
	public SettingsDecryptionRequest getErrorPassword(final Properties settings) {
		//error.password
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
				s.setPassword(settings.getProperty("error.password"));
				servers.add(s);
				return servers;
			}
			
		};
	}
	
	/**
	 * Creates a new <code>SettingsDecryptionRequest</code> instance that has one server instance. 
	 * The Server instance properties are defined in the <code>test.settings.properties</code>.
	 * 
	 * @param settings the test settings
	 * 
	 * @return a new <code>SettingsDecryptionRequest</code> instance that has one server and no proxies
	 * Note that the methods <code>setProxies(List)</code> and <code>setServers(List)</code>
	 * throws <code>UnsupportedOperationException</code>.
	 * 
	 */
	public SettingsDecryptionRequest getOneServerPasswordEncrypted(final Properties settings) {
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
	public SettingsDecryptionRequest getServerPasswordNotSetRequest() {
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
	

}
