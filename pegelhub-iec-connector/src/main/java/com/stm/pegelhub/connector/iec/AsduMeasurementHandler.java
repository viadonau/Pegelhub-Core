package com.stm.pegelhub.connector.iec;

import com.stm.pegelhub.lib.InfluxID;
import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.internal.ApplicationPropertiesImpl;
import com.stm.pegelhub.lib.internal.dto.SupplierSendDto;
import com.stm.pegelhub.lib.model.Measurement;
import com.stm.pegelhub.lib.model.Supplier;
import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.ie.IeScaledValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.*;

/**
 * Handler for incoming ASdu messages (specifically M_ME_NB_1) and converts them into measurements
 * to be sent to Pegelhub Core.
 */
public class AsduMeasurementHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AsduMeasurementHandler.class);

    private final PegelHubCommunicator communicator;
    private final ApplicationPropertiesImpl properties;
    private final InfluxID influxID;
    private final boolean isReadingFromIec;

    public AsduMeasurementHandler(PegelHubCommunicator communicator,
                                  ConnectorOptions options,
                                  ApplicationPropertiesImpl properties) {
        this.communicator = communicator;
        this.properties = properties;
        this.isReadingFromIec = options.isReadingFromIec();
        if (isReadingFromIec) {
            this.influxID = new InfluxID(communicator, properties);
        } else {
            this.influxID = null;
        }
    }

    /**
     * Processes the received ASdu based on its type.
     * @param aSdu the received ASdu
     */
    public void handle(ASdu aSdu) {
        switch (aSdu.getTypeIdentification()) {
            case M_ME_NB_1 -> handleMeasurement(aSdu);
            default -> logUnhandledType(aSdu);
        }
    }

    /**
     * Extracts measurement data from ASdu of type M_ME_NB_1 and sends them to Pegelhub.
     * @param aSdu the received ASdu
     */
    /**
     * Extracts measurement data from ASdu of type M_ME_NB_1 and sends them to Pegelhub.
     * @param aSdu the received ASdu
     */
    private void handleMeasurement(ASdu aSdu) {
        List<Measurement> measurements = new ArrayList<>();
        Supplier supplier = findSupplier();
        if (supplier == null) return;

        for (var infoObj : aSdu.getInformationObjects()) {
            int ioa = infoObj.getInformationObjectAddress();
            var elements = infoObj.getInformationElements();

            double value = ((IeScaledValue) elements[0][0]).getNormalizedValue();
            int raw = ((IeScaledValue) elements[0][0]).getUnnormalizedValue();
            String quality = elements[0][1].toString();
            OffsetDateTime now = OffsetDateTime.now();

            Map<String, Double> fields = Map.of("NormalizedValue", value);
            Map<String, String> infos = Map.ofEntries(
                    Map.entry("ChannelUse", supplier.getChannelUse()),
                    Map.entry("Type", supplier.getChannelUse()),
                    Map.entry("Timetype", "TODO"),
                    Map.entry("ID", isReadingFromIec && influxID != null ? String.valueOf(influxID.getIDValue()) : "N/A"),
                    Map.entry("IOA", String.valueOf(ioa)),
                    Map.entry("UnNormalizedValue", String.valueOf(raw)),
                    Map.entry("Quality", quality),
                    Map.entry("TimestampWithOffset", now.toString())
            );

            Measurement measurement = new Measurement(fields, infos);
            measurements.add(measurement);
            if (isReadingFromIec && influxID != null) {
                influxID.addID();
            }
            else {
                LOG.debug("Not in reading mode: Received measurement ASdu");
            }
            LOG.info("Received IEC measurement: IOA={}, Value={}, Quality={}", ioa, value, quality);
        }

        communicator.sendMeasurements(measurements);
    }

    /**
     * Finds the supplier for the current connector.
     * @return the matched Supplier or null if not found
     */
    private Supplier findSupplier() {
        SupplierSendDto configuredSupplier = properties.getSupplier();
        if (configuredSupplier == null || configuredSupplier.stationNumber() == null) {
            LOG.info("Supplier or Station Number not configured");
            return null;
        }

        String configuredStationNumber = configuredSupplier.stationNumber();

        return communicator.getSuppliers().stream()
                .filter(s -> configuredStationNumber.equals(s.getStationNumber()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Logs ASdus that are not handled by the system.
     * @param aSdu the unhandled ASdu
     */
    private void logUnhandledType(ASdu aSdu) {
        LOG.info("Unhandled ASdu Type: {}, Description: {}", aSdu.getTypeIdentification(), aSdu.getTypeIdentification().getDescription());
    }
}

