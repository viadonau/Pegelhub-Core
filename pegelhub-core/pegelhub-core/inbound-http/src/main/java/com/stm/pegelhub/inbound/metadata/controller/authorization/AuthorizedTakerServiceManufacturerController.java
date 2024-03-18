package com.stm.pegelhub.inbound.metadata.controller.authorization;

import com.stm.pegelhub.inbound.metadata.controller.TakerServiceManufacturerController;
import com.stm.pegelhub.inbound.metadata.dto.CreateTakerServiceManufacturerDto;
import com.stm.pegelhub.inbound.metadata.dto.TakerServiceManufacturerDto;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

//TODO: why does this class even exist? It doesn't authorize anything. It just forwards to the underlying controller?

/**
 * Implementation of the Interface {@code TakerServiceManufacturerController}.
 * Performs authorization of requests where necessary before they are delegated to the underlying controller.
 */
public class AuthorizedTakerServiceManufacturerController implements TakerServiceManufacturerController {

    private final TakerServiceManufacturerController delegate;

    public AuthorizedTakerServiceManufacturerController(TakerServiceManufacturerController delegate) {

        this.delegate = requireNonNull(delegate);
    }

    @Override
    public TakerServiceManufacturerDto saveTakerServiceManufacturer(CreateTakerServiceManufacturerDto takerServiceManufacturer) {
        return delegate.saveTakerServiceManufacturer(takerServiceManufacturer);
    }

    @Override
    public TakerServiceManufacturerDto getTakerServiceManufacturerById(UUID uuid) {
        return delegate.getTakerServiceManufacturerById(uuid);
    }

    @Override
    public List<TakerServiceManufacturerDto> getAllTakerServiceManufacturers() {
        return delegate.getAllTakerServiceManufacturers();
    }

    @Override
    public void deleteTakerServiceManufacturer(UUID uuid) {
        delegate.deleteTakerServiceManufacturer(uuid);
    }
}
