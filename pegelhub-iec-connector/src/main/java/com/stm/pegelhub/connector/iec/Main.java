package com.stm.pegelhub.connector.iec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final String CONFIG = "W:\\Tech\\ViaDonau\\WehrNussdorf_22182\\Unterlagen\\Pegelhub\\PHv0.3\\pegelhub-iec-connector\\src\\main\\resources\\config.properties";

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

    /**
     * Reads ConnectorOptions at application start from commandline.
     * @param args
     * @return {@code ConnectorOption}
     * @throws UnknownHostException
     */
    public static ConnectorOptions readArguments(String[] args) throws IOException {
        Properties props = getProperties(getConfigPath(args));

        String propertyFilePath = (String)props.get("Property.File.Path");
        boolean isReadingFromIec = Boolean.parseBoolean((String)props.get("Connector.IsReadingFromIec"));

        String core_ip                      = (String)props.get("Core.IP");
        String core_port                    = (String)props.get("Core.Port");
        String iec_ip                       = (String)props.get("Iec.Host.IP");
        String iec_port                     = (String)props.get("Iec.Host.Port");
        String iec_common_address           = (String)props.get("Iec.CommonAddress");
        String iec_start_dt_retries         = (String)props.get("Iec.StartDtRetries");
        String iec_connection_timeout       = (String)props.get("Iec.ConnectionTimeout");
        String iec_message_fragment_timeout = (String)props.get("Iec.MessageFragmentTimeout");
        String telemetryCycleTime          = (String)props.get("TelemetryCycleTime");

        UUID connector_identifier = UUID.fromString((String) props.get("ConnectorIdentifier"));

        String delayInterval = (String)props.get("DelayInterval");

        String stationNumbers = (String)props.get("StationNumbers");

        Duration delay = readDelay(delayInterval);
        Duration cycleTime = readDelay(telemetryCycleTime);
        List<String> supplerStationNumbersList = Arrays.stream(stationNumbers.split(",")).toList();


        return new ConnectorOptions(
                propertyFilePath,
                isReadingFromIec,
                InetAddress.getByName(core_ip), Integer.parseInt(core_port),
                InetAddress.getByName(iec_ip), Integer.parseInt(iec_port),
                Integer.parseInt(iec_common_address),
                Integer.parseInt(iec_start_dt_retries),
                Integer.parseInt(iec_connection_timeout),
                Integer.parseInt(iec_message_fragment_timeout),
                connector_identifier,
                delay,
                delayInterval,
                supplerStationNumbersList,
                cycleTime
        );
    }

    public static void main(String[] args) throws Exception {
        var connOpts = readArguments(args);
        System.out.println("before connector");
        var connector = new IecConnector(connOpts);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOG.info("Shutdown issued. Stopping connector ...");
                try {
                    connector.close();
                } catch (Exception e) {
                    LOG.error("Couldn't shutdown!");
                    throw new RuntimeException(e);
                }
                LOG.info("OK");
            }));

    }
}
