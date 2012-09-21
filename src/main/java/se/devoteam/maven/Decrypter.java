/*
 * se.devoteam.maven.Decrypter
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
import org.apache.maven.settings.building.DefaultSettingsProblem;
import org.apache.maven.settings.building.SettingsProblem;
import org.apache.maven.settings.building.SettingsProblem.Severity;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.apache.maven.settings.crypto.SettingsDecryptionRequest;
import org.apache.maven.settings.crypto.SettingsDecryptionResult;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcherException;

/**
 * 
 * @author Karin Karlsson
 * @see #decrypt(SettingsDecryptionRequest)
 *
 */
@Component( role = SettingsDecrypter.class ) //TODO: add this to components.xml
public class Decrypter implements SettingsDecrypter {
	
	//TODO: consider making a org.sonatype.plexus.components.sec.dispatcher.PasswordDecryptor class
	
	//TODO: let plexus container handle this
	@Requirement
	private SecretKey key = new MavenExtSecretKey();
	
	//TODO: let plexus handle this 
	@Requirement( hint = "maven" )
    private SecDispatcher securityDispatcher = new ExtSecDispatcher();

	/**
	 * Decrypt supplied passwords.
	 * @param request the passwords to decrypt
	 * @return all of the decrypted passwords as well as a list of encountered problems (if any)
	 */
	public SettingsDecryptionResult decrypt(SettingsDecryptionRequest request) {
		
		List<Server> clonedServers = cloneServers(request.getServers());
		List<Proxy> clonedProxies = cloneProxies(request.getProxies());
		List<SettingsProblem> problems = new ArrayList<SettingsProblem>();
		
		for (Server clonedServer : clonedServers) {
			
            try {
            	clonedServer.setPassword(decrypt( clonedServer.getPassword()));
            }
            catch (SecDispatcherException e) {
                problems.add( new DefaultSettingsProblem( "Failed to decrypt password for server " + clonedServer.getId()
                    + ": " + e.getMessage(), Severity.ERROR, "server: " + clonedServer.getId(), -1, -1, e ) );
            }
			
		}
		
		for (Proxy clonedProxy : clonedProxies) {
            try {
            	clonedProxy.setPassword( decrypt( clonedProxy.getPassword() ) );
            }
            catch ( SecDispatcherException e ) {
                problems.add( new DefaultSettingsProblem( "Failed to decrypt password for proxy " + clonedProxy.getId()
                    + ": " + e.getMessage(), Severity.ERROR, "proxy: " + clonedProxy.getId(), -1, -1, e ) );
            }
		}
		return getResult(clonedServers,clonedProxies,problems);
	}
	
	
	private List<Server> cloneServers(final List<Server> servers) {
		List<Server> clonedServers = new ArrayList<Server>(servers.size());
		for (Server server : servers) {
			clonedServers.add(server.clone());
		}
		return clonedServers;
	}
	
	private List<Proxy> cloneProxies(final List<Proxy> proxies) {
		List<Proxy> clonedProxies = new ArrayList<Proxy>(proxies.size());
		for (Proxy proxy : proxies ) {
			clonedProxies.add(proxy.clone());
		}
		return clonedProxies;
	}
	
	/**
	 * Decrypts a string. The string must start with a <code>{</code> character and
	 * end with a <code>}</code> in order to be decrypted. 
	 * @param str the string to decrypt. 
	 * @return str decrypted or just str if it dosen't cohere to expected format.
	 * @throws SecDispatcherException decryption error
	 */
	private String decrypt(final String str) throws SecDispatcherException {
		
		if (str != null) {
			return securityDispatcher.decrypt(str);
		}
		return str;
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
