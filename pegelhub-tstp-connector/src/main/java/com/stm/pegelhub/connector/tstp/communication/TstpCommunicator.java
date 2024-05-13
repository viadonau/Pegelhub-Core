package com.stm.pegelhub.connector.tstp.communication;

import com.stm.pegelhub.connector.tstp.parsing.model.XmlQueryResponse;
import com.stm.pegelhub.lib.model.Measurement;

import java.time.Instant;
import java.util.List;

/**
 * Handles the communication with the TSTP-Server
 */
public interface TstpCommunicator {
    /**
     * Get measurements from the TSTP-Server
     *
     * @param zrid the ZRID to identify the time series
     * @param readFrom the start point to read entries
     * @param readUntil the endpoint to stop reading entries
     * @param quality the quality of the time series
     * @return a list of measurements returned from the TSTP-Server
     */
    List<Measurement> getMeasurements(String zrid, Instant readFrom, Instant readUntil, String quality);

    /**
     * Get the ZRID Catalog from the TSTP-Server - needs the proper logic ones the real tstp server is available
     *
     * @return the catalog returned from the TSTP-Server
     */
    XmlQueryResponse getCatalog();

    /**
     * Send a list of measurements to the TSTP-Server
     *
     * @param zrid the ZRID of the time series to write the measurements to
     * @param measurements the measurements to send
     */
    void sendMeasurements(String zrid, List<Measurement> measurements);
}
