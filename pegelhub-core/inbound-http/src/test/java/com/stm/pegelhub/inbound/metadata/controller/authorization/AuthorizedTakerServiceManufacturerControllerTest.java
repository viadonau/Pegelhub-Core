package com.stm.pegelhub.inbound.metadata.controller.authorization;

import com.stm.pegelhub.inbound.metadata.controller.TakerServiceManufacturerController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.stm.pegelhub.inbound.dto.util.ExampleDtos.CREATE_TAKER_SERVICE_MANUFACTURER_DTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizedTakerServiceManufacturerControllerTest {
    private AuthorizedTakerServiceManufacturerController sut;
    private static final TakerServiceManufacturerController DELEGATE_CONTROLLER = mock(TakerServiceManufacturerController.class);

    @BeforeEach
    public void prepare() {
        sut = new AuthorizedTakerServiceManufacturerController(DELEGATE_CONTROLLER);
        reset(DELEGATE_CONTROLLER);
    }

    @Test
    void constructor_WhenAnyArgsAreNull_ThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new AuthorizedTakerServiceManufacturerController(null));
    }
    @Test
    void saveTakerServiceManufacturer() {
        sut.saveTakerServiceManufacturer(CREATE_TAKER_SERVICE_MANUFACTURER_DTO);
        verify(DELEGATE_CONTROLLER, times(1)).saveTakerServiceManufacturer(any());
    }

    @Test
    void getTakerServiceManufacturerById() {
        sut.getTakerServiceManufacturerById(null);
        verify(DELEGATE_CONTROLLER, times(1)).getTakerServiceManufacturerById(any());
    }

    @Test
    void getAllTakerServiceManufacturers() {
        sut.getAllTakerServiceManufacturers();
        verify(DELEGATE_CONTROLLER, times(1)).getAllTakerServiceManufacturers();
    }

    @Test
    void deleteTakerServiceManufacturer() {
        sut.deleteTakerServiceManufacturer(null);
        verify(DELEGATE_CONTROLLER, times(1)).deleteTakerServiceManufacturer(any());
    }
}