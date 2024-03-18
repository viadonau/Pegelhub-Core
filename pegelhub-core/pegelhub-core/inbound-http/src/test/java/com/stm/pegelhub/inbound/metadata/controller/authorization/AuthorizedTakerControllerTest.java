package com.stm.pegelhub.inbound.metadata.controller.authorization;

import com.stm.pegelhub.inbound.metadata.controller.TakerController;
import com.stm.pegelhub.logic.exception.UnauthorizedException;
import com.stm.pegelhub.logic.service.metadata.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.stm.pegelhub.inbound.dto.util.ExampleDtos.CREATE_TAKER_DTO;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthorizedTakerControllerTest {
    private AuthorizedTakerController sut;

    private static final AuthorizationService SERVICE = mock(AuthorizationService.class);
    private static final TakerController DELEGATE_CONTROLLER = mock(TakerController.class);

    @BeforeEach
    public void prepare() {
        sut = new AuthorizedTakerController(SERVICE, DELEGATE_CONTROLLER);
        reset(SERVICE);
        reset(DELEGATE_CONTROLLER);
    }

    @Test
    void constructor_WhenAnyArgsAreNull_ThrowsNPE() {

        assertThrows(NullPointerException.class, () -> new AuthorizedTakerController(null, DELEGATE_CONTROLLER));
        assertThrows(NullPointerException.class, () -> new AuthorizedTakerController(SERVICE, null));
    }

    @Test
    void saveTaker() {
        sut.saveTaker("", CREATE_TAKER_DTO);
        verify(SERVICE, times(1)).authorize(any());
        verify(DELEGATE_CONTROLLER, times(1)).saveTaker(any(), any());
    }

    @Test
    void saveTakerWithInvalidApiKeyThrowsUE() {
        doThrow(new UnauthorizedException()).when(SERVICE).authorize(any());
        assertThrows(UnauthorizedException.class, () -> sut.saveTaker("", CREATE_TAKER_DTO));
        verify(DELEGATE_CONTROLLER, times(0)).saveTaker(any(), any());
    }

    @Test
    void getTakerById() {
        sut.getTakerById(null);
        verify(DELEGATE_CONTROLLER, times(1)).getTakerById(any());
    }

    @Test
    void getAllTakers() {
        sut.getAllTakers();
        verify(DELEGATE_CONTROLLER, times(1)).getAllTakers();
    }

    @Test
    void deleteTaker() {
        sut.deleteTaker(null);
        verify(DELEGATE_CONTROLLER, times(1)).deleteTaker(any());
    }
}