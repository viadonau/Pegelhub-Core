package org.example;

import com.stm.pegelhub.lib.PegelHubCommunicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLOutput;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;


public class Main {

    static
    {
        System.loadLibrary("RevPiReader");
    }
    private static ConnectorOptions conOpts;
    private static Connector connector;
    private static final String CONFIG = "/home/pi/mAConnectorFull/Connector4...20mA/src/main/resources/config.properties";
    public static void main(String[] args)
    {
        try {
           conOpts = readArguments(args);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            connector = new Connector(conOpts);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        try {
            connector.close();
        } catch (Exception e)
        {
            throw new RuntimeException();
        }
    }));
    }

    public static ConnectorOptions readArguments(String[] args) throws IOException {
        Properties props = getProperties(getConfigPath(args));

        String core_ip                      = (String)props.get("Core.IP");
        String core_port                    = (String)props.get("Core.Port");


        String delayInterval = (String)props.get("DelayInterval");

        String cycleTime = (String)props.get("TelemetryCycleTime");
        Duration delay = readDelay(delayInterval);
        Duration telemetryCycleTime = readDelay(cycleTime);

        String propertiesFile = (String)props.get("Property.File.Path");

        String inputOnRevpi = (String)props.get("Revpi.Input.Offset");

        return new ConnectorOptions(core_ip,
                                    core_port,
                                    delay,
                                    propertiesFile, telemetryCycleTime, inputOnRevpi);
    }

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
    private static Properties getProperties(String path) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(path));
        return props;
    }

    private static String getConfigPath(String[] args) {
        if(args.length > 0)
        {
            System.out.println("config found");
        }

        return args.length > 0 ? args[0] : CONFIG;
    }
}
