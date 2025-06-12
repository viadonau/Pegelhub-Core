package com.stm.pegelhub.connector.iec;

import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.internal.ApplicationPropertiesImpl;
import com.stm.pegelhub.lib.model.Supplier;
import com.stm.pegelhub.lib.model.Telemetry;
import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.Connection;
import org.openmuc.j60870.ConnectionEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class combines the logic for incoming data and scheduled telemetry reporting in a single component.
 * It has the following functionality:
 * Listens to incoming IEC 60870-5-104 ASdu messages via ConnectionEventListener
 * Delegates measurement processing to AsduMeasurementHandler
 * Periodically sends telemetry data to the Pegelhub Core
 */
public class IecClientEventListener implements ConnectionEventListener {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionEventListener.class);

    private final AsduMeasurementHandler measurementHandler;
    private final PegelHubCommunicator communicator;
    private final ConnectorOptions options;
    private final ApplicationPropertiesImpl properties;
    private final Timer timer = new Timer();

    public IecClientEventListener(PegelHubCommunicator communicator,
                                  ConnectorOptions conOpt,
                                  ApplicationPropertiesImpl properties,
                                  Connection connection) {

        this.communicator = communicator;
        this.options = conOpt;
        this.properties = properties;

        this.measurementHandler = new AsduMeasurementHandler(communicator, conOpt, properties);
        startSending();
    }

    /**
     * Handles incoming ASdu messages and delegates processing.
     * @param aSdu the received ASdu
     */
    @Override
    public void newASdu(ASdu aSdu) {
        measurementHandler.processByType(aSdu);
    }

    /**
     * This method is Called when the IEC connection is closed.
     * @param e the exception causing the closure
     */
    @Override
    public void connectionClosed(IOException e) {
        LOG.error("Connection closed: {}", e.getMessage());
    }

    /**
     * Called when the IEC data transfer state changes.
     * @param stopped true if data transfer stopped, false if started
     */
    @Override
    public void dataTransferStateChanged(boolean stopped) {
        LOG.info("Data transfer {}", stopped ? "stopped" : "started");
    }

    /**
     * Starts periodic telemetry data transmission to the core.
     */
    public void startSending() {
        if (properties.getSupplier() != null) {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    sendTelemetry();
                }
            }, 0, options.telemetryCycleTime().toMillis());
            LOG.info("Telemetry sending activated, configured as supplier.");
        } else {
            LOG.warn("Telemetry sending skipped: Configured as Taker.");
        }
    }

    /**
     * Collects and sends telemetry information to the Pegelhub Core.
     */
    void sendTelemetry() {
        LOG.info("********* Start sending telemetry data *********");
        Supplier sup = communicator.getSuppliers().stream()
                .filter(s -> s.getStationNumber().equals(properties.getSupplier().stationNumber()))
                .findFirst().orElse(null);

        if (sup == null) {
            LOG.warn("No supplier found for station number: {}", properties.getSupplier().stationNumber());
            return;
        }
        String supplierId = sup.getId();
        LOG.debug("Fetching telemetry for supplier ID: {}", supplierId);

        Duration cycleTimeDuration = options.telemetryCycleTime();
        long minutes = cycleTimeDuration.toMinutes();
        String timespan = minutes + "m";
        LOG.debug("Calculated timespan for getTelemetry: {}", timespan);

        Telemetry telemetryToSend = new Telemetry();
        telemetryToSend.setMeasurement(sup.getId());
        telemetryToSend.setCycleTime(options.telemetryCycleTime().toMillis());
        try {
            telemetryToSend.setStationIPAddressExtern(options.coreAddress().getHostAddress());
            telemetryToSend.setStationIPAddressIntern(InetAddress.getLocalHost().toString());
        } catch (UnknownHostException e) {
            LOG.error("Error getting host address: {}", e.getMessage());
        }
        telemetryToSend.setTimestamp(LocalDateTime.now());

        LOG.info("Sending telemetry data: {}", telemetryToSend);
        communicator.sendTelemetry(telemetryToSend);
    }
}
