package com.stm.pegelhub.outbound.data;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.exceptions.InfluxException;
import com.stm.pegelhub.InfluxIntegrationTestBase;
import com.stm.pegelhub.common.model.data.Measurement;
import com.stm.pegelhub.outbound.db.DatabaseProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class InfluxMeasurementRepositoryTest extends InfluxIntegrationTestBase {

    private static final DatabaseProperties PROPERTIES = new DatabaseProperties("url", "org", "data", "token");
    private static final InfluxDBClient CLIENT = getInfluxDBDataClient();
    private InfluxMeasurementRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InfluxMeasurementRepository(CLIENT, PROPERTIES);
    }

    @Test
    public void constructorWithNullArgsThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new InfluxMeasurementRepository(null, PROPERTIES));
        assertThrows(NullPointerException.class, () -> new InfluxMeasurementRepository(CLIENT, null));
    }

    @Test
    void saveMeasurementThrowsNPE() {
        assertThrows(NullPointerException.class, () -> repository.storeMeasurements(null));
    }

    @Test
    void saveMeasurement() {
        UUID id = UUID.fromString("0d0f370f-dc56-472a-a895-1547ce0ed43c");
        Measurement measurement = new Measurement(
                id,
                LocalDateTime.now()
                        .minus(48, ChronoUnit.HOURS)
                        .truncatedTo(ChronoUnit.SECONDS),
                Map.of("m1", 10.1),
                Map.of());
        repository.storeMeasurements(List.of(measurement));

        Measurement queryMeasurement = repository.getLastData(id);
        assertEquals(measurement, queryMeasurement);
    }

    @Test
    void getByRangeWithInvalidValueThrowsIE() {
        assertThrows(InfluxException.class, () -> repository.getByRange(null));
        assertThrows(InfluxException.class, () -> repository.getByRange(""));
        assertThrows(InfluxException.class, () -> repository.getByRange("null"));
        assertThrows(InfluxException.class, () -> repository.getByRange("-3d"));
    }

    @Test
    void getByRange() {
        String range = "40h";
        List<Measurement> measurements = repository.getByRange(range);
        assertEquals(2, measurements.size());
    }

    @Test
    void getByShortRange() {
        String range = "16h";
        List<Measurement> measurements = repository.getByRange(range);
        assertEquals(1, measurements.size());
    }

    @Test
    void getByIdAndRange() {
        String range = "40h";
        UUID uuid = UUID.fromString("e27efad2-b947-48b1-928e-c25663597f1c");
        List<Measurement> measurements = repository.getByIDAndRange(uuid, range);
        assertEquals(2, measurements.size());
    }

    @Test
    void getLastDataWithWrongUUIDThrowsIE() {
        UUID uuid = UUID.fromString("e27efad9-b947-48b1-928e-c25663597f1c");
        assertThrows(InfluxException.class, () -> repository.getLastData(uuid));
    }

    @Test
    void getLastData() {
        String id = "e27efad2-b947-48b1-928e-c25663597f1c";
        Measurement lastData = repository.getLastData(UUID.fromString(id));
        assertEquals(id, lastData.measurement().toString());
        assertEquals(55.0, lastData.fields().get("Wasserstand"));
    }
}