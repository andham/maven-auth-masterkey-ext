package se.devoteam.maven;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.plexus.components.cipher.DefaultPlexusCipher;
import org.sonatype.plexus.components.cipher.PlexusCipherException;
import org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.PasswordDecryptor;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcherException;
import org.sonatype.plexus.components.sec.dispatcher.SecUtil;
import org.sonatype.plexus.components.sec.dispatcher.model.SettingsSecurity;

/**
 * This is a temporary solution to make the tests work.
 * Remove this class when the plexus container is in place.
 */
public final class ExtSecDispatcher extends DefaultSecDispatcher {
	
	@Requirement
	SecretKey masterKey;

	@SuppressWarnings("rawtypes")
	public ExtSecDispatcher() {
		super();
		try {
			//TODO: let plexus handle this (_chiper, _decryptors and masterKey)
			super._cipher = new DefaultPlexusCipher();
			super._decryptors = new HashMap();
			masterKey = new MavenExtSecretKey();
		} catch (PlexusCipherException e) {
			e.printStackTrace();
		}
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public String decrypt(String str) throws SecDispatcherException {
		
		String res = str;
		
        if(str != null && _cipher.isEncryptedString(str)) { 
        	final String bare = undecorate(str);
        	Map<String,String> attr = stripAttributes(bare);
    		SettingsSecurity sec = getSec();
    		if (attr == null || attr.get("type") == null) {
    			try {
					res = _cipher.decrypt(bare,getMaster(sec));
				} catch (PlexusCipherException e) {
					throw new SecDispatcherException(e);
				}
    		} else {
    			if (_decryptors != null && _decryptors.size() > 0) {
    				final String type = attr.get(TYPE_ATTR);
					Map conf = SecUtil.getConfig(sec, type);
    				PasswordDecryptor dispatcher = (PasswordDecryptor)_decryptors.get(type);
    				if (dispatcher == null) {
    					throw new SecDispatcherException("no dispatcher for hint " + type);
    				}
    				res = dispatcher.decrypt(strip(bare), attr, conf);
    			}
    			
    			
    		}
        }
        return res;

	}
	
	private String undecorate(final String str) throws SecDispatcherException {
    	String bare = null;
    	try
    	{
    		bare = _cipher.unDecorate(str);
    	}
    	catch ( PlexusCipherException e)
    	{
    		throw new SecDispatcherException( e);
    	}
    	return bare;
	}
	
	private SettingsSecurity getSec()  throws SecDispatcherException {
		//TODO: set all properties on SettingsSecurity
		SettingsSecurity sec = new SettingsSecurity();
		if (masterKey.isSet()) {
			sec.setMaster(masterKey.getKey());
		} else {
			throw new SecDispatcherException("Unable to get hold of the master key");
		}
		return sec;
	}
	
	private String getMaster(SettingsSecurity sec) throws SecDispatcherException  {
		//settins.security is used by the DefaultSecDispatcher implementation
		
		if (sec.getMaster() != null) {
			final String passPharse = "settings.security";
			try {
				return _cipher.decryptDecorated(sec.getMaster(), passPharse);
			} catch (PlexusCipherException e) {
				throw new SecDispatcherException(e);
			}
		} else {
			throw new SecDispatcherException( "master password is not set" );
		}
	}
	
	private Map<String,String> stripAttributes(String str) {
    	
    	final int start = str.indexOf( ATTR_START );
        final int stop = str.indexOf( ATTR_STOP );
    	Map<String,String> result = null;
        
    	if (start > -1 && stop > (start + 1)) {
    		final String attrs = str.substring( start+1, stop ).trim();
    		if (attrs != null && attrs.length() > 0) {
    			final StringTokenizer st = new StringTokenizer( attrs, ", " );
    			if (st.countTokens() > 0) {
	    			result = new HashMap<String,String>(st.countTokens());
	    			while (st.hasMoreTokens()) {
	    				final String pair = st.nextToken();
	                    int pos = pair.indexOf( '=' );
	                    if (pos >= 0) {
	                    	final String key = pair.substring( 0, pos ).trim();
	                    	if(pos == pair.length()) {
	                    		result.put(key, null);
	                    	} else {
	                    		result.put(key, pair.substring(pos+1));
	                    	}
	                    }
	    			}
    			}
    		}
    		
    	}
    	return result;

    }

	
    private String strip( String str ) {
        int pos = str.indexOf( ATTR_STOP );
        
        if(pos == str.length()) {
            return null;
        }
        
        if(pos != -1) {
            return str.substring(pos+1);
        }
        
        return str;
    }
	
}