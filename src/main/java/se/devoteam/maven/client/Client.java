package se.devoteam.maven.client;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

@Component(role = ClientComponent.class)
public class Client implements ClientComponent {
	
	
	private final Options cmdOpts = new Options();
	private final String [] argsList;
	
	@Requirement
	private List<ClientCmd> commands;


	private Client(final String [] args) {
		argsList = args;
		createOptions();
	}
	
	public Options getCmdOpts() {
		return cmdOpts;
	}
	
	public Result executeCmd() throws ParseException, ClientException {
		Parser gnuParser = new GnuParser();
		CommandLine cmdLine = gnuParser.parse(getCmdOpts(), argsList);
		Option[] opts = cmdLine.getOptions();
		if (opts.length == 0 || opts.length > 1) {
			throw new UnsupportedOperationException(argsList.toString());
		}
		
		final String currentArgName = opts[0].getArgName();
		@SuppressWarnings("rawtypes")
		final List values = opts[0].getValuesList();
		
		
		for (ClientCmd cmd : commands) {
			if (currentArgName.equals(cmd.getName())) {
				return cmd.execute(values);
			}
		}
		
		throw new ClientException("No command to exeucute.");
		
	}
	
	private void createOptions() {
		for (ClientCmd cmd : commands) {
			OptionBuilder.withLongOpt(cmd.getLongName());
			OptionBuilder.hasArg();
			OptionBuilder.withDescription(cmd.getDescription());
			cmdOpts.addOption(OptionBuilder.create(cmd.getName()));
		}
	}
	
	//TODO: create a plexus container and test

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client client = new Client(args);
		try {
			Result result = client.executeCmd();
			System.out.println(result.getValue());
			System.exit(1);
		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (ClientException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

}
