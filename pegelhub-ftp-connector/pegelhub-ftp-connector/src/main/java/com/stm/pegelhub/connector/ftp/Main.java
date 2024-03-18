package com.stm.pegelhub.connector.ftp;

import com.stm.pegelhub.connector.ftp.fileparsing.ParserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * parses the given arguments to the needes properties to connect to the FTP server
     * @param args
     * @return
     * @throws UnknownHostException
     */
    private static ConnectorOptions readArguments(String[] args) throws UnknownHostException {
        if (args.length < 7 || args.length > 10) {
            LOGGER.error("Wrong amount of arguments specified!");
            LOGGER.error("java -jar FTPConnector.jar <core IP> <core port> <FTP IP> <FTP port> <username> <password> <path> [<parser type>] [<readDuration h/m/s>] [<properties file>]");
            throw new IllegalArgumentException("Wrong amount of arguments specified!");
        }
        ParserType parserType = ParserType.valueOfName(args[7]);
        if (parserType == null) {
            parserType = ParserType.ASC;
        }
        Duration readDelay = getReadDelay(args);
        String propertiesFile = null;
        if (args.length == 10) {
            propertiesFile = args[9];
        }

        return new ConnectorOptions(InetAddress.getByName(args[0]), Integer.parseInt(args[1]),
                InetAddress.getByName(args[2]), Integer.parseInt(args[3]),
                args[4], args[5],
                args[6], parserType,
                readDelay, propertiesFile);
    }

    private static Duration getReadDelay(String[] args) {
        Duration readDelay;
        if (args.length >= 9) {
            String number = args[8].substring(0, args[8].length() - 1);
            char unit = args[8].charAt(args[8].length() - 1);
            readDelay = switch (unit) {
                case 'h', 'H' -> Duration.ofHours(Long.parseLong(number));
                case 'm', 'M' -> Duration.ofMinutes(Long.parseLong(number));
                case 's', 'S' -> Duration.ofSeconds(Long.parseLong(number));
                default -> throw new IllegalArgumentException(String.format("Unknown unit type for time: %c", unit));
            };
        } else {
            readDelay = Duration.ofHours(2);
        }
        return readDelay;
    }

    /**
     * Main method. Initiates the connection to the FTP Server
     */
    public static void main(String[] args) throws Exception {
        var connOpts = readArguments(args);
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
}
