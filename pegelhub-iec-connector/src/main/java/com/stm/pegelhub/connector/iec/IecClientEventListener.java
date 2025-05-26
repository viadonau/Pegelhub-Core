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
import java.time.LocalDateTime;
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
        measurementHandler.handle(aSdu);
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
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendTelemetry();
            }
        }, 0, options.telemetryCycleTime().toMillis());
    }

    /**
     * Collects and sends telemetry information to the Pegelhub Core.
     * Uses local IP and default values for battery and environmental sensors.
     */
    void sendTelemetry() {
        Supplier sup = communicator.getSuppliers().stream()
                .filter(s -> s.getStationNumber().equals(properties.getSupplier().stationNumber()))
                .findFirst().orElse(null);

        if (sup == null) return;

        try {
            //this is still hardcoded and needs to be specified
            Telemetry telemetry = new Telemetry();
            telemetry.setTimestamp(LocalDateTime.now());
            telemetry.setMeasurement(sup.getId());
            telemetry.setCycleTime(options.telemetryCycleTime().toMillis());
            telemetry.setStationIPAddressExtern(InetAddress.getLocalHost().getHostAddress());
            telemetry.setStationIPAddressIntern(InetAddress.getLocalHost().toString());
            telemetry.setFieldStrengthTransmission(20.0);
            telemetry.setTemperatureAir(20.0);
            telemetry.setTemperatureWater(20.0);
            telemetry.setPerformanceElectricityBattery(20.0);
            telemetry.setPerformanceVoltageBattery(20.0);
            telemetry.setPerformanceVoltageSupply(20.0);

            communicator.sendTelemetry(telemetry);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
