package com.stm.pegelhub.connector.tstp.parsing;

import com.stm.pegelhub.connector.tstp.model.ConnectorOptions;

import java.io.IOException;

public interface TstpConfigParser {
    ConnectorOptions getConnectorOptions() throws IOException;
}
