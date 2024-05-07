package com.stm.pegelhub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


/**
 * Entry point of the ICC Connector.
 * Reads the 2 endpoints from the config file + interval in which data for selected suppliers is fetched from
 * supplying PH (source) to receiving PH (sink).
 */
public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final String CONFIG = "/app/files/config.properties";
    private static IccConnector _icc;

    private static Duration readDelay(String delay) {
            String number = delay.substring(0, delay.length() - 1);
            char unit = delay.charAt(delay.length() - 1);
            return switch (unit) {
                case 'h', 'H' -> Duration.ofHours(Long.parseLong(number));
                case 'm', 'M' -> Duration.ofMinutes(Long.parseLong(number));
                case 's', 'S' -> Duration.ofSeconds(Long.parseLong(number));
                default -> throw new IllegalArgumentException(String.format("Unknown unit type for time: %c", unit));
            };
    }

    public static void main(String[] args) throws Exception {

        Properties props = getProperties(getConfigPath(args));

        String sourceUrl = (String)props.get("Core.Source");
        String sinkUrl = (String)props.get("Core.Sink");
        String sourceStationNumbers = (String)props.get("Icc.SourceStationNumber");
        String refreshInterval = (String)props.get("Icc.RefreshInterval");
        Duration delay = readDelay(refreshInterval);

        LOG.info("SinkUrl: " + sourceUrl);
        LOG.info("SourceUrl: " + sinkUrl);
        LOG.info("SourceStationNumbers: " + sourceStationNumbers);
        LOG.info("Interval: " + delay);

        List<String> sourceStationNumbersList = Arrays.stream(sourceStationNumbers.split(",")).toList();

        try {
            _icc = new IccConnector(
                    new URL(sourceUrl), "/app/files/source_properties.yaml",
                    new URL(sinkUrl), "/app/files/sink_properties.yaml",
                    sourceStationNumbersList, delay, refreshInterval
            );
        } catch (Exception ex) {
            LOG.error("Creation of ICC Connector failed");
        }

        if (_icc != null) {
            // Graceful shutdown
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOG.info("Shutdown issued. Stopping connector...");
                try {
                    _icc.close();
                } catch (Exception e) {
                    LOG.error("Couldn't shutdown!");
                    throw new RuntimeException(e);
                }
                LOG.info("OK");
            }));
        }
    }

    /**
     * Returns all stored properties from provided property file.
     * @param path Path to the property file.
     * @return Properties.
     * @throws IOException If file does not exist.
     */
    private static Properties getProperties(String path) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(path));
        return props;
    }

    /**
     * Returns path to config file from provided command line args or the fallback path.
     * @param args Command line arguments.
     * @return Path to the config file.
     */
    private static String getConfigPath(String[] args) {
        if(args.length > 0)
            LOG.info("Found config");

        return args.length > 0 ? args[0] : CONFIG;
    }
}
