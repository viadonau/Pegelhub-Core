package com.stm.pegelhub.inbound.metadata.controller.authorization;

import com.stm.pegelhub.inbound.metadata.controller.TakerController;
import com.stm.pegelhub.inbound.metadata.dto.CreateTakerDto;
import com.stm.pegelhub.inbound.metadata.dto.SupplierDto;
import com.stm.pegelhub.inbound.metadata.dto.TakerDto;
import com.stm.pegelhub.logic.holder.AuthTokenIdHolder;
import com.stm.pegelhub.logic.service.metadata.AuthorizationService;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of the Interface {@code TakerController}.
 * Performs authorization of requests where necessary before they are delegated to the underlying controller.
 */
public class AuthorizedTakerController implements TakerController {

    private final AuthorizationService authorizationService;
    private final TakerController delegate;

    public AuthorizedTakerController(AuthorizationService authorizationService, TakerController delegate) {
        this.authorizationService = requireNonNull(authorizationService);
        this.delegate = requireNonNull(delegate);
    }

    /**
     * authorizes the request and forwards it to for further handling
     * @param apiKey the key which is used for Authorization
     * @param taker the Taker to be saved
     * @return the saved {@link TakerDto Taker}
     */
    @Override
    public TakerDto saveTaker(String apiKey, CreateTakerDto taker) {
        AuthTokenIdHolder.set(authorizationService.authorize(apiKey));
        TakerDto takerDto = delegate.saveTaker(apiKey, taker);
        AuthTokenIdHolder.clear();
        return takerDto;
    }

    @Override
    public TakerDto getTakerById(UUID uuid) {
        return delegate.getTakerById(uuid);
    }

    @Override
    public List<TakerDto> getAllTakers() {
        return delegate.getAllTakers();
    }

    @Override
    public void deleteTaker(UUID uuid) {
        delegate.deleteTaker(uuid);
    }
}
