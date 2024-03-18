package com.stm.pegelhub.connector.iec;

import com.stm.pegelhub.lib.InfluxID;
import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.internal.ApplicationPropertiesImpl;
import com.stm.pegelhub.lib.model.Measurement;
import com.stm.pegelhub.lib.model.Supplier;
import com.stm.pegelhub.lib.model.Telemetry;
import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.Connection;
import org.openmuc.j60870.ConnectionEventListener;
import org.openmuc.j60870.ie.IeScaledValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Event-listener to receive incoming messages from iec server.
 * Incoming ASdu type must be specified in method (public void newASdu(ASdu aSdu)).
 */
public class IecClientEventListener implements ConnectionEventListener {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionEventListener.class);
    private final PegelHubCommunicator communicator;
    private final ConnectorOptions conOpt;
    private Properties props;
    private final ApplicationPropertiesImpl properties;
    private LocalDateTime lastCheck;
    private long ID = 0;
    private InfluxID influxID;
    private boolean IDfetched = false;
    private  Timer telInterval;
    private TimerTask telTask = null;
    private Timer inetAddressCheckTimer;
    private TimerTask inetAddressCheckTask = null;

    private Supplier sup;

    private InetAddress siteLocal;

    private InetAddress publicFacing;

    private Boolean needsRefresh = false;
    private Duration cycleTime;
    private  Connection connection;

    public IecClientEventListener(PegelHubCommunicator communicator, ConnectorOptions conOpt, ApplicationPropertiesImpl properties, Connection connection) {
        this.communicator = communicator;
        this.conOpt = conOpt;
        this.properties = properties;
        this.connection = connection;
        this.cycleTime = conOpt.telemetryCycleTime();
        inetAddressCheckTimer = new Timer();
        this.influxID = new InfluxID(communicator, properties);


        //This task only serves as placeholder. Will be changed as soon as I know where the IP Address comes from and how I should obtain it
        inetAddressCheckTask = new TimerTask() {
            @Override
            public void run() {
                Enumeration<NetworkInterface> enumNetworkInterfaces = null;
                try {
                    enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
                } catch (SocketException ex) {
                    throw new RuntimeException(ex);
                }

                InetAddress newSiteLocal = null;
                InetAddress newPublicFacing = null;
                while(enumNetworkInterfaces.hasMoreElements())
                {
                    NetworkInterface networkInterface = (NetworkInterface) enumNetworkInterfaces.nextElement();
                    Enumeration<InetAddress> enumNetwork = networkInterface.getInetAddresses();
                    while (enumNetwork.hasMoreElements())
                    {
                        InetAddress inetAddress = (InetAddress) enumNetwork.nextElement();
                        if(inetAddress.isSiteLocalAddress())
                        {
                            if(siteLocal == null)
                            {
                                needsRefresh = true;
                                newSiteLocal = inetAddress;
                            }
                            else if(!siteLocal.getHostAddress().equals(inetAddress.getHostAddress()))
                            {
                                needsRefresh = true;
                                newSiteLocal = inetAddress;
                            }
                        }
                        else if(!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && !inetAddress.isMulticastAddress())
                        {
                            if(publicFacing == null)
                            {
                                needsRefresh = true;
                                newPublicFacing = inetAddress;
                            }
                            else if(!publicFacing.getHostAddress().equals(inetAddress.getHostAddress()))
                            {
                                needsRefresh = true;
                                newPublicFacing = inetAddress;
                            }
                        }

                    }
                }

                if(needsRefresh)
                {
                    if(sup != null) {
                        Telemetry tel = new Telemetry();
                        tel.setTimestamp(LocalDateTime.now());
                        tel.setMeasurement(sup.getId());
                        tel.setStationIPAddressIntern(newSiteLocal.getHostAddress());
                        tel.setStationIPAddressExtern(newPublicFacing.getHostAddress());
                        tel.setCycleTime(cycleTime.toMillis());
                        siteLocal = newSiteLocal;
                        publicFacing = newPublicFacing;
                        needsRefresh = false;
                    }
                }
            }
        };
        inetAddressCheckTimer.scheduleAtFixedRate(inetAddressCheckTask, 0, 5000);
    }

    @Override
    public void newASdu(ASdu aSdu) {
        // define sent iec types
        // must be specified with the customer

        Timestamp influxTime = communicator.getSystemTime();

        LocalDateTime checkTime = influxTime.toLocalDateTime();

        Collection<Supplier> sups = communicator.getSuppliers();

        Set<Supplier> fromProperties;

        fromProperties = sups.stream().filter(supplier -> properties.getSupplier().stationNumber().equals(supplier.getStationNumber())).collect(Collectors.toSet());

        Optional<Supplier> optionalWork;
        Supplier work = null;

        telInterval = new Timer();

        if(fromProperties.size() == 1)
        {
            optionalWork = fromProperties.stream().findFirst();
            work = optionalWork.get();
            sup = work;
        }

        UUID supUUID = UUID.fromString(work.getId());

     /*   long seconds = 0;
        seconds = checkTime.getHour()*60*60;
        seconds = seconds + checkTime.getMinute()*60;
        seconds = seconds + checkTime.getSecond();

        if(!IDfetched) {
            HashSet<Long> IDs = communicator.getMeasurementsIDsOfStation(work.getStationNumber(), seconds + "s");

            List<Long> listIDs = new ArrayList<>(IDs.stream().toList());

            if (!listIDs.isEmpty()) {
                Long max = Collections.max(listIDs);
                if (max != 0L) {
                    IDfetched = true;
                    ID = max + 1;
                }
            }
        }
        if(lastCheck != null) {
            if (lastCheck.getYear() < checkTime.getYear())
            {
                ID = 0;
            } else if (lastCheck.getMonth().getValue() < checkTime.getMonth().getValue()) {
                ID = 0;
            } else if (lastCheck.getMonth() == checkTime.getMonth()) {
                if (lastCheck.getDayOfMonth() < checkTime.getDayOfMonth()) {
                    ID = 0;
                }
            }
        }
        lastCheck = checkTime; */
        influxID.calculateID();
        System.out.println("before switch");
        System.out.println(influxID.getIDValue());

        switch (aSdu.getTypeIdentification()) {
            case M_ME_NB_1 -> {
                String measurementID = "";
                List<Measurement> measurements = new ArrayList<>();
                for (int i = 0; i < aSdu.getInformationObjects().length; i++) {
                    var ioa = aSdu.getInformationObjects()[i].getInformationObjectAddress();

                    for (int j = 0; j < aSdu.getInformationObjects()[i].getInformationElements().length; j++) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        OffsetDateTime now = OffsetDateTime.now();
                        var normalizedValue = ((IeScaledValue) aSdu.getInformationObjects()[i].getInformationElements()[j][0]).getNormalizedValue();
                        var unNormalizedValue = ((IeScaledValue) aSdu.getInformationObjects()[i].getInformationElements()[j][0]).getUnnormalizedValue();
                        var quality = aSdu.getInformationObjects()[i].getInformationElements()[j][1].toString();

                        LOG.info("************** Received Data from IEC **********************************************");
                        LOG.info(String.format("ASdu Type:          %s", aSdu.getTypeIdentification().toString()));
                        LOG.info(String.format("ASdu Description:   %s", aSdu.getTypeIdentification().getDescription()));
                        LOG.info(String.format("IOA:                %d", ioa));
                        LOG.info(String.format("NormalizedValue:    %f", normalizedValue));
                        LOG.info(String.format("UnNormalizedValue:  %d", unNormalizedValue));
                        LOG.info(String.format("Quality:            %s", quality));
                        LOG.info("************************************************************************************");

                        // create map and send data to pegelhub
                        var fields = new HashMap<String, Double>();
                        fields.put("NormalizedValue", normalizedValue);

                        // For testing purposes: Each Connector only serves one Supplier. Need to clarify later

                        // TODO: Configure placeholders as soon as I know what to write to them.
                        var infos = new HashMap<String, String>();
                        if(work != null)
                        {
                            infos.put("ChannelUse", work.getChannelUse());
                            infos.put("Type", work.getChannelUse());
                        }
                        else {
                            infos.put("ChannelUse", "Placeholder");
                            infos.put("Type", "Placeholder");
                        }
                        infos.put("Timetype", "Placeholder");
                        infos.put("Error", "Placeholder");
                        infos.put("ID", String.valueOf(influxID.getIDValue()));
                        infos.put("IOA", String.valueOf(ioa));
                        infos.put("UnNormalizedValue", String.valueOf(unNormalizedValue));
                        infos.put("Quality", quality);
                        infos.put("SomeOtherKind", "ofInformation");
                        infos.put("TimestampWithOffset", now.toString());

                        var measurement = new Measurement(fields, infos);
                        measurementID = measurement.getInfos().get("measurement");
                        measurements.add(measurement);
                        influxID.addID();
                    }
                }

                telTask = new TimerTask()
                {
                    public void run()
                    {
                        try {
                            Telemetry test = new Telemetry();
                            test.setCycleTime(Long.valueOf(cycleTime.toMillis()));
                            test.setFieldStrengthTransmission(Double.valueOf(20));
                            test.setPerformanceElectricityBattery(Double.valueOf(20));
                            test.setPerformanceVoltageBattery(Double.valueOf(20));
                            test.setPerformanceElectricitySupply(null);
                            test.setPerformanceVoltageSupply(Double.valueOf(20));
                            test.setStationIPAddressExtern(Inet4Address.getLocalHost().getHostAddress());
                            test.setStationIPAddressIntern(InetAddress.getLocalHost().toString());
                            test.setTemperatureAir(Double.valueOf(20));
                            test.setTemperatureWater(Double.valueOf(20));
                            test.setMeasurement(supUUID.toString());
                            communicator.sendTelemetry(test);
                        } catch (UnknownHostException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
                telInterval.scheduleAtFixedRate(telTask, 0, cycleTime.toMillis());
                    communicator.sendMeasurements(measurements);
            }


            default -> {
                LOG.info("************** Got unhandled ASdu Type *********************************************");
                LOG.info(String.format("ASdu Type:          %s", aSdu.getTypeIdentification().toString()));
                LOG.info(String.format("ASdu Description:   %s", aSdu.getTypeIdentification().getDescription()));
                LOG.info("************************************************************************************");
            }
        }
    }

    @Override
    public void connectionClosed(IOException e) {
        LOG.error(String.format("Connections closed because: %s", e.getMessage()));
    }

    @Override
    public void dataTransferStateChanged(boolean stopped) {
        // monitor data transfer state
        LOG.info(String.format("Data transfer started: %s", stopped ? "stopped" : "started"));
    }
}
