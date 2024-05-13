package com.stm.pegelhub.connector.tstp.task;

/**
 * Handles the logic for refreshing the catalog and always receiving the up to date ZRIDs
 */
public interface CatalogHandler {
    /**
     * Get the up to date ZRID
     *
     * @return the requested ZRID
     */
    String getZrid();
}
