package com.stm.pegelhub.inbound.metadata.controller.impl;

import com.stm.pegelhub.inbound.metadata.controller.TakerController;
import com.stm.pegelhub.inbound.metadata.dto.CreateTakerDto;
import com.stm.pegelhub.inbound.metadata.dto.TakerDto;
import com.stm.pegelhub.logic.service.metadata.TakerService;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of the Interface {@code TakerController}.
 */
public class HttpTakerController implements TakerController {

    private final TakerService takerService;

    public HttpTakerController(TakerService takerService) {
        this.takerService = requireNonNull(takerService);
    }

    @Override
    public TakerDto saveTaker(String apiKey, CreateTakerDto taker) {
        return DomainToDtoConverter.convert(takerService.saveTaker(DtoToDomainConverter.convert(taker)));
    }

    @Override
    public TakerDto getTakerById(UUID uuid) {
        return DomainToDtoConverter.convert(takerService.getTakerById(uuid));
    }

    @Override
    public List<TakerDto> getAllTakers() {
        return DomainToDtoConverter.convert(takerService.getAllTakers());
    }

    @Override
    public void deleteTaker(UUID uuid) {
        takerService.deleteTaker(uuid);
    }
}
