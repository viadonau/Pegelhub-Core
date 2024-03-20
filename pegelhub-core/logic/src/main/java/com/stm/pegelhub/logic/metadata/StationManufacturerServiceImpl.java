package com.stm.pegelhub.logic.metadata;

import com.stm.pegelhub.common.model.metadata.StationManufacturer;
import com.stm.pegelhub.logic.service.metadata.StationManufacturerService;
import com.stm.pegelhub.outbound.repository.metadata.StationManufacturerRepository;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Default implementation for {@code StationManufacturerService}.
 */
public final class StationManufacturerServiceImpl implements StationManufacturerService {

    private final StationManufacturerRepository stationManufacturerRepository;

    public StationManufacturerServiceImpl(StationManufacturerRepository stationManufacturerRepository) {
        this.stationManufacturerRepository = requireNonNull(stationManufacturerRepository);
    }

    /**
     * @param stationManufacturer to save.
     * @return the saved {@link StationManufacturer}
     */
    @Override
    public StationManufacturer createStationManufacturer(StationManufacturer stationManufacturer) {
        return stationManufacturerRepository.saveStationManufacturer(stationManufacturer);
    }

    /**
     * @param uuid of the station manufacturer to be searched for.
     * @return the corresponding {@link StationManufacturer} to the specified {@link UUID}
     */
    @Override
    public StationManufacturer getStationManufacturerById(UUID uuid) {
        return stationManufacturerRepository.getById(uuid);
    }

    /**
     * @return all saved {@link StationManufacturer}s
     */
    @Override
    public List<StationManufacturer> getAllStationManufacturers() {
        return stationManufacturerRepository.getAllStationManufacturers();
    }

    /**
     * @param stationManufacturer to update.
     * @return the updated {@link StationManufacturer}
     */
    @Override
    public StationManufacturer updateStationManufacturers(StationManufacturer stationManufacturer) {
        return stationManufacturerRepository.update(stationManufacturer);
    }

    /**
     * @param uuid {@link UUID} of the station manufacturer to delete.
     */
    @Override
    public void deleteStationManufacturer(UUID uuid) {
        stationManufacturerRepository.deleteStationManufacturer(uuid);

    }
}
