package com.stm.pegelhub.connector.iec;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;

public class ParameterTest {

    @Test
    void testReadArguments_validInput() throws Exception {
        String[] args = {"src/main/resources/config.properties"};

        ConnectorOptions params = Main.readArguments(args);

        assertNotNull(params);
        assertEquals("properties.yaml", params.propertyFileName());
        assertTrue(params.isReadingFromIec());
        assertEquals(InetAddress.getByName("192.168.2.29"), params.coreAddress());
        assertEquals(8081, params.corePort());
        assertEquals(2404, params.iec_port());
        assertEquals(1, params.common_address());
        assertEquals(1, params.start_dt_retries());
        assertEquals(20000, params.connection_timeout());
        assertEquals(5000, params.message_fragment_timeout());
        assertEquals(Duration.parse("PT5000S"), params.telemetryCycleTime());
        assertEquals("PT30S", params.delay().toString());
        assertEquals("nr1", params.stationNumbers().get(0));
    }

    @Test
    void testReadArguments_missingArgument() throws IOException {
        String path = "src/test/resources/test.properties";
        String[] args = { path };
        ConnectorOptions params = Main.readArguments(args);

        assertEquals("localhost/127.0.0.1", params.iec_host().toString());
    }


    @Test
    void testReadArguments_invalidFormat() throws IOException {
        String[] args = {"notAParameter"};

        assertThrows(IOException.class, () -> {
            Main.readArguments(args);
        });
    }
}
