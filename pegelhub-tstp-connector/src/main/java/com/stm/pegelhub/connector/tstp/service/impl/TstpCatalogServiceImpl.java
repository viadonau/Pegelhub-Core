package com.stm.pegelhub.connector.tstp.service.impl;

import com.stm.pegelhub.connector.tstp.communication.TstpCommunicator;
import com.stm.pegelhub.connector.tstp.service.model.XmlQueryResponse;
import com.stm.pegelhub.connector.tstp.service.TstpCatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TstpCatalogServiceImpl implements TstpCatalogService {
    private static final Logger LOG = LoggerFactory.getLogger(TstpCatalogServiceImpl.class);
    private Instant latestRefresh;
    private XmlQueryResponse catalog;
    private final TstpCommunicator communicator;

    public TstpCatalogServiceImpl(TstpCommunicator communicator) {
        this.communicator = communicator;
        refreshCatalog();
    }

    private void refreshCatalog() {
        catalog = communicator.getCatalog();
        this.latestRefresh = Instant.now();
    }

    @Override
    public String getZrid() {
        LOG.info("getting ZRID");
        if (!isCatalogInSync()) {
            LOG.info("Catalog out of sync");
            refreshCatalog();
        }
        if(this.catalog == null){
            return "";
        }
        LOG.info("ZRID from catalog:" + catalog.getDef().get(0).getZrid());
        return catalog.getDef().get(0).getZrid();
    }

    private boolean isCatalogInSync() {
        return latestRefresh.isAfter(Instant.now().minus(5, ChronoUnit.SECONDS));
    }
}
