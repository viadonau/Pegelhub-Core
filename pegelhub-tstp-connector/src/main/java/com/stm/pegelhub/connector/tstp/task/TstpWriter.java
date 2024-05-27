package com.stm.pegelhub.connector.tstp.task;

import com.stm.pegelhub.connector.tstp.communication.TstpCommunicator;
import com.stm.pegelhub.connector.tstp.service.TstpCatalogService;
import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.model.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TstpWriter extends TimerTask {
    private static final Logger LOG = LoggerFactory.getLogger(TstpWriter.class);
    private final TstpCommunicator tstpCommunicator;
    private final PegelHubCommunicator phCommunicator;
    private final String durationToLookBack;
    private final String stationNumber;
    private final TstpCatalogService tstpCatalogService;

    public TstpWriter(PegelHubCommunicator phCommunicator, TstpCommunicator tstpCommunicator, String durationToLookBack, String stationNumber, TstpCatalogService tstpCatalogService) {
        this.phCommunicator = phCommunicator;
        this.durationToLookBack = durationToLookBack;
        this.tstpCommunicator = tstpCommunicator;
        this.stationNumber = stationNumber;
        this.tstpCatalogService = tstpCatalogService;
    }

    /**
     * The connection to the TSTP Server. Reads the file and tries to parse it. If successful, the parsed Measurements get
     * transferred to pegelhub-core
     */
    @Override
    public void run() {
        try {
            List<Measurement> measurements = (List<Measurement>) phCommunicator.getMeasurementsOfStation(stationNumber, durationToLookBack);
            String zrid = tstpCatalogService.getZrid();

            if (!measurements.isEmpty()) {
                tstpCommunicator.sendMeasurements(zrid, measurements);
            } else {
                LOG.info("Measurement list is empty - nothing was sent");
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
