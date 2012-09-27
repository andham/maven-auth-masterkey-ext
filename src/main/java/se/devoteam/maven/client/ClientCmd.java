package se.devoteam.maven.client;

import java.util.List;

public interface ClientCmd {
	
	public String getName();
	
	public String getLongName();
	
	public String getDescription();
	
	@SuppressWarnings("rawtypes")
	public Result execute(List values) throws ClientException;

}
