package com.stm.pegelhub.connector.tstp.service;

import com.stm.pegelhub.connector.tstp.model.ConnectorOptions;

import java.io.IOException;

/**
 * Handles the parsing of the input parameters for the TSTP-Connector
 */
public interface TstpConfigService {

    /**
     * Parses the given arguments from the config file to the needed properties to instantiate a TSTP-Connector
     *
     * @return the parsed ConnectorOptions
     * @throws IOException if an error occurs while reading the properties
     */
    ConnectorOptions getConnectorOptions() throws IOException;
}
