package com.stm.pegelhub.connector.tstp;

import com.stm.pegelhub.connector.tstp.parsing.TstpConfigParser;
import com.stm.pegelhub.connector.tstp.parsing.impl.TstpConfigParserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String TSTP_CONFIG_PATH = "pegelhub-tstp-connector/src/main/resources/config1.properties";
    private static final String CORE_PROPERTIES_PATH = "pegelhub-tstp-connector/properties1.yaml";

    /**
     * Initiates the TSTP Connector
     */
    public static void main(String[] args) throws Exception {
        TstpConfigParser configParser = new TstpConfigParserImpl(TSTP_CONFIG_PATH, CORE_PROPERTIES_PATH);
        var connector = new TstpConnector(configParser);

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
