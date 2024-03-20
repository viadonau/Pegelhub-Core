package com.stm.pegelhub.inbound.metadata.controller.impl;

import com.stm.pegelhub.inbound.metadata.dto.ApiTokenDto;
import com.stm.pegelhub.logic.service.metadata.ApiTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.stm.pegelhub.common.util.ExampleData.API_TOKEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HttpApiTokenControllerTest {

    private HttpApiTokenController sut;

    private static final ApiTokenService SERVICE = mock(ApiTokenService.class);

    @BeforeEach
    public void setUp() {
        sut = new HttpApiTokenController(SERVICE);
        reset(SERVICE);
    }

    @Test
    public void constructorShouldThrowNullPointerExceptionIfApiTokenServiceIsNull() {
        assertThrows(NullPointerException.class, () -> new HttpApiTokenController(null));
    }

    @Test
    public void createTokenShouldReturnApiTokenDto() {
        when(SERVICE.createToken()).thenReturn(API_TOKEN);

        ApiTokenDto result = sut.createToken();

        verify(SERVICE, times(1)).createToken();
        assertEquals(API_TOKEN, result.apiKey());
    }

    @Test
    public void refreshTokenShouldReturnApiTokenDto() {
        String apiKey = "api_key";
        when(SERVICE.refreshToken(apiKey, null)).thenReturn(API_TOKEN);

        ApiTokenDto result = sut.refreshToken(apiKey, null);

        verify(SERVICE, times(1)).refreshToken(apiKey, null);
        assertEquals(API_TOKEN, result.apiKey());
    }

    @Test
    public void invalidateTokenShouldCallApiTokenServiceInvalidateToken() {
        String apiKey = "api_key";
        sut.invalidateToken(apiKey, null);

        verify(SERVICE, times(1)).invalidateToken(apiKey, null);
    }

    @Test
    public void getTokensShouldReturnApiTokens() {
        List<UUID> tokens = new ArrayList<>();
        UUID token1 = UUID.randomUUID();
        UUID token2 = UUID.randomUUID();
        tokens.add(token1);
        tokens.add(token2);
        when(SERVICE.getTokens()).thenReturn(tokens);

        List<UUID> result = sut.getTokens();

        verify(SERVICE, times(1)).getTokens();
        assertEquals(tokens, result);
    }

    @Test
    public void activateTokenShouldCallApiTokenServiceActivateToken() {
        UUID token = UUID.randomUUID();
        sut.activateToken(token.toString());

        verify(SERVICE, times(1)).activateToken(token);
    }
}