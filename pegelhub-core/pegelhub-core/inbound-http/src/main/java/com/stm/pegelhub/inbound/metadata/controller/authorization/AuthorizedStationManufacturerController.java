package com.stm.pegelhub.inbound.metadata.controller.authorization;

import com.stm.pegelhub.inbound.metadata.controller.StationManufacturerController;
import com.stm.pegelhub.inbound.metadata.dto.CreateStationManufacturerDto;
import com.stm.pegelhub.inbound.metadata.dto.StationManufacturerDto;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

//TODO: why does this class even exist? It doesn't authorize anything. It just forwards to the underlying controller?
/**
 * Implementation of the Interface {@code StationManufacturerController}.
 * Performs authorization of requests where necessary before they are delegated to the underlying controller.
 */
public class AuthorizedStationManufacturerController implements StationManufacturerController {


    private final StationManufacturerController delegate;

    public AuthorizedStationManufacturerController(StationManufacturerController delegate) {

        this.delegate = requireNonNull(delegate);
    }

    @Override
    public StationManufacturerDto saveStationManufacturer(CreateStationManufacturerDto stationManufacturer) {
        return delegate.saveStationManufacturer(stationManufacturer);
    }

    @Override
    public StationManufacturerDto getStationManufacturerById(UUID uuid) {
        return delegate.getStationManufacturerById(uuid);
    }

    @Override
    public List<StationManufacturerDto> getAllStationManufacturers() {
        return delegate.getAllStationManufacturers();
    }

    @Override
    public void deleteStationManufacturer(UUID uuid) {
        delegate.deleteStationManufacturer(uuid);
    }
}
