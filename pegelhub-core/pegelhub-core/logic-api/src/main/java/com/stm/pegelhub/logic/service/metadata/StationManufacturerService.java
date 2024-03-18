package com.stm.pegelhub.logic.service.metadata;

import com.stm.pegelhub.common.model.metadata.StationManufacturer;

import java.util.List;
import java.util.UUID;

/**
 * Service for all {@code StationManufacturer}s.
 */
public interface StationManufacturerService {

    /**
     * Creates a station manufacturer.
     *
     * @param stationManufacturer to save.
     * @return the saved station manufacturer.
     */
    StationManufacturer createStationManufacturer(StationManufacturer stationManufacturer);

    /**
     * Get a station manufacturer by its id.
     *
     * @param uuid of the station manufacturer.
     * @return the found station manufacturer.
     */
    StationManufacturer getStationManufacturerById(UUID uuid);

    /**
     * Get all station manufacturers.
     *
     * @return the found station manufacturers.
     */
    List<StationManufacturer> getAllStationManufacturers();

    /**
     * Updates a station manufacturer.
     *
     * @param stationManufacturer to update.
     * @return the updated station manufacturer.
     */
    StationManufacturer updateStationManufacturers(StationManufacturer stationManufacturer);

    /**
     * Deletes a station manufacturer by its id.
     *
     * @param uuid of the station manufacturer to delete.
     */
    void deleteStationManufacturer(UUID uuid);
}
