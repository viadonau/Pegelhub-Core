package org.example;

import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.PegelHubCommunicatorFactory;
import com.stm.pegelhub.lib.internal.ApplicationProperties;
import com.stm.pegelhub.lib.internal.ApplicationPropertiesFactory;
import com.stm.pegelhub.lib.model.Supplier;
import com.stm.pegelhub.lib.model.Telemetry;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

public class Connector implements AutoCloseable{

    static
    {
        System.loadLibrary("RevPiReader");
    }
    private Timer taskTimer;
    private ConnectorTask connectorTask;
    private PegelHubCommunicator communicator;
    private ConnectorOptions conOpts;
    private Timer telTimer;
    private TimerTask telTask;

    ApplicationProperties properties;
    public Connector(ConnectorOptions conOpts) throws IOException
    {
        communicator = PegelHubCommunicatorFactory.create(new URL(String.format("http://%s:%s/", conOpts.getCore_ip(), conOpts.getCore_port())), conOpts.getPropertiesFile());
        properties = ApplicationPropertiesFactory.create(conOpts.getPropertiesFile());

        taskTimer = new Timer();
        connectorTask = new ConnectorTask(communicator, conOpts);
        taskTimer.scheduleAtFixedRate(connectorTask, 0, 5000);

        Collection<Supplier> sups = communicator.getSuppliers();

        Set<Supplier> fromProperties;

        fromProperties = sups.stream().filter(supplier -> properties.getSupplier().stationNumber().equals(supplier.getStationNumber())).collect(Collectors.toSet());

        Optional<Supplier> optionalWork;
        Supplier work = null;

        if(fromProperties.size() == 1)
        {
            optionalWork = fromProperties.stream().findFirst();
            work = optionalWork.get();
        }

        UUID supUUID = UUID.fromString(work.getId());

	telTimer = new Timer();

        telTask = new TimerTask() {
            @Override
            public void run() {
                try
                {
                    Telemetry test = new Telemetry();
                    test.setCycleTime(conOpts.getTelemetryCycleTime().toMillis());
                    test.setFieldStrengthTransmission(382.0);
                    test.setPerformanceElectricityBattery(232.0);
                    test.setPerformanceVoltageBattery(325.0);
                    test.setPerformanceElectricitySupply(2432.0);
                    test.setPerformanceVoltageSupply(245565.0);
                    test.setStationIPAddressIntern(Inet4Address.getLocalHost().getHostAddress());
                    test.setStationIPAddressExtern(InetAddress.getLocalHost().toString());
                    test.setTemperatureAir(4.0);
                    test.setTemperatureWater(90.0);
                    test.setMeasurement(supUUID.toString());
                    communicator.sendTelemetry(test);
                }
                catch (UnknownHostException e)
                {
                    throw new RuntimeException(e);
                }
            }
        };

        telTimer.scheduleAtFixedRate(telTask, 0, conOpts.getTelemetryCycleTime().toMillis());
    }
    @Override
    public void close() throws Exception {
        System.exit(1);
    }
}
