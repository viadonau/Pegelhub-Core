package com.stm.pegelhub.connector.ftp;

import com.stm.pegelhub.connector.ftp.fileparsing.ParserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.util.Properties;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String FTP_CONFIG_PATH = "/app/files/config.properties";
    private static final String CORE_PROPERTIES_PATH = "/app/files/properties.yaml";


    /**
     * Main method. Initiates the connection to the FTP Server
     */
    public static void main(String[] args) throws Exception {
        var connOpts = getConnectorOptions();
        var connector = new FtpConnector(connOpts);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Shutdown issued. Stopping connector...");
            try {
                connector.close();
            } catch (Exception e) {
                LOGGER.error("Couldn't shutdown!");
                throw new RuntimeException(e);
            }
            LOGGER.info("OK");
        }));
    }


    /**
     * Parses the given arguments from the config file to the needed properties to connect to the FTP server
     *
     * @return the parsed ConnectorOptions
     * @throws IOException if an error occurs while reading the properties
     */
    private static ConnectorOptions getConnectorOptions() throws IOException {
        Properties props = getProperties();

        ParserType parserType = ParserType.valueOfName(props.getProperty("parser.type"));
        if (parserType == null) {
            parserType = ParserType.ASC;
        }

        Duration readDelay = parseReadDelay(props.getProperty("read.delay"));

        return new ConnectorOptions(
                InetAddress.getByName(props.getProperty("core.address")),
                Integer.parseInt(props.getProperty("core.port")),
                InetAddress.getByName(props.getProperty("ftp.address")),
                Integer.parseInt(props.getProperty("ftp.port")),
                props.getProperty("ftp.user"),
                props.getProperty("ftp.password"),
                props.getProperty("ftp.path"),
                parserType,
                readDelay,
                CORE_PROPERTIES_PATH
        );
    }

    /**
     * Returns all stored properties from provided property file.
     *
     * @return Properties.
     * @throws IOException If file does not exist.
     */
    private static Properties getProperties() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(FTP_CONFIG_PATH));
        return props;
    }

    /**
     * Parses the string to a Duration Object if the format is correct
     *
     * @param delayToParse the string to parse
     * @return the parsed Duration
     */
    private static Duration parseReadDelay(String delayToParse) {
        if (delayToParse.isEmpty()) {
            return Duration.ofHours(2);
        }

        String delayDuration = delayToParse.substring(0, delayToParse.length() - 1);
        char unit = delayToParse.charAt(delayToParse.length() - 1);

        return switch (unit) {
            case 'h', 'H' -> Duration.ofHours(Long.parseLong(delayDuration));
            case 'm', 'M' -> Duration.ofMinutes(Long.parseLong(delayDuration));
            case 's', 'S' -> Duration.ofSeconds(Long.parseLong(delayDuration));
            default -> throw new IllegalArgumentException(String.format("Unknown unit type for time: %c", unit));
        };
    }
}
