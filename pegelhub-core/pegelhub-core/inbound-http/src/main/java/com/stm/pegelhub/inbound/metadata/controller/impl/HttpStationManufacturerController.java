package com.stm.pegelhub.inbound.metadata.controller.impl;

import com.stm.pegelhub.inbound.metadata.controller.StationManufacturerController;
import com.stm.pegelhub.inbound.metadata.dto.CreateStationManufacturerDto;
import com.stm.pegelhub.inbound.metadata.dto.StationManufacturerDto;
import com.stm.pegelhub.logic.service.metadata.StationManufacturerService;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of the Interface {@code StationManufacturerController}.
 */
public class HttpStationManufacturerController implements StationManufacturerController {

    private final StationManufacturerService stationManufacturerService;

    public HttpStationManufacturerController(StationManufacturerService stationManufacturerService) {
        this.stationManufacturerService = requireNonNull(stationManufacturerService);
    }

    @Override
    public StationManufacturerDto saveStationManufacturer(CreateStationManufacturerDto stationManufacturer) {
        return DomainToDtoConverter.convert(stationManufacturerService.createStationManufacturer(DtoToDomainConverter.convert(stationManufacturer)));
    }

    @Override
    public StationManufacturerDto getStationManufacturerById(UUID uuid) {
        return DomainToDtoConverter.convert(stationManufacturerService.getStationManufacturerById(uuid));
    }

    @Override
    public List<StationManufacturerDto> getAllStationManufacturers() {
        return DomainToDtoConverter.convert(stationManufacturerService.getAllStationManufacturers());
    }

    @Override
    public void deleteStationManufacturer(UUID uuid) {
        stationManufacturerService.deleteStationManufacturer(uuid);
    }
}
