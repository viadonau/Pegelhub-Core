package com.stm.pegelhub.inbound.metadata.controller.impl;

import com.stm.pegelhub.common.model.metadata.Taker;
import com.stm.pegelhub.inbound.metadata.dto.TakerDto;
import com.stm.pegelhub.logic.service.metadata.TakerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.stm.pegelhub.common.util.ExampleData.TAKER;
import static com.stm.pegelhub.inbound.dto.util.ExampleDtos.CREATE_TAKER_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HttpTakerControllerTest {

    private HttpTakerController sut;

    private static final TakerService SERVICE = mock(TakerService.class);

    @BeforeEach
    void setUp() {
        sut = new HttpTakerController(SERVICE);
        reset(SERVICE);
    }

    @Test
    public void constructorShouldThrowNullPointerExceptionIfApiTokenServiceIsNull() {
        assertThrows(NullPointerException.class, () -> new HttpTakerController(null));
    }

    @Test
    void createTaker() {
        when(SERVICE.saveTaker(any())).thenReturn(TAKER);
        TakerDto expected = DomainToDtoConverter.convert(TAKER);
        TakerDto actual = sut.saveTaker("", CREATE_TAKER_DTO);
        assertEquals(expected, actual);
    }

    @Test
    void getTakerById() {
        UUID uuid = UUID.randomUUID();
        when(SERVICE.getTakerById(uuid)).thenReturn(TAKER);
        TakerDto expected = DomainToDtoConverter.convert(TAKER);
        TakerDto actual = sut.getTakerById(uuid);
        assertEquals(expected, actual);
    }

    @Test
    void getAllTakers() {
        List<Taker> takers = new ArrayList<>();
        takers.add(TAKER);
        when(SERVICE.getAllTakers()).thenReturn(takers);
        List<TakerDto> expected = DomainToDtoConverter.convert(takers);
        List<TakerDto> actual = sut.getAllTakers();
        assertEquals(expected, actual);
    }

    @Test
    void deleteTaker() {
        UUID uuid = UUID.randomUUID();
        sut.deleteTaker(uuid);
        verify(SERVICE, times(1)).deleteTaker(uuid);
    }
}