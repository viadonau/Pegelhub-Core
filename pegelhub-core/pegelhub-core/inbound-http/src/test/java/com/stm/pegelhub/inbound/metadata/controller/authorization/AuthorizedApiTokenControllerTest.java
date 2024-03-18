package com.stm.pegelhub.inbound.metadata.controller.authorization;

import com.stm.pegelhub.inbound.metadata.controller.ApiTokenController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizedApiTokenControllerTest {

    private AuthorizedApiTokenController sut;
    private static final ApiTokenController DELEGATE_CONTROLLER = mock(ApiTokenController.class);

    @BeforeEach
    public void prepare() {
        sut = new AuthorizedApiTokenController(DELEGATE_CONTROLLER);
        reset(DELEGATE_CONTROLLER);
    }

    @Test
    void constructor_WhenAnyArgsAreNull_ThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new AuthorizedApiTokenController(null));
    }

    @Test
    void createToken() {
        sut.createToken();
        verify(DELEGATE_CONTROLLER, times(1)).createToken();
    }

    @Test
    void refreshToken() {
        sut.refreshToken("", null);
        verify(DELEGATE_CONTROLLER, times(1)).refreshToken(any(), null);
    }

    @Test
    void invalidateToken() {
        sut.invalidateToken("",null);
        verify(DELEGATE_CONTROLLER, times(1)).invalidateToken(any(), null);
    }

    @Test
    void getTokens() {
        sut.getTokens();
        verify(DELEGATE_CONTROLLER, times(1)).getTokens();
    }

    @Test
    void activateToken() {
        sut.activateToken(null);
        verify(DELEGATE_CONTROLLER, times(1)).activateToken(any());
    }
}