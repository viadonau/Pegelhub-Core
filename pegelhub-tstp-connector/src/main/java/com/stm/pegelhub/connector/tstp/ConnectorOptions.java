package com.stm.pegelhub.connector.tstp;

import java.net.InetAddress;
import java.time.Duration;

public record ConnectorOptions(InetAddress coreAddress, int corePort,
                               String tstpAddress, int tstpPort,
                               Duration readDelay, String propertiesFile) {}
