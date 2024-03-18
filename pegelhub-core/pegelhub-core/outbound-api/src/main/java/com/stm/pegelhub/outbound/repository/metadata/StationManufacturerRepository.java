package com.stm.pegelhub.outbound.repository.metadata;

import com.stm.pegelhub.common.model.metadata.StationManufacturer;

import java.util.List;
import java.util.UUID;

/**
 * Repository for all {@code StationManufacturer}s.
 */
public interface StationManufacturerRepository {

    /**
     * Saves a station manufacturer to the repository.
     *
     * @param stationManufacturer to save.
     * @return the saved station manufacturer.
     */
    StationManufacturer saveStationManufacturer(StationManufacturer stationManufacturer);

    /**
     * Get a station manufacturer from the repository by its id.
     *
     * @param uuid of the station manufacturer.
     * @return the found station manufacturer.
     */
    StationManufacturer getById(UUID uuid);

    /**
     * Get all station manufacturers stored in the repository.
     *
     * @return the found station manufacturers.
     */
    List<StationManufacturer> getAllStationManufacturers();

    /**
     * Updates a station manufacturer in the repository.
     *
     * @param stationManufacturer to update.
     * @return the updated station manufacturer.
     */
    StationManufacturer update(StationManufacturer stationManufacturer);

    /**
     * Deletes a station manufacturer by its id.
     *
     * @param uuid of the station manufacturer to delete.
     */
    void deleteStationManufacturer(UUID uuid);
}

