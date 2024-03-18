package com.stm.pegelhub.inbound.metadata.controller.authorization;

import com.stm.pegelhub.inbound.metadata.controller.ApiTokenController;
import com.stm.pegelhub.inbound.metadata.dto.ApiTokenDto;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

//TODO: why does this class even exist? It doesn't authorize anything. It just forwards to the underlying controller?
/**
 * Implementation of the Interface {@code ApiTokenController}.
 * Performs authorization of requests where necessary before they are delegated to the underlying controller.
 */
public class AuthorizedApiTokenController implements ApiTokenController {

    private final ApiTokenController delegate;

    public AuthorizedApiTokenController(ApiTokenController delegate) {
        this.delegate = requireNonNull(delegate);
    }


    @Override
    public ApiTokenDto createToken() {
        return delegate.createToken();
    }

    @Override
    public ApiTokenDto refreshToken(String apiKey, String uuid) {
        return delegate.refreshToken(apiKey, uuid);
    }

    @Override
    public void invalidateToken(String apiKey, String uuid) {
        delegate.invalidateToken(apiKey, uuid);
    }

    @Override
    public List<UUID> getTokens() {
        return delegate.getTokens();
    }

    @Override
    public void activateToken(String uuid) {
        delegate.activateToken(uuid);
    }
}
