package com.stm.pegelhub.connector.tstp.task;

import com.stm.pegelhub.connector.tstp.communication.TstpCommunicator;
import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.model.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

//so far same as reader
public class TstpWriter extends TimerTask {
    private static final Logger LOG = LoggerFactory.getLogger(TstpWriter.class);
    private final TstpCommunicator tstpCommunicator;
    private final PegelHubCommunicator phCommunicator;
    private final String durationToLookBack;
    private final String stationNumber;
    private final CatalogHandler catalogHandler;

    public TstpWriter(PegelHubCommunicator phCommunicator, TstpCommunicator tstpCommunicator, String durationToLookBack, String stationNumber, CatalogHandler catalogHandler) {
        this.phCommunicator = phCommunicator;
        this.durationToLookBack = durationToLookBack;
        this.tstpCommunicator = tstpCommunicator;
        this.stationNumber = stationNumber;
        this.catalogHandler = catalogHandler;
    }

    /**
     * The connection to the FTP Server. Reads the file and tries to parse it. If successful, the parsed Measurements get
     * transferred to pegelhub-core
     */
    @Override
    public void run() {
        try {
            //change to durationToLookBack
            List<Measurement> measurements = (List<Measurement>) phCommunicator.getMeasurementsOfStation(stationNumber, "2h");
            LOG.info("Fetched measurements from PH Core");

            String zrid = catalogHandler.getZrid();
            if (!measurements.isEmpty()) {
                tstpCommunicator.sendMeasurements(zrid, measurements);
                LOG.info("Sent measurements to tstp-server");
            }
        } catch (Exception e) {
            LOG.error("Unhandled Exception was thrown!", e);
        }
    }

    @Override
    public boolean cancel() {
        try {
            phCommunicator.close();
            return super.cancel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
