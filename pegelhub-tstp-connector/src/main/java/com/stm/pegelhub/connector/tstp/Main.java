package com.stm.pegelhub.connector.tstp;

import com.stm.pegelhub.connector.tstp.model.ConnectorOptions;
import com.stm.pegelhub.connector.tstp.model.ConnectorMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String TSTP_CONFIG_PATH = "pegelhub-tstp-connector/src/main/resources/config.properties";
    private static final String CORE_PROPERTIES_PATH = "pegelhub-tstp-connector/properties.yaml";


    /**
     * Main method. Initiates the connection to the FTP Server
     */
    public static void main(String[] args) throws Exception {
        var connOpts = getConnectorOptions();
        var connector = new TstpConnector(connOpts);
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

        ConnectorMode mode = ConnectorMode.valueOfName(props.getProperty("connector.mode"));
        if (mode == null) {
            mode = ConnectorMode.READ;
        }

        Duration readDelay = parseReadDelay(props.getProperty("connector.readDelay"));

        List<String> stationNumbers = Arrays.stream(props.getProperty("core.stationNumbers").split(",")).toList();

        return new ConnectorOptions(
                InetAddress.getByName(props.getProperty("core.address")),
                Integer.parseInt(props.getProperty("core.port")),
                props.getProperty("tstp.address"),
                Integer.parseInt(props.getProperty("tstp.port")),
                props.getProperty("tstp.user"),
                props.getProperty("tstp.password"),
                stationNumbers,
                mode,
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
        props.load(new FileInputStream(TSTP_CONFIG_PATH));
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
