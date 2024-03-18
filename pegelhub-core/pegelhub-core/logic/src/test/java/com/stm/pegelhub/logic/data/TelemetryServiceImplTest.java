package com.stm.pegelhub.logic.data;

import com.stm.pegelhub.common.model.data.Telemetry;
import com.stm.pegelhub.logic.data.service.impl.TelemetryServiceImpl;
import com.stm.pegelhub.outbound.repository.data.TelemetryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.stm.pegelhub.common.util.ExampleData.TELEMETRY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

final class TelemetryServiceImplTest {

    private TelemetryServiceImpl telemetryService;
    private static final TelemetryRepository REPOSITORY = mock(TelemetryRepository.class);

    @BeforeEach
    public void prepare() {
        telemetryService = new TelemetryServiceImpl(REPOSITORY);
        reset(REPOSITORY);
    }

    @Test
    public void constructorWithNullArgsThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new TelemetryServiceImpl(null));
    }

    @Test
    public void saveTelemetry() {
        when(REPOSITORY.saveTelemetry(any())).thenReturn(TELEMETRY);

        Telemetry result = telemetryService.saveTelemetry(TELEMETRY);
        assertEquals(TELEMETRY, result);
        verify(REPOSITORY, times(1)).saveTelemetry(any());
    }

    @Test
    public void getByRange() {
        when(REPOSITORY.getByRange(any())).thenReturn(List.of(TELEMETRY));

        List<Telemetry> result = telemetryService.getByRange("72d");
        assertEquals(1,result.size());
        assertEquals(TELEMETRY, result.get(0));
        verify(REPOSITORY, times(1)).getByRange(any());
    }

    @Test
    public void getLastData() {
        when(REPOSITORY.getLastData(any())).thenReturn(TELEMETRY);

        Object result = telemetryService.getLastData(UUID.randomUUID());
        assertEquals(TELEMETRY, result);
        verify(REPOSITORY, times(1)).getLastData(any());
    }
}