package com.stm.pegelhub.connector.iec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * class for loading the config from a specific file path
 * converts property values into a ConnectorOptions object
 */
public class ConfigLoader {
    private static final String DEFAULT_CONFIG = "/app/files/config.properties";
    private static final Logger LOG = LoggerFactory.getLogger(ConfigLoader.class);


    /**
     * Loads connector configuration from the provided command-line arguments.
     * If no arguments are passed, uses a default configuration path.
     * @param args Command-line arguments
     * @return A ConnectorOptions object
     */
    public static ConnectorOptions fromArgs(String[] args) throws IOException {
        String path=getConfigPath(args);
        Properties props = loadProperties(path);

        LOG.info("Loaded config from: {}", path);
        LOG.debug("Loaded properties:");
        props.forEach((key, value) -> LOG.debug("{} = {}", key, value));

        Duration delay = readDelay(props.getProperty("DelayInterval"));
        Duration cycleTime = readDelay(props.getProperty("TelemetryCycleTime"));
        List<String> stationNumbers = Arrays.stream(props.getProperty("StationNumbers").split(","))
                .map(String::trim).collect(Collectors.toList());

        return new ConnectorOptions(
                props.getProperty("Property.File.Path"),
                Boolean.parseBoolean(getRequiredProperty(props,"Connector.IsReadingFromIec")),
                InetAddress.getByName(getRequiredProperty(props,"Core.IP")),
                Integer.parseInt(getRequiredProperty(props,"Core.Port")),
                InetAddress.getByName(getRequiredProperty(props,"Iec.Host.IP")),
                Integer.parseInt(getRequiredProperty(props,"Iec.Host.Port")),
                Integer.parseInt(getRequiredProperty(props,"Iec.CommonAddress")),
                Integer.parseInt(getRequiredProperty(props,"Iec.StartDtRetries")),
                Integer.parseInt(getRequiredProperty(props,"Iec.ConnectionTimeout")),
                Integer.parseInt(getRequiredProperty(props,"Iec.MessageFragmentTimeout")),
                UUID.fromString(getRequiredProperty(props, "ConnectorIdentifier")),
                delay,
                props.getProperty("DelayInterval"),
                stationNumbers,
                cycleTime
        );
    }

    /**
     * Returns the configuration file path from command-line arguments, or a default path if none is given.
     * @param args Command-line arguments.
     * @return Path to the config file.
     */
    private static String getConfigPath(String[] args) {
        return (args.length > 0) ? args[0] : DEFAULT_CONFIG;
    }

    /**
     * Loads property values from a file at the given path.
     * @param path Path to the property file.
     * @return A Properties object containing all key-value pairs.
     * @throws IOException If the file cannot be opened or read.
     */
    private static Properties loadProperties(String path) throws IOException {
        Properties props = new Properties();
        try (var stream = new FileInputStream(path)) {
            props.load(stream);
        }
        return props;
    }

    /**
     * Parses a delay string into a Duration object.
     * For example: "10s", "5m", "1h"
     * @param delay the delay string in the format "[number][unit]", example: "10s" for 10 seconds
     * @return a Duration representing the parsed delay
     * @throws IllegalArgumentException if the format is invalid or the unit is unknown
     * @throws NumberFormatException if the numeric part is not a valid number
     */
    public static Duration readDelay(String delay) {
        String number = delay.substring(0, delay.length() - 1);
        char unit = delay.charAt(delay.length() - 1);
        return switch (Character.toLowerCase(unit)) {
            case 'h' -> Duration.ofHours(Long.parseLong(number));
            case 'm' -> Duration.ofMinutes(Long.parseLong(number));
            case 's' -> Duration.ofSeconds(Long.parseLong(number));
            default -> throw new IllegalArgumentException("Unknown unit: " + unit);
        };
    }

    /**
     * Checks if a property is missing or empty
     * @param props the Properties object containing key-value pairs
     * @param key the key of the property
     * @return the non-blank value associated with the given key
     * @throws IllegalArgumentException if the key is missing or the value is blank
     */
    private static String getRequiredProperty(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing or empty property: " + key);
        }
        return value;
    }
}
