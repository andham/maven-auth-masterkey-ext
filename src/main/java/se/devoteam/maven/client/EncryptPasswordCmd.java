package se.devoteam.maven.client;

import java.util.List;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import se.devoteam.maven.crypto.EncryptException;
import se.devoteam.maven.crypto.Encrypter;

@Component(role = ClientCmd.class)
public class EncryptPasswordCmd implements ClientCmd {
	
	@Requirement
	private Encrypter enc;

	public String getName() {
		return "extep";
	}

	public String getLongName() {
		return "encrypt-password";
	}

	public String getDescription() {
		return "Encrypt server password";
	}

	@SuppressWarnings("rawtypes") 
	public Result execute(final List values) throws ClientException {
		if (values.size() == 1) {
			return new Result() {
				public String getValue() throws ClientException {
					try {
						return enc.encryptAndDecorate((String)values.get(0));
					} catch (EncryptException e) {
						throw new ClientException(e);
					}
				}
			};
		} else {
			throw new ClientException("Un-recognized command!");
		}
		
	}

}
