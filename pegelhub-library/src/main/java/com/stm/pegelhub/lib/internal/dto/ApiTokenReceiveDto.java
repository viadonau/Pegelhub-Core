package com.stm.pegelhub.lib.internal.dto;

import com.stm.pegelhub.lib.model.ApiToken;

public record ApiTokenReceiveDto(String apiKey) {
    public ApiToken toApiToken() {
        var dto = new ApiToken();
        dto.setApiKey(apiKey);
        return dto;
    }
}