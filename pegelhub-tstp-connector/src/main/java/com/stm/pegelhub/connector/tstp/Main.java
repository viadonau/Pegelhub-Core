package com.stm.pegelhub.connector.tstp;

import com.stm.pegelhub.connector.tstp.xmlparsing.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * parses the given arguments to the needes properties to connect to the TSTP server
     * @param args
     * @return
     * @throws UnknownHostException
     */
    private static ConnectorOptions readArguments(String[] args) throws UnknownHostException {
        if (args.length  != 9) {
            LOGGER.error("Wrong amount of arguments specified!");
            LOGGER.error("java -jar TSTPConnector.jar <core IP> <core port> <TSTP IP> <TSTP port> <username> <password> <method> <readDuration h/m/s> <properties file>");
            throw new IllegalArgumentException("Wrong amount of arguments specified!");
        }

        Duration readDelay = getReadDelay(args);
        Method method = getMethod(args);

        return new ConnectorOptions(InetAddress.getByName(args[0]), Integer.parseInt(args[1]),
                InetAddress.getByName(args[2]), Integer.parseInt(args[3]),
                args[4], args[5],
                method, readDelay, args[8]);
    }

    private static Method getMethod(String[] args) {
        Method foundMethod = Method.valueOfName(args[6]);
        if(foundMethod != null) {
            return foundMethod;
        } else {
            throw new IllegalArgumentException("Wrong method specified!");
        }
    }

    private static Duration getReadDelay(String[] args) {
        Duration readDelay;

        String number = args[7].substring(0, args[7].length() - 1);
        char unit = args[7].charAt(args[7].length() - 1);
        readDelay = switch (unit) {
            case 'h', 'H' -> Duration.ofHours(Long.parseLong(number));
            case 'm', 'M' -> Duration.ofMinutes(Long.parseLong(number));
            case 's', 'S' -> Duration.ofSeconds(Long.parseLong(number));
            default -> throw new IllegalArgumentException(String.format("Unknown unit type for time: %c", unit));
        };

        return readDelay;
    }

    /**
     * Main method. Initiates the connection to the FTP Server
     */
    public static void main(String[] args) throws Exception {
        var connOpts = readArguments(args);
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
}
