package com.stm.pegelhub.inbound.data.controller.authorization;

import com.stm.pegelhub.inbound.data.controller.TelemetryController;
import com.stm.pegelhub.logic.exception.UnauthorizedException;
import com.stm.pegelhub.logic.service.metadata.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.stm.pegelhub.common.util.ExampleData.TELEMETRY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthorizedTelemetryControllerTest {

    private AuthorizedTelemetryController sut;

    private static final AuthorizationService SERVICE = mock(AuthorizationService.class);
    private static final TelemetryController DELEGATE_CONTROLLER = mock(TelemetryController.class);

    @BeforeEach
    public void prepare() {
        sut = new AuthorizedTelemetryController(SERVICE, DELEGATE_CONTROLLER);
        reset(SERVICE);
        reset(DELEGATE_CONTROLLER);
    }
    @Test
    void constructor_WhenAnyArgsAreNull_ThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new AuthorizedTelemetryController(null, DELEGATE_CONTROLLER));
        assertThrows(NullPointerException.class, () -> new AuthorizedTelemetryController(SERVICE, null));
    }
    @Test
    void writeTelemetryData_WhenEverythingWorks() {
        sut.writeTelemetryData("", TELEMETRY);
        verify(SERVICE, times(1)).authorize(any());
        verify(DELEGATE_CONTROLLER, times(1)).writeTelemetryData(any(), any());
    }
    @Test
    void writeTelemetryData_WhenUnauthorized_ThrowsUE() { // UnauthorizedException
        doThrow(new UnauthorizedException()).when(SERVICE).authorize(any());
        assertThrows(UnauthorizedException.class, () -> sut.writeTelemetryData("", TELEMETRY));

        verify(DELEGATE_CONTROLLER, times(0)).writeTelemetryData(any(), any());
    }
    @Test
    void findTelemetryInRange_WhenEverythingWorks() {
        sut.findTelemetryInRange("", "");
        verify(SERVICE, times(1)).authorize(any());
        verify(DELEGATE_CONTROLLER, times(1)).findTelemetryInRange(any(), any());
    }
    @Test
    void findTelemetryInRange_WhenUnauthorized_ThrowsUE() { // UnauthorizedException
        doThrow(new UnauthorizedException()).when(SERVICE).authorize(any());
        assertThrows(UnauthorizedException.class, () -> sut.findTelemetryInRange("", ""));

        verify(DELEGATE_CONTROLLER, times(0)).findTelemetryById(any(), any());
    }
    @Test
    void findTelemetryById_WhenEverythingWorks() {
        sut.findTelemetryById("", null);
        verify(SERVICE, times(1)).authorize(any());
        verify(DELEGATE_CONTROLLER, times(1)).findTelemetryById(any(), any());
    }
    @Test
    void findTelemetryById_WhenUnauthorized_ThrowsUE() { // UnauthorizedException
        doThrow(new UnauthorizedException()).when(SERVICE).authorize(any());
        assertThrows(UnauthorizedException.class, () -> sut.findTelemetryById("", null));

        verify(DELEGATE_CONTROLLER, times(0)).findTelemetryById(any(), any());
    }
}