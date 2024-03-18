package com.stm.pegelhub.inbound.metadata.dto;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.stm.pegelhub.inbound.dto.util.ExampleDtos.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SupplierDtoTest {
    private static final String LONG_DATA = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";

    private static final UUID id = UUID.randomUUID();

    @Test
    void constructor_WhenEverythingWorks() {
        assertDoesNotThrow(() -> new SupplierDto(id, STATION_NUMBER, STATION_ID, STATION_NAME,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
    }

    @Test
    void constructorWithNullArgsThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new SupplierDto(null, STATION_NUMBER, STATION_ID, STATION_NAME,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(NullPointerException.class, () -> new SupplierDto(id, null, STATION_ID, STATION_NAME,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(NullPointerException.class, () -> new SupplierDto(id, STATION_NUMBER, null, STATION_NAME,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(NullPointerException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, null,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(NullPointerException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, STATION_NAME,
                null, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(NullPointerException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, STATION_NAME,
                STATION_WATER, null, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(NullPointerException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, STATION_NAME,
                STATION_WATER, STATION_WATER_TYPE, null, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(NullPointerException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, STATION_NAME,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, null, REFRESH_RATE, ACCURACY,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(NullPointerException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, STATION_NAME,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, null, ACCURACY,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(NullPointerException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, STATION_NAME,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, null,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(NullPointerException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, STATION_NAME,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                null, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(NullPointerException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, STATION_NAME,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                MAIN_USAGE, null, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
    }

    @Test
    void constructorWithLongArgsThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> new SupplierDto(id, LONG_DATA, STATION_ID, STATION_NAME,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(IllegalArgumentException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, LONG_DATA,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(IllegalArgumentException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, STATION_NAME,
                LONG_DATA, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(IllegalArgumentException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, STATION_NAME,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, 10000.0,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(IllegalArgumentException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, STATION_NAME,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                LONG_DATA, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(IllegalArgumentException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, STATION_NAME,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                MAIN_USAGE, LONG_DATA, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
    }

    @Test
    void constructorWithEmptyArgsThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> new SupplierDto(id, "", STATION_ID, STATION_NAME,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(IllegalArgumentException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, "",
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(IllegalArgumentException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, STATION_NAME,
                "", STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(IllegalArgumentException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, STATION_NAME,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                "", DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
        assertThrows(IllegalArgumentException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, STATION_NAME,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, ACCURACY,
                MAIN_USAGE, "", REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
    }

    @Test
    void constructorWithNegativeArgsThrowsIAE() {
        assertThrows(IllegalArgumentException.class, () -> new SupplierDto(id, STATION_NUMBER, STATION_ID, STATION_NAME,
                STATION_WATER, STATION_WATER_TYPE, STATION_MANUFACTURER_DTO, CONNECTOR_DTO, REFRESH_RATE, -1.0,
                MAIN_USAGE, DATA_CRITICALLY, REFERENCE_LEVEL, REFERENCE_PLACE, WATER_KILOMETER, WATER_SIDE, WATER_LAT,
                WATER_LONG, WATER_LAT, WATER_LONG, HSW, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW_REF, HSW, HSW, HSW, HSW, "TestUse"));
    }
}