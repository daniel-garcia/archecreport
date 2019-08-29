package com.infoblox.cto.i2o;

import com.versionone.apiclient.*;
import com.versionone.apiclient.exceptions.V1Exception;
import com.versionone.apiclient.interfaces.IAssetType;
import com.versionone.apiclient.interfaces.IAttributeDefinition;
import com.versionone.apiclient.interfaces.IServices;
import com.versionone.apiclient.services.QueryFind;
import com.versionone.apiclient.services.QueryResult;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;


/**
 * Hello world!
 *
 */
public class App 
{
    private static final String API_TOKEN_OPTION = "k";
    private static final String API_ENDPOINT_OPTION="e";
    private static final String JSON_QUERY_FILE="f";

    public static Options generateOptions() {
        final Option apiToken = Option.builder(API_TOKEN_OPTION)
                .required(true)
                .hasArg()
                .longOpt("api-token")
                .desc("api token to use")
                .build();
        final Option apiEndpoint = Option.builder(API_ENDPOINT_OPTION)
                .required(true)
                .hasArg()
                .longOpt("api-endpoint")
                .desc("api endpoint to use")
                .build();
        final Option jsonQueryFile = Option.builder(JSON_QUERY_FILE)
                .required(true)
                .hasArg()
                .longOpt("json-query-file")
                .desc("file that contains json query")
                .build();
        final Options options = new Options();
        options.addOption(apiToken);
        options.addOption(apiEndpoint);
        options.addOption(jsonQueryFile);
        return options;
    }

    public static CommandLine generateCommandLine(final Options options, final String[]args) {
        final CommandLineParser cmdLineParser = new DefaultParser();
        CommandLine commandLine = null;
        try
        {
            commandLine = cmdLineParser.parse(options, args);
        }
        catch (ParseException parseException)
        {
            System.out.println(
                    "ERROR: Unable to parse command-line arguments "
                            + Arrays.toString(args) + " due to: "
                            + parseException);
            System.exit(1);
        }
        return commandLine;
    }

    private static String readFile(String filePath)
    {
        String content = "";
        try
        {
            content = new String (Files.readAllBytes(Paths.get(filePath) ) );
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        return content;
    }

    public static void main( String[] args ) {

        CommandLine cli = generateCommandLine(generateOptions(), args);

        try {

            String apiToken = cli.getOptionValue(API_TOKEN_OPTION);
            String apiEndpoint = cli.getOptionValue(API_ENDPOINT_OPTION);
            String jsonQueryFile = cli.getOptionValue(JSON_QUERY_FILE);

            String query = readFile(jsonQueryFile);

            V1Connector connector = V1Connector
                    .withInstanceUrl(apiEndpoint)
                    .withUserAgentHeader("cto-i2o-report", "1.0")
                    .withAccessToken(apiToken)
                    .build();

            IServices services = new Services(connector);
            String result = services.executePassThroughQuery(query);
            System.out.println(result);

        } catch (V1Exception e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {

        }

    }
}
