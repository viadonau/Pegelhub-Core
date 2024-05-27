package com.stm.pegelhub.connector.tstp.service.impl;

import com.stm.pegelhub.connector.tstp.ConnectorOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TstpConfigServiceImplTest {
    private static final String TEST_TSTP_CONFIG_PATH = "src/test/resources/test_tstp_config.properties";
    private static final String TEST_CORE_PROPERTIES_PATH = "test_core.properties";

    @InjectMocks
    private TstpConfigServiceImpl tstpConfigService;

    @BeforeEach
    void setUp() throws IOException {
        createTestPropertiesFile();
        tstpConfigService = new TstpConfigServiceImpl(TEST_TSTP_CONFIG_PATH, TEST_CORE_PROPERTIES_PATH);
    }

    @Test
    void testGetConnectorOptions_returnsCorrectOptions() throws Exception {
        ConnectorOptions options = tstpConfigService.getConnectorOptions();

        assertEquals("127.0.0.1", options.coreAddress());
        assertEquals(8080, options.corePort());
        assertEquals("tstp.test.com", options.tstpAddress());
        assertEquals(8030, options.tstpPort());
        assertEquals(Duration.ofHours(1), options.readDelay());
        assertEquals(TEST_CORE_PROPERTIES_PATH, options.propertiesFile());
    }

    @Test
    void testParseDurationString_validInputs() {
        assertEquals(Duration.ofHours(1), tstpConfigService.parseDurationString("1h"));
        assertEquals(Duration.ofMinutes(30), tstpConfigService.parseDurationString("30m"));
        assertEquals(Duration.ofSeconds(45), tstpConfigService.parseDurationString("45s"));
    }

    @Test
    void testParseDurationString_emptyString_returnsDefault() {
        assertEquals(Duration.ofHours(2), tstpConfigService.parseDurationString(""));
    }

    @Test
    void testParseDurationString_invalidUnit_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> tstpConfigService.parseDurationString("10x"));
    }

    private void createTestPropertiesFile() throws IOException {
        Properties properties = new Properties();
        properties.setProperty("connector.readDelay", "1h");
        properties.setProperty("core.address", "127.0.0.1");
        properties.setProperty("core.port", "8080");
        properties.setProperty("tstp.address", "tstp.test.com");
        properties.setProperty("tstp.port", "8030");

        try (FileOutputStream out = new FileOutputStream(TEST_TSTP_CONFIG_PATH)) {
            properties.store(out, null);
        }
    }
}
