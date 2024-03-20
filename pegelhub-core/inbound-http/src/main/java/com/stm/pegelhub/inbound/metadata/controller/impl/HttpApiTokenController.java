package com.stm.pegelhub.inbound.metadata.controller.impl;

import com.stm.pegelhub.inbound.metadata.controller.ApiTokenController;
import com.stm.pegelhub.inbound.metadata.dto.ApiTokenDto;
import com.stm.pegelhub.logic.service.metadata.ApiTokenService;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of the Interface {@code ApiTokenController}.
 */
public class HttpApiTokenController implements ApiTokenController {

    private final ApiTokenService apiTokenService;

    public HttpApiTokenController(ApiTokenService apiTokenService) {
        this.apiTokenService = requireNonNull(apiTokenService);
    }


    @Override
    public ApiTokenDto createToken() {
        return new ApiTokenDto(apiTokenService.createToken());
    }

    @Override
    public ApiTokenDto refreshToken(String apiKey, String uuid) {
        return new ApiTokenDto(apiTokenService.refreshToken(apiKey, UUID.fromString(uuid)));
    }

    @Override
    public void invalidateToken(String apiKey, String uuid) {
        apiTokenService.invalidateToken(apiKey, UUID.fromString(uuid));
    }

    @Override
    public List<UUID> getTokens() {
        return apiTokenService.getTokens();
    }

    @Override
    public void activateToken(String uuid) {
        apiTokenService.activateToken(UUID.fromString(uuid));
    }
}
