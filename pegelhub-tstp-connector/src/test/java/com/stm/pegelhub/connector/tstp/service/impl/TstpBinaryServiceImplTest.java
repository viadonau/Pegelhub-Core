package com.stm.pegelhub.connector.tstp.service.impl;

import com.stm.pegelhub.lib.model.Measurement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TstpBinaryServiceImplTest {
    private TstpBinaryServiceImpl tstpBinaryService;

    @BeforeEach
    public void setUp() {
        tstpBinaryService = new TstpBinaryServiceImpl();
    }

    @Test
    public void testDecode_binaryWithOneMeasurement_listWithOneMeasurement() {
        byte[] toDecode = new byte[]{
                0, 7, -38, 8, 3, 13, 30, 0, 68, 38, 44, -51
        };
        List<Measurement> measurements = tstpBinaryService.decode(toDecode);

        assertEquals(1, measurements.size());
        Measurement measurement = measurements.get(0);
        assertEquals(LocalDateTime.of(2010, 8, 3, 13, 30, 0), measurement.getTimestamp());
        assertEquals(664.7, measurement.getFields().get("value"));
    }

    @Test
    public void testEncode_oneMeasurement_binaryWithOneMeasurement() {
        List<Measurement> measurements = new ArrayList<>();
        LocalDateTime dateTime = LocalDateTime.of(2010, 8, 3, 13, 30, 0);
        HashMap<String, Double> valueMap = new HashMap<>();
        valueMap.put("value", 664.7);
        measurements.add(new Measurement(dateTime, valueMap, new HashMap<>()));

        byte[] encodedBytes = tstpBinaryService.encode(measurements);

        byte[] expectedBytes = new byte[]{
                0, 7, -38, 8, 3, 13, 30, 0, 68, 38, 44, -51
        };

        assertArrayEquals(expectedBytes, encodedBytes);
    }

    @Test
    public void testDecode_emptyBinary_emptyList() {
        byte[] toDecode = new byte[0];
        List<Measurement> measurements = tstpBinaryService.decode(toDecode);

        assertEquals(0, measurements.size());
    }

    @Test
    public void testEncode_emptyList_emptyBinary() {
        List<Measurement> measurements = new ArrayList<>();
        byte[] encodedBytes = tstpBinaryService.encode(measurements);

        assertArrayEquals(new byte[0], encodedBytes);
    }

    @Test
    public void testDecode_invalidBinaryLength_emptyList() {
        byte[] toDecode = new byte[]{0, 1, 0x07};
        List<Measurement> measurements = tstpBinaryService.decode(toDecode);

        assertEquals(0, measurements.size());
    }

    @Test
    public void testEncode_listWithMultipleMeasurements_binaryWithMultipleMeasurements() {
        List<Measurement> measurements = new ArrayList<>();
        LocalDateTime dateTime1 = LocalDateTime.of(2010, 8, 3, 13, 30, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2024, 5, 24, 7, 44, 37);
        HashMap<String, Double> valueMap1 = new HashMap<>();
        valueMap1.put("value", 664.7);
        HashMap<String, Double> valueMap2 = new HashMap<>();
        valueMap2.put("value", 351.0);
        measurements.add(new Measurement(dateTime1, valueMap1, new HashMap<>()));
        measurements.add(new Measurement(dateTime2, valueMap2, new HashMap<>()));

        byte[] encodedBytes = tstpBinaryService.encode(measurements);

        byte[] expectedBytes = new byte[]{
                0, 7, -38, 8, 3, 13, 30, 0, 68, 38, 44, -51,
                0, 7, -24, 5, 24, 7, 44, 37, 67, -81, -128, 0
        };

        assertArrayEquals(expectedBytes, encodedBytes);
    }

    @Test
    public void testDecode_binaryWithMultipleMeasurements_listWithMultipleMeasurements() {
        byte[] toDecode = new byte[]{
                0, 7, -38, 8, 3, 13, 30, 0, 68, 38, 44, -51,
                0, 7, -24, 5, 24, 7, 44, 37, 67, -81, -128, 0
        };
        List<Measurement> measurements = tstpBinaryService.decode(toDecode);

        assertEquals(2, measurements.size());
        Measurement measurement1 = measurements.get(0);
        assertEquals(LocalDateTime.of(2010, 8, 3, 13, 30, 0), measurement1.getTimestamp());
        assertEquals(664.7, measurement1.getFields().get("value"));
        Measurement measurement2 = measurements.get(1);
        assertEquals(LocalDateTime.of(2024, 5, 24, 7, 44, 37), measurement2.getTimestamp());
        assertEquals(351.0, measurement2.getFields().get("value"));
    }
}
