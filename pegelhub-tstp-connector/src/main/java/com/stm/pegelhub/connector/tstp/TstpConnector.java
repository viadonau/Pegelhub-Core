package com.stm.pegelhub.connector.tstp;

import com.stm.pegelhub.connector.tstp.xmlparsing.ParserFactory;
import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.PegelHubCommunicatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class TstpConnector implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(TstpConnector.class);

    // some tstp communicator / client

    private final PegelHubCommunicator communicator;
    private final Timer sleepInterval;
    private final TimerTask task;

    /**
     * Configures the Connections as an TSTP Client to the TSTP Server with the given options. Configures the Delay at which the Client should
     * query for new data
     */
    public TstpConnector(ConnectorOptions conOpt) throws IOException {
        // initialize tstp communicator

        communicator = PegelHubCommunicatorFactory.create(new URL(
                String.format("http://%s:%s/",
                        conOpt.coreAddress().getHostAddress(),
                        conOpt.corePort())), conOpt.propertiesFile());

        sleepInterval = new Timer();

        // instead of parser - something with read write idk
        task = new TstpTask(conOpt, communicator, ParserFactory.getParser(conOpt.connectorMode()));
        sleepInterval.scheduleAtFixedRate(task, 0, conOpt.readDelay().toMillis());
    }

    @Override
    public void close() throws Exception {
        task.cancel();
        sleepInterval.cancel();
        communicator.close();
    }
}
