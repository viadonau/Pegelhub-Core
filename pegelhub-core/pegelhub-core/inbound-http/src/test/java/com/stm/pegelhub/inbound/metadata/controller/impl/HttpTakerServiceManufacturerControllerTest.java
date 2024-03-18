package com.stm.pegelhub.inbound.metadata.controller.impl;

import com.stm.pegelhub.common.model.metadata.TakerServiceManufacturer;
import com.stm.pegelhub.inbound.metadata.dto.TakerServiceManufacturerDto;
import com.stm.pegelhub.logic.service.metadata.TakerServiceManufacturerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.stm.pegelhub.common.util.ExampleData.TAKER_SERVICE_MANUFACTURER;
import static com.stm.pegelhub.inbound.dto.util.ExampleDtos.CREATE_TAKER_SERVICE_MANUFACTURER_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HttpTakerServiceManufacturerControllerTest {

    private HttpTakerServiceManufacturerController sut;

    private static final TakerServiceManufacturerService SERVICE = mock(TakerServiceManufacturerService.class);

    @BeforeEach
    void setUp() {
        sut = new HttpTakerServiceManufacturerController(SERVICE);
        reset(SERVICE);
    }

    @Test
    public void constructorShouldThrowNullPointerExceptionIfApiTokenServiceIsNull() {
        assertThrows(NullPointerException.class, () -> new HttpTakerServiceManufacturerController(null));
    }

    @Test
    void createTakerServiceManufacturer() {
        when(SERVICE.createTakerServiceManufacturer(any())).thenReturn(TAKER_SERVICE_MANUFACTURER);
        TakerServiceManufacturerDto expected = DomainToDtoConverter.convert(TAKER_SERVICE_MANUFACTURER);
        TakerServiceManufacturerDto actual = sut.saveTakerServiceManufacturer(CREATE_TAKER_SERVICE_MANUFACTURER_DTO);
        assertEquals(expected, actual);
    }

    @Test
    void getTakerServiceManufacturerById() {
        UUID uuid = UUID.randomUUID();
        when(SERVICE.getTakerServiceManufacturerById(uuid)).thenReturn(TAKER_SERVICE_MANUFACTURER);
        TakerServiceManufacturerDto expected = DomainToDtoConverter.convert(TAKER_SERVICE_MANUFACTURER);
        TakerServiceManufacturerDto actual = sut.getTakerServiceManufacturerById(uuid);
        assertEquals(expected, actual);
    }

    @Test
    void getAllTakerServiceManufacturers() {
        List<TakerServiceManufacturer> takerServiceManufacturers = new ArrayList<>();
        takerServiceManufacturers.add(TAKER_SERVICE_MANUFACTURER);
        when(SERVICE.getAllTakerServiceManufacturers()).thenReturn(takerServiceManufacturers);
        List<TakerServiceManufacturerDto> expected = DomainToDtoConverter.convert(takerServiceManufacturers);
        List<TakerServiceManufacturerDto> actual = sut.getAllTakerServiceManufacturers();
        assertEquals(expected, actual);
    }

    @Test
    void deleteTakerServiceManufacturer() {
        UUID uuid = UUID.randomUUID();
        sut.deleteTakerServiceManufacturer(uuid);
        verify(SERVICE, times(1)).deleteTakerServiceManufacturer(uuid);
    }
}