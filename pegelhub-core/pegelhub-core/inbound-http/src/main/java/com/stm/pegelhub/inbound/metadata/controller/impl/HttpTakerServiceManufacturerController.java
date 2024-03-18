package com.stm.pegelhub.inbound.metadata.controller.impl;

import com.stm.pegelhub.inbound.metadata.controller.TakerServiceManufacturerController;
import com.stm.pegelhub.inbound.metadata.dto.CreateTakerServiceManufacturerDto;
import com.stm.pegelhub.inbound.metadata.dto.TakerServiceManufacturerDto;
import com.stm.pegelhub.logic.service.metadata.TakerServiceManufacturerService;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of the Interface {@code TakerServiceManufacturerController}.
 */
public class HttpTakerServiceManufacturerController implements TakerServiceManufacturerController {

    private final TakerServiceManufacturerService takerServiceManufacturerService;

    public HttpTakerServiceManufacturerController(TakerServiceManufacturerService takerServiceManufacturerService) {
        this.takerServiceManufacturerService = requireNonNull(takerServiceManufacturerService);
    }

    @Override
    public TakerServiceManufacturerDto saveTakerServiceManufacturer(CreateTakerServiceManufacturerDto takerServiceManufacturer) {
        return DomainToDtoConverter.convert(takerServiceManufacturerService.createTakerServiceManufacturer(DtoToDomainConverter.convert(takerServiceManufacturer)));
    }

    @Override
    public TakerServiceManufacturerDto getTakerServiceManufacturerById(UUID uuid) {
        return DomainToDtoConverter.convert(takerServiceManufacturerService.getTakerServiceManufacturerById(uuid));
    }

    @Override
    public List<TakerServiceManufacturerDto> getAllTakerServiceManufacturers() {
        return DomainToDtoConverter.convert(takerServiceManufacturerService.getAllTakerServiceManufacturers());
    }

    @Override
    public void deleteTakerServiceManufacturer(UUID uuid) {
        takerServiceManufacturerService.deleteTakerServiceManufacturer(uuid);
    }
}
