package com.stm.pegelhub.connector.tstp.service;

/**
 * Handles the logic for refreshing the catalog and always receiving the up to date ZRIDs
 */
public interface TstpCatalogService {
    /**
     * Get the up to date ZRID
     *
     * @return the requested ZRID
     */
    String getZrid();
}
