package com.stm.pegelhub.connector.tstp;

import com.stm.pegelhub.connector.ftp.fileparsing.ParserFactory;
import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.PegelHubCommunicatorFactory;
import org.apache.commons.net.PrintCommandListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

public class TstpConnector implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(TstpConnector.class);

    private final FTPClient ftp;
    private final PegelHubCommunicator communicator;
    private final Timer sleepInterval;
    private final TimerTask task;

    /**
     * Configures the Connections as an FTP Client to the FTP Server with the given options. Configures the Delay at which the Client should
     * query for new data
     */
    public TstpConnector(ConnectorOptions conOpt) throws IOException {
        ftp = new FTPClient();
        ftp.setControlKeepAliveTimeout(Duration.ofMinutes(15));
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(new LogOutputStream(LOG))));
        ftp.setDataTimeout(Duration.ofMinutes(15));
        if (conOpt.propertiesFile() != null) {
            communicator = PegelHubCommunicatorFactory.create(new URL(
                    String.format("http://%s:%s/",
                            conOpt.coreAddress().getHostAddress(),
                            conOpt.corePort())), conOpt.propertiesFile());
        } else {
            communicator = PegelHubCommunicatorFactory.create(new URL(
                    String.format("http://%s:%s/",
                            conOpt.coreAddress().getHostAddress(),
                            conOpt.corePort())));
        }
        sleepInterval = new Timer();
        task = new TstpTask(ftp, conOpt, communicator, ParserFactory.getParser(conOpt.parserType()));
        sleepInterval.scheduleAtFixedRate(task, 0, conOpt.readDelay().toMillis());
    }

    @Override
    public void close() throws Exception {
        task.cancel();
        sleepInterval.cancel();
        communicator.close();
        ftp.disconnect();
    }
}
