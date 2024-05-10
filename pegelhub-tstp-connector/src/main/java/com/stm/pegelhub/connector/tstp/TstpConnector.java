package com.stm.pegelhub.connector.tstp;

import com.stm.pegelhub.connector.tstp.model.ConnectorOptions;
import com.stm.pegelhub.connector.tstp.task.TstpTaskFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class TstpConnector implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(TstpConnector.class);
    private final Timer sleepInterval;
    private final TimerTask tstpTask;

    /**
     * Configures the Connections as an TSTP Client to the TSTP Server with the given options. Configures the Delay at which the Client should
     * query for new data
     */
    public TstpConnector(ConnectorOptions conOpt) throws IOException {
        tstpTask = TstpTaskFactory.getTstpTask(conOpt);
        System.out.println("created tstp task");

        sleepInterval = new Timer();
        sleepInterval.scheduleAtFixedRate(tstpTask, 0, conOpt.readDelay().toMillis());
    }

    @Override
    public void close() throws Exception {
        tstpTask.cancel();
        sleepInterval.cancel();
    }
}