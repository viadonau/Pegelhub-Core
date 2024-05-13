package com.stm.pegelhub.connector.tstp.model;

import java.net.InetAddress;
import java.time.Duration;

public record ConnectorOptions(InetAddress coreAddress, int corePort,
                               String tstpAddress, int tstpPort,
                               String username, String password,
                               String stationNumber, ConnectorMode connectorMode,
                               Duration readDelay, String propertiesFile) {}
