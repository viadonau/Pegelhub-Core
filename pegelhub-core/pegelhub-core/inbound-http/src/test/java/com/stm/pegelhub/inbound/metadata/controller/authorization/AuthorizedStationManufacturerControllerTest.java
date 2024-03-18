package com.stm.pegelhub.inbound.metadata.controller.authorization;

import com.stm.pegelhub.inbound.metadata.controller.StationManufacturerController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.stm.pegelhub.inbound.dto.util.ExampleDtos.CREATE_STATION_MANUFACTURER_DTO;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthorizedStationManufacturerControllerTest {
    private AuthorizedStationManufacturerController sut;
    private static final StationManufacturerController DELEGATE_CONTROLLER = mock(StationManufacturerController.class);

    @BeforeEach
    public void prepare() {
        sut = new AuthorizedStationManufacturerController(DELEGATE_CONTROLLER);
        reset(DELEGATE_CONTROLLER);
    }

    @Test
    void constructor_WhenAnyArgsAreNull_ThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new AuthorizedStationManufacturerController(null));
    }

    @Test
    void saveStationManufacturer() {
        sut.saveStationManufacturer(CREATE_STATION_MANUFACTURER_DTO);
        verify(DELEGATE_CONTROLLER, times(1)).saveStationManufacturer(any());
    }

    @Test
    void getStationManufacturerById() {
        sut.getStationManufacturerById(null);
        verify(DELEGATE_CONTROLLER, times(1)).getStationManufacturerById(any());
    }

    @Test
    void getAllStationManufacturers() {
        sut.getAllStationManufacturers();
        verify(DELEGATE_CONTROLLER, times(1)).getAllStationManufacturers();
    }

    @Test
    void deleteStationManufacturer() {
        sut.deleteStationManufacturer(null);
        verify(DELEGATE_CONTROLLER, times(1)).deleteStationManufacturer(any());
    }
}