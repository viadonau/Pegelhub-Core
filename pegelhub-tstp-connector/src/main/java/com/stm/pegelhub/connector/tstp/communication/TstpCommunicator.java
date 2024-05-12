package com.stm.pegelhub.connector.tstp.communication;

import com.stm.pegelhub.connector.tstp.parsing.model.XmlQueryResponse;
import com.stm.pegelhub.lib.model.Measurement;

import java.time.Instant;
import java.util.List;

public interface TstpCommunicator {
    List<Measurement> getMeasurements(String zrid, Instant readFrom, Instant readUntil, String quality);
    XmlQueryResponse getCatalog(Instant readFrom, Instant readUntil, String quality);
}
