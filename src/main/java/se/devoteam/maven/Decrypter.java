/*
 * se.devoteam.mave.Decrypter
 * 
 * Version:  1.0 
 *
 * Date:     2012-09-18
 * 
 * Copyright: 
 */
package se.devoteam.maven;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.building.SettingsProblem;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.apache.maven.settings.crypto.SettingsDecryptionRequest;
import org.apache.maven.settings.crypto.SettingsDecryptionResult;

/**
 * 
 * @author Karin Karlsson
 * @see #decrypt(SettingsDecryptionRequest)
 *
 */
public class Decrypter implements SettingsDecrypter {
	
	/**
	 * Decrypt supplied passwords.
	 * @param request the passwords to decrypt
	 * @return all of the decrypted passwords
	 */
	public SettingsDecryptionResult decrypt(SettingsDecryptionRequest request) {
		
		List<Server> servers = new ArrayList<Server>(request.getServers().size());
		List<Proxy> proxies = new ArrayList<Proxy>(request.getProxies().size());
		List<SettingsProblem> problems = new ArrayList<SettingsProblem>();
		
		for (Server server : request.getServers()) {
			servers.add(server.clone());
		}
		
		for (Proxy proxy : request.getProxies()) {
			proxies.add(proxy.clone());
		}
		return getResult(servers,proxies,problems);
	}
	
	
	/**
	 * Creates a new <code>SettingsDecryptionResult</code> instance.
	 * @param servers the list of servers to assign to the new <code>SettingsDecryptionResult</code> instance
	 * @param proxies the list of proxies to assign to the new <code>SettingsDecryptionResult</code> instance
	 * @param problems the list of problems to assign to the new <code>SettingsDecryptionResult</code> instance
	 * @return new <code>SettingsDecryptionResult</code> instance
	 */
	private SettingsDecryptionResult getResult(final List<Server> servers, final List<Proxy> proxies, final List<SettingsProblem> problems) {
		return new SettingsDecryptionResult() {

			public List<SettingsProblem> getProblems() {
				return problems;
			}

			public List<Proxy> getProxies() {
				return proxies;
			}

			public Proxy getProxy() {
				 return proxies.isEmpty() ? null : proxies.get( 0 );
			}

			public Server getServer() {
				 return servers.isEmpty() ? null : servers.get( 0 );
			}

			public List<Server> getServers() {
				return servers;
			}
			
		};
	}

}
