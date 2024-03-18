package com.stm.pegelhub.connector.iec;

import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.PegelHubCommunicatorFactory;
import com.stm.pegelhub.lib.internal.ApplicationProperties;
import com.stm.pegelhub.lib.internal.ApplicationPropertiesFactory;
import com.stm.pegelhub.lib.internal.ApplicationPropertiesImpl;
import com.stm.pegelhub.lib.model.Connector;
import com.stm.pegelhub.lib.model.Contact;
import com.stm.pegelhub.lib.model.Measurement;
import com.stm.pegelhub.lib.model.Telemetry;
import jdk.jfr.Timespan;
import org.openmuc.j60870.*;
import org.openmuc.j60870.ie.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Opens a connection to configured iec server and sends periodically an interrogation command.
 * An event-listener is registered to receive incoming messages from iec server.
 * ----------------------------------------------------------------------------------------------------
 * Queries periodically measurements for all defined stationNumbers and sends them to the iec server
 */
public class IecConnector implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(IecConnector.class);

    private final PegelHubCommunicator communicator;
    private static Connection connection;
    private final Timer sleepInterval;

    private TimerTask task = null;

    private Boolean isSupplier = false;

    private ApplicationPropertiesImpl properties;


    public IecConnector(ConnectorOptions conOpt) throws IOException {
        communicator = getCommunicator(conOpt);
        connection = getConnection(conOpt);
        properties = new ApplicationPropertiesImpl(conOpt.propertyFileName());
        registerCallback(conOpt);
        sleepInterval = new Timer();
        System.out.println("before tasks");

        if(conOpt.isReadingFromIec()) {
            task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        connection.synchronizeClocks(conOpt.common_address(), new IeTime56(System.currentTimeMillis()));

                        // ------------------------------------------------------
                        // TODO specify communication between iec and connector
                        // Example:
                        connection.interrogation(conOpt.common_address(), CauseOfTransmission.ACTIVATION, new IeQualifierOfInterrogation(20));



                        // ------------------------------------------------------

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };

            sleepInterval.scheduleAtFixedRate(task, 0, conOpt.delay().toMillis());
        } else {
            task = new TimerTask() {
                @Override
                public void run() {
                    LOG.info("****************************************************************************************");
                    LOG.info("Sending measurements to iec server ...");
                    LOG.info("****************************************************************************************");
                    for (String stationNumber: conOpt.stationNumbers()) {
                        try {
                            List<Measurement> measurements = communicator.getMeasurementsOfStation(stationNumber, conOpt.delayString()).stream().toList();
                            for(Measurement m : measurements)
                            {
                                Map<String, Double> fields = m.getFields();
                                List<Map.Entry<String, Double>> values = fields.entrySet().stream().toList();

                                int i = 0;

                                for(Map.Entry<String, Double> v : values)
                                {
                                    i++;
                                    connection.send(new ASdu(ASduType.M_DP_NA_1, false, CauseOfTransmission.INTERROGATED_BY_STATION,
                                            false, false, 0, conOpt.common_address(),
                                            new InformationObject(1,
                                                    new IeShortFloat(v.getValue().floatValue())
                                            )));
                                }
                                Thread.sleep(1000);
                                connection.close();
                                connection = null;
                                connection = getConnection(conOpt);
                            }

                            // ------------------------------------------------------
                            // TODO send measurements to iec server for each stationNumber using the following example statement
                            // TODO map each measurement to InformationElement
                            // Example:
                            // connection.send(new ASdu(ASduType.M_ME_NB_1, true, CauseOfTransmission.INTERROGATED_BY_STATION,
                            //         false, false, 0, conOpt.common_address(),
                            //         new InformationObject(1,
                            //                 new InformationElement[][] {
                            //                         {
                            //                                 new IeScaledValue(#Value#),
                            //                                 new IeQuality(false, false, false, false, false),
                            //                         }
                            //                 }
                            //         )));
                            // ------------------------------------------------------


                        } catch (Exception e) {
                            e.printStackTrace();
                            LOG.error("Error when syncing data for station " + stationNumber + " using iec \n");
                        }
                    }
                }
            };
        }
        sleepInterval.scheduleAtFixedRate(task, 0, 500);
    }

    private void registerCallback(ConnectorOptions conOpt) {
        boolean connected = false;
        int retryCount = 1;


        while (!connected && retryCount <= conOpt.start_dt_retries()) {
            try {
                LOG.info(String.format("Send start DT. Try no. %d", retryCount));
                // handle iec events and receive data from iec
                connection.startDataTransfer(new IecClientEventListener(communicator, conOpt, properties, connection));
            } catch (InterruptedIOException e2) {
                if (retryCount == conOpt.start_dt_retries()) {
                    LOG.error("Starting data transfer timed out. Closing connection. Because of no more retries.");
                    connection.close();
                    return;
                } else {
                    LOG.info("Got Timeout.class Next try.");
                    ++retryCount;
                    continue;
                }
            } catch (IOException e) {
                LOG.error(String.format("Connection closed for the following reason: %s", e.getMessage()));
                return;
            }
            connected = true;
        }
        LOG.info("successfully connected");
    }

    private Connection getConnection(ConnectorOptions conOpt) throws IOException {
        if (connection != null) {
            return connection;
        }

        try {

            ClientConnectionBuilder clientConnectionBuilder = new ClientConnectionBuilder(conOpt.iec_host())
                    .setMessageFragmentTimeout(conOpt.message_fragment_timeout())
                    .setConnectionTimeout(conOpt.connection_timeout())
                    .setPort(conOpt.iec_port());

            return clientConnectionBuilder.build();
        } catch (IOException e) {
            throw new IOException(String.format("Unable to connect to remote host: %s:%d", conOpt.iec_host().toString(), conOpt.iec_port()));
        }
    }

    private PegelHubCommunicator getCommunicator(ConnectorOptions conOpt) throws IOException {
        if (communicator != null) {
            return communicator;
        }
        System.out.println(String.format("http://%s:%s/", conOpt.coreAddress().getHostAddress(), conOpt.corePort()));
        return PegelHubCommunicatorFactory.create(new URL(String.format("http://%s:%s/", conOpt.coreAddress().getHostAddress(), conOpt.corePort())), conOpt.propertyFileName());
    }

    @Override
    public void close() {
        connection.close();
    }
}
