package se.devoteam.maven;

import junit.framework.Assert;


import org.apache.maven.security.crypto.CryptoException;
import org.codehaus.plexus.PlexusTestCase;

import se.devoteam.maven.crypto.ExtSecretKey;



/**
 * Tests the <code>ExtSecretKey</code> component.
 * 
 * @author Karin Karlsson
 * @see ExtSecretKey
 *
 */
public class ExtSecretKeyTest extends PlexusTestCase {
	

	protected void setUp() throws Exception {
		super.setUp();		
	}
	
	
	protected void tearDown() throws Exception {
	    super.tearDown();
	}
	
	/**
	 * This test is successful if the {@code ExtSecretKey.getValue()} returns an expected value.
	 */
	public void testGetValue() {
	    ExtSecretKey secretKey = new ExtSecretKey();
	    try {
            Assert.assertEquals( "{GiTE7JtdcWUHNZYC+p5P3AZVLDzI7ygb0EOFiFZZ7P0=}", secretKey.getValue() );
        }
        catch ( CryptoException e ) {
            Assert.fail(e.getMessage());
        }
	}

}
