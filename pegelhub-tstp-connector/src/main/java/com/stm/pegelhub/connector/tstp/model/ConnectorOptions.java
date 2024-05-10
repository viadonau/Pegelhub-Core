package com.stm.pegelhub.connector.tstp.model;

import java.net.InetAddress;
import java.time.Duration;
import java.util.List;

public record ConnectorOptions(InetAddress coreAddress, int corePort,
                               String tstpAddress, int tstpPort,
                               String username, String password,
                               List<String> stationNumbers, ConnectorMode connectorMode,
                               Duration readDelay, String propertiesFile) {}
