package se.devoteam.maven.client;

import org.apache.commons.cli.ParseException;

/**
 * 
 * 
 * @author Karin Karlsson
 *
 */
public interface ClientComponent {
	
	public Result executeCmd() throws ParseException, ClientException;

}
