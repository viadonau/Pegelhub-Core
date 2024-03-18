package com.stm.pegelhub.logic.data;

import com.stm.pegelhub.common.model.data.Measurement;
import com.stm.pegelhub.common.model.metadata.Supplier;
import com.stm.pegelhub.logic.data.service.impl.MeasurementServiceImpl;
import com.stm.pegelhub.logic.domain.WriteMeasurement;
import com.stm.pegelhub.logic.domain.WriteMeasurements;
import com.stm.pegelhub.outbound.repository.data.MeasurementRepository;
import com.stm.pegelhub.outbound.repository.metadata.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.stm.pegelhub.common.util.ExampleData.MEASUREMENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

final class MeasurementServiceImplTest {

    private MeasurementServiceImpl measurementService;
    private static final SupplierRepository SUPPLIER_REPOSITORY = mock(SupplierRepository.class);
    private static final MeasurementRepository MEASUREMENT_REPOSITORY = mock(MeasurementRepository.class);

    @BeforeEach
    public void prepare() {
        measurementService = new MeasurementServiceImpl(SUPPLIER_REPOSITORY, MEASUREMENT_REPOSITORY);
        reset(SUPPLIER_REPOSITORY);
        reset(MEASUREMENT_REPOSITORY);
    }

    @Test
    public void constructorWithNullArgsThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new MeasurementServiceImpl(SUPPLIER_REPOSITORY, null));
        assertThrows(NullPointerException.class, () -> new MeasurementServiceImpl(null, MEASUREMENT_REPOSITORY));
    }

    @Test
    public void saveMeasurement() {
        when(SUPPLIER_REPOSITORY.getSupplierIdForAuthId(any())).thenReturn(UUID.randomUUID());
        measurementService.writeMeasurements(new WriteMeasurements(List.of(new WriteMeasurement(LocalDateTime.now(), Map.of("", 1.0), Map.of()))));
        verify(MEASUREMENT_REPOSITORY, times(1)).storeMeasurements(any());
    }

    @Test
    public void getByRange() {
        when(MEASUREMENT_REPOSITORY.getByRange(any())).thenReturn(List.of(MEASUREMENT));

        List<Measurement> result = measurementService.getByRange("72d");
        assertEquals(1, result.size());
        assertEquals(MEASUREMENT, result.get(0));
        verify(MEASUREMENT_REPOSITORY, times(1)).getByRange(any());
    }

    @Test
    public void getBySupplierAndRange() {
        when(SUPPLIER_REPOSITORY.findByStationNumber(any())).thenReturn(Optional.of(new Supplier()));
        when(MEASUREMENT_REPOSITORY.getByIDAndRange(any(), any())).thenReturn(List.of(MEASUREMENT));

        List<Measurement> result = measurementService.getBySupplierAndRange("sup", "72d");
        assertEquals(1, result.size());
        assertEquals(MEASUREMENT, result.get(0));
        verify(MEASUREMENT_REPOSITORY, times(1)).getByIDAndRange(any(), any());
    }

    @Test
    public void getLastData() {
        when(MEASUREMENT_REPOSITORY.getLastData(any())).thenReturn(MEASUREMENT);

        Measurement result = measurementService.getLastData(UUID.randomUUID());
        assertEquals(MEASUREMENT, result);
        verify(MEASUREMENT_REPOSITORY, times(1)).getLastData(any());
    }
}