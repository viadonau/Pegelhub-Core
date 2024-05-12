package com.stm.pegelhub.connector.tstp.task;

import com.stm.pegelhub.connector.tstp.communication.TstpCommunicator;
import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.model.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

//so far same as reader
public class TstpWriter extends TimerTask {
    private static final Logger LOG = LoggerFactory.getLogger(TstpReader.class);
    private final TstpCommunicator tstpCommunicator;
    private final PegelHubCommunicator phCommunicator;
    private final Duration durationToLookBack;

    public TstpWriter(PegelHubCommunicator phCommunicator, TstpCommunicator tstpCommunicator, Duration durationToLookBack) {
        this.phCommunicator = phCommunicator;
        this.durationToLookBack = durationToLookBack;
        this.tstpCommunicator = tstpCommunicator;
    }

    /**
     * The connection to the FTP Server. Reads the file and tries to parse it. If successful, the parsed Measurements get
     * transferred to pegelhub-core
     */
    @Override
    public void run() {
        try {
            List<Measurement> measurements = tstpCommunicator.getMeasurements("12",getLookBackTimestamp(),Instant.now(),"1");
            System.out.println("parsed measurements");
            if (!measurements.isEmpty()) {
                phCommunicator.sendMeasurements(measurements);
                System.out.println("sent measurements");
            }
        } catch (Exception e) {
            LOG.error("Unhandled Exception was thrown!", e);
        }
    }

    private Instant getLookBackTimestamp() {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.systemDefault());
        return currentTime.minus(durationToLookBack)
                .minus(Duration.of(currentTime.getOffset().getTotalSeconds(), ChronoUnit.SECONDS))
                .toInstant();
    }
}
