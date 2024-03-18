package com.stm.pegelhub.inbound.data.controller.impl;

import com.stm.pegelhub.common.model.data.Measurement;
import com.stm.pegelhub.inbound.data.dto.WriteMeasurementDto;
import com.stm.pegelhub.inbound.data.dto.WriteMeasurementsDto;
import com.stm.pegelhub.logic.service.data.MeasurementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.stm.pegelhub.common.util.ExampleData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HttpMeasurementControllerTest {


    private HttpMeasurementController controller;
    private static final MeasurementService SERVICE = mock(MeasurementService.class);

    @BeforeEach
    public void prepare() {
        controller = new HttpMeasurementController(SERVICE);
        reset(SERVICE);
    }

    @Test
    void constructor_WhenAnyArgsAreNull_ThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new HttpMeasurementController(null));
    }

    @Test
    void writeMeasurementData() {
        assertDoesNotThrow(() -> controller.writeMeasurementData("", new WriteMeasurementsDto(List.of(new WriteMeasurementDto(LocalDateTime.now(), Map.of("", 1.0), Map.of())))));
    }

    @Test
    void findMeasurementInRange() {
        when(SERVICE.getByRange("range")).thenReturn(MEASUREMENTS);
        List<Measurement> res = controller.findMeasurementInRange("", "range");
        assertEquals(MEASUREMENTS, res);
    }

    @Test
    void findMeasurementForSupplierInRange() {
        when(SERVICE.getBySupplierAndRange("supplier","range")).thenReturn(MEASUREMENTS);
        List<Measurement> res = controller.findMeasurementForSupplierInRange("", "supplier", "range");
        assertEquals(MEASUREMENTS, res);
    }

    @Test
    void findMeasurementById() {
        when(SERVICE.getLastData(ID)).thenReturn(MEASUREMENT);
        Measurement res = controller.findMeasurementById("", ID);
        assertEquals(MEASUREMENT, res);
    }
}