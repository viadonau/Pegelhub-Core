package com.stm.pegelhub.inbound.data.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WriteMeasurementsDtoTest {

    @Test
    void constructor_WhenEverythingWorks() {
        assertDoesNotThrow(() -> new WriteMeasurementsDto(List.of(new WriteMeasurementDto(LocalDateTime.now(), Map.of("hello", 1.0), Map.of()))));
    }

    @Test
    void constructorWithNullArgsThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new WriteMeasurementsDto(null));
    }

    @Test
    void constructorWithEmptyArgsThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> new WriteMeasurementsDto(List.of()));
    }
}