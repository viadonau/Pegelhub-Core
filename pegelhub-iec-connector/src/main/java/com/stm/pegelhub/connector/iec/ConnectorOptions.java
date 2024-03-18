package com.stm.pegelhub.connector.iec;

import java.net.InetAddress;
import java.time.Duration;
import java.util.List;
import java.util.UUID;


/**
 * ConnectorOptions spec.
 */
public record ConnectorOptions(
        String propertyFileName,
        boolean isReadingFromIec,
        InetAddress coreAddress, int corePort,
        InetAddress iec_host, int iec_port,
        int common_address,
        int start_dt_retries,
        int connection_timeout,
        int message_fragment_timeout,
        UUID connectorUUID,
        Duration delay,
        String delayString,
        List<String> stationNumbers,
        Duration telemetryCycleTime
        ) {
}
