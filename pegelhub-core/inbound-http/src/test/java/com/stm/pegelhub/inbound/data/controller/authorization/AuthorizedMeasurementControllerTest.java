package com.stm.pegelhub.inbound.data.controller.authorization;

import com.stm.pegelhub.inbound.data.controller.MeasurementController;
import com.stm.pegelhub.inbound.data.dto.WriteMeasurementDto;
import com.stm.pegelhub.inbound.data.dto.WriteMeasurementsDto;
import com.stm.pegelhub.logic.exception.UnauthorizedException;
import com.stm.pegelhub.logic.service.metadata.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthorizedMeasurementControllerTest {


    private AuthorizedMeasurementController sut;

    private static final AuthorizationService SERVICE = mock(AuthorizationService.class);
    private static final MeasurementController DELEGATE_CONTROLLER = mock(MeasurementController.class);

    @BeforeEach
    public void prepare() {
        sut = new AuthorizedMeasurementController(SERVICE, DELEGATE_CONTROLLER);
        reset(SERVICE);
        reset(DELEGATE_CONTROLLER);
    }

    @Test
    void constructor_WhenAnyArgsAreNull_ThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new AuthorizedMeasurementController(null, DELEGATE_CONTROLLER));
        assertThrows(NullPointerException.class, () -> new AuthorizedMeasurementController(SERVICE, null));
    }

    @Test
    void writeMeasurementData_WhenEverythingWorks() {
        sut.writeMeasurementData("", new WriteMeasurementsDto(List.of(new WriteMeasurementDto(LocalDateTime.now(), Map.of("", 1.0), Map.of()))));
        verify(SERVICE, times(1)).authorize(any());
        verify(DELEGATE_CONTROLLER, times(1)).writeMeasurementData(any(), any());
    }

    @Test
    void writeMeasurementData_WhenUnauthorized_ThrowsUE() { // UnauthorizedException
        doThrow(new UnauthorizedException()).when(SERVICE).authorize(any());
        assertThrows(UnauthorizedException.class, () -> sut.writeMeasurementData("", new WriteMeasurementsDto(List.of(new WriteMeasurementDto(LocalDateTime.now(), Map.of("", 1.0), Map.of())))));

        verify(DELEGATE_CONTROLLER, times(0)).writeMeasurementData(any(), any());
    }

    @Test
    void findMeasurementInRange_WhenEverythingWorks() {
        sut.findMeasurementInRange("", "");
        verify(SERVICE, times(1)).authorize(any());
        verify(DELEGATE_CONTROLLER, times(1)).findMeasurementInRange(any(), any());
    }

    @Test
    void findMeasurementInRange_WhenUnauthorized_ThrowsUE() { // UnauthorizedException
        doThrow(new UnauthorizedException()).when(SERVICE).authorize(any());
        assertThrows(UnauthorizedException.class, () -> sut.findMeasurementInRange("", ""));

        verify(DELEGATE_CONTROLLER, times(0)).findMeasurementById(any(), any());
    }

    @Test
    void findMeasurementForSupplierInRange_WhenEverythingWorks() {
        sut.findMeasurementForSupplierInRange("", "", "");
        verify(SERVICE, times(1)).authorize(any());
        verify(DELEGATE_CONTROLLER, times(1)).findMeasurementForSupplierInRange(any(), any(), any());
    }

    @Test
    void findMeasurementForSupplierInRange_WhenUnauthorized_ThrowsUE() { // UnauthorizedException
        doThrow(new UnauthorizedException()).when(SERVICE).authorize(any());
        assertThrows(UnauthorizedException.class, () -> sut.findMeasurementForSupplierInRange("", "", ""));

        verify(DELEGATE_CONTROLLER, times(0)).findMeasurementForSupplierInRange(any(), any(), any());
    }

    @Test
    void findMeasurementById_WhenEverythingWorks() {
        sut.findMeasurementById("", null);
        verify(SERVICE, times(1)).authorize(any());
        verify(DELEGATE_CONTROLLER, times(1)).findMeasurementById(any(), any());
    }

    @Test
    void findMeasurementById_WhenUnauthorized_ThrowsUE() { // UnauthorizedException
        doThrow(new UnauthorizedException()).when(SERVICE).authorize(any());
        assertThrows(UnauthorizedException.class, () -> sut.findMeasurementById("", null));

        verify(DELEGATE_CONTROLLER, times(0)).findMeasurementById(any(), any());
    }
}