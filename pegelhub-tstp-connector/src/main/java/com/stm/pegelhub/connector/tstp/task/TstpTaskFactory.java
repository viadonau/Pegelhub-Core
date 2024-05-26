package com.stm.pegelhub.connector.tstp.task;

import com.stm.pegelhub.connector.tstp.communication.TstpCommunicator;
import com.stm.pegelhub.connector.tstp.communication.impl.TstpCommunicatorImpl;
import com.stm.pegelhub.connector.tstp.ConnectorOptions;
import com.stm.pegelhub.connector.tstp.service.impl.TstpBinaryServiceImpl;
import com.stm.pegelhub.connector.tstp.service.impl.TstpXmlServiceImpl;
import com.stm.pegelhub.connector.tstp.service.impl.TstpCatalogServiceImpl;
import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.PegelHubCommunicatorFactory;
import com.stm.pegelhub.lib.internal.ApplicationProperties;
import com.stm.pegelhub.lib.internal.ApplicationPropertiesFactory;
import com.stm.pegelhub.lib.internal.dto.SupplierSendDto;
import com.stm.pegelhub.lib.internal.dto.TakerSendDto;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.TimerTask;

public class TstpTaskFactory {
    public static TimerTask getTstpTask(ConnectorOptions conOpt) throws MalformedURLException {
        PegelHubCommunicator phCommunicator = PegelHubCommunicatorFactory.create(new URL(
                String.format("http://%s:%s/",
                        conOpt.coreAddress().getHostAddress(),
                        conOpt.corePort())), conOpt.propertiesFile());
        ApplicationProperties properties = ApplicationPropertiesFactory.create(conOpt.propertiesFile());
        TstpCommunicator tstpCommunicator = new TstpCommunicatorImpl(
                conOpt.tstpAddress(),
                conOpt.tstpPort(),
                HttpClient.newHttpClient(),
                new TstpXmlServiceImpl(new TstpBinaryServiceImpl()));

        if (properties.isSupplier()) {
            SupplierSendDto supplier = properties.getSupplier();
            int stationId = supplier.stationId();

            return new TstpReader(phCommunicator,
                    tstpCommunicator,
                    conOpt.readDelay(),
                    new TstpCatalogServiceImpl(tstpCommunicator, stationId));
        } else {
            TakerSendDto taker = properties.getTaker();
            int stationId = taker.stationId();

            return new TstpWriter(phCommunicator,
                    tstpCommunicator,
                    conOpt.readDelay().toSeconds()+"s",
                    taker.stationNumber(),
                    new TstpCatalogServiceImpl(tstpCommunicator, stationId));
        }
    }
}
