package com.stm.pegelhub.connector.iec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        ConnectorOptions connectorOptions = ConfigLoader.fromArgs(args);
        LOG.info("Starting IEC Connector...");

        try (IecConnector connector = new IecConnector(connectorOptions)) {
            LOG.info("IEC Connector is running.");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOG.info("Shutdown issued. Connector will close automatically.");
            }));

            Thread.currentThread().join();
        }
    }
}
