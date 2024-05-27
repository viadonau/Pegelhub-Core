package com.stm.pegelhub.connector.tstp;

import com.stm.pegelhub.connector.tstp.service.TstpConfigService;
import com.stm.pegelhub.connector.tstp.service.impl.TstpConfigServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String TSTP_CONFIG_PATH = "/app/files/config.properties";
    private static final String CORE_PROPERTIES_PATH = "/app/files/properties.yaml";

    /**
     * Initiates the TSTP Connector
     */
    public static void main(String[] args) throws Exception {
        TstpConfigService configParser = new TstpConfigServiceImpl(TSTP_CONFIG_PATH, CORE_PROPERTIES_PATH);
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
