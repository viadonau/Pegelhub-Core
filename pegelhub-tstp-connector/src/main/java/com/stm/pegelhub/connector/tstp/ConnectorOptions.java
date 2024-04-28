package com.stm.pegelhub.connector.tstp;

import com.stm.pegelhub.connector.tstp.xmlparsing.Method;

import java.net.InetAddress;
import java.time.Duration;

public record ConnectorOptions(InetAddress coreAddress, int corePort,
                               InetAddress tstpAddress, int tstpPort,
                               String username, String password,
                               Method method, Duration readDelay,
                               String propertiesFile) {}
