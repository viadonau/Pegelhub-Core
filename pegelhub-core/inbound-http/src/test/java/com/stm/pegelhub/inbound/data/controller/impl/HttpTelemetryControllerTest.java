package com.stm.pegelhub.inbound.data.controller.impl;

import com.stm.pegelhub.common.model.data.Telemetry;
import com.stm.pegelhub.logic.service.data.TelemetryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.stm.pegelhub.common.util.ExampleData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class HttpTelemetryControllerTest {

    private static final String API_KEY = "api_key";
    private HttpTelemetryController sut;

    private static final TelemetryService SERVICE = mock(TelemetryService.class);

    @BeforeEach
    public void setUp() {
        sut = new HttpTelemetryController(SERVICE);
        reset(SERVICE);
    }

    @Test
    public void constructorShouldThrowNullPointerExceptionIfTelemetryServiceIsNull() {
        assertThrows(NullPointerException.class, () -> new HttpTelemetryController(null));
    }

    @Test
    public void writeTelemetryDataShouldSaveTelemetry() {
        when(SERVICE.saveTelemetry(TELEMETRY)).thenReturn(TELEMETRY);

        Telemetry result = sut.writeTelemetryData(API_KEY, TELEMETRY);

        verify(SERVICE, times(1)).saveTelemetry(TELEMETRY);
        assertSame(TELEMETRY, result);
    }

    @Test
    public void findTelemetryInRangeShouldReturnTelemetryInRange() {
        String range = "last_hour";
        when(SERVICE.getByRange(range)).thenReturn(TELEMETRIES);

        List<Telemetry> result = sut.findTelemetryInRange(API_KEY, range);

        verify(SERVICE, times(1)).getByRange(range);
        assertSame(TELEMETRIES, result);
    }

    @Test
    public void findTelemetryByIdShouldReturnLastData() {
        when(SERVICE.getLastData(ID)).thenReturn(TELEMETRY);

        Telemetry result = sut.findTelemetryById(API_KEY, ID);

        verify(SERVICE, times(1)).getLastData(ID);
        assertSame(TELEMETRY, result);
    }
}