package com.stm.pegelhub.connector.tstp.task.impl;

import com.stm.pegelhub.connector.tstp.communication.TstpCommunicator;
import com.stm.pegelhub.connector.tstp.parsing.model.XmlQueryResponse;
import com.stm.pegelhub.connector.tstp.task.CatalogHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CatalogHandlerImpl implements CatalogHandler {
    private static final Logger LOG = LoggerFactory.getLogger(CatalogHandlerImpl.class);
    private Instant latestRefresh;
    private XmlQueryResponse catalog;
    private final TstpCommunicator communicator;

    public CatalogHandlerImpl(TstpCommunicator communicator) {
        this.communicator = communicator;
        refreshCatalog();
    }

    private void refreshCatalog() {
        catalog = communicator.getCatalog(Instant.now(), Instant.now(), "asdf");
        this.latestRefresh = Instant.now();
    }

    @Override
    public String getZrid() {
        LOG.info("getting ZRID");
        if (!isCatalogInSync()) {
            LOG.info("Catalog out of sync");
            LOG.info("latest refresh: "+this.latestRefresh.toString());
            refreshCatalog();
            LOG.info("updated latest refresh: "+this.latestRefresh.toString());
        }

        return catalog.getDef().get(0).getZrid();
    }

    private boolean isCatalogInSync() {
        return latestRefresh.isAfter(Instant.now().minus(5, ChronoUnit.SECONDS));
    }
}
