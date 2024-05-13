package com.stm.pegelhub.connector.tstp.task;

import com.stm.pegelhub.connector.tstp.communication.TstpCommunicator;
import com.stm.pegelhub.connector.tstp.communication.impl.TstpCommunicatorImpl;
import com.stm.pegelhub.connector.tstp.model.ConnectorOptions;
import com.stm.pegelhub.connector.tstp.task.impl.CatalogHandlerImpl;
import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.PegelHubCommunicatorFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.TimerTask;

public class TstpTaskFactory {
    public static TimerTask getTstpTask(ConnectorOptions conOpt) throws MalformedURLException {
        PegelHubCommunicator phCommunicator = PegelHubCommunicatorFactory.create(new URL(
                String.format("http://%s:%s/",
                        conOpt.coreAddress().getHostAddress(),
                        conOpt.corePort())), conOpt.propertiesFile());

        TstpCommunicator tstpCommunicator = new TstpCommunicatorImpl(
                conOpt.tstpAddress(),
                conOpt.tstpPort(),
                conOpt.username()+conOpt.password());

        return switch (conOpt.connectorMode()) {
            case READ -> new TstpReader(phCommunicator,
                    tstpCommunicator,
                    conOpt.readDelay(),
                    new CatalogHandlerImpl(tstpCommunicator));
            case WRITE -> new TstpWriter(phCommunicator,
                    tstpCommunicator,
                    conOpt.readDelay().toSeconds()+"s",
                    conOpt.stationNumber(),
                    new CatalogHandlerImpl(tstpCommunicator));
        };
    }
}
