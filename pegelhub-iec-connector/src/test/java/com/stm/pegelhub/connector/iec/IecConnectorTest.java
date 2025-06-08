package com.stm.pegelhub.connector.iec;

import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.PegelHubCommunicatorFactory;
import com.stm.pegelhub.lib.internal.ApplicationPropertiesImpl;
import com.stm.pegelhub.lib.internal.dto.SupplierSendDto;
import com.stm.pegelhub.lib.model.Measurement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class IecConnectorTest {

    private IecConnector connector;
    private MockedStatic<PegelHubCommunicatorFactory> communicatorFactoryMock;

    @Mock
    private PegelHubCommunicator mockCommunicator;
    @Mock
    private org.openmuc.j60870.Connection mockIecConnection;
    @Mock
    private ApplicationPropertiesImpl mockApplicationProperties;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        mockCommunicator = mock(PegelHubCommunicator.class);
        communicatorFactoryMock = Mockito.mockStatic(PegelHubCommunicatorFactory.class);
        communicatorFactoryMock.when(() -> PegelHubCommunicatorFactory.create(any(), any()))
                .thenReturn(mockCommunicator);

        String configPath = "src/test/resources/ConnectorTest.properties";

        when(mockCommunicator.getSystemTime())
                .thenReturn(Timestamp.valueOf(LocalDateTime.now()));

        when(mockCommunicator.getMeasurementsOfStation(eq("123"), any()))
                .thenReturn(List.of(
                        new Measurement(
                                Collections.singletonMap("level", 1.23),
                                Collections.singletonMap("info", "test")
                        )
                ));
        SupplierSendDto dummySupplierSendDto = new SupplierSendDto(
                "STATION_NUMBER_TEST",
                123,
                "Station Name",
                "Water Name",
                'A',
                null,
                null,
                null, null, null, null,
                null, null, null, null,
                null, null, null, null,
                null, null, null, null,
                null, null, null, null,
                null, null, null, null, null, null
        );
        when(mockApplicationProperties.getSupplier()).thenReturn(dummySupplierSendDto);
        ConnectorOptions options = ConfigLoader.readArguments(new String[]{configPath});

        connector = new IecConnector(mockCommunicator, mockIecConnection, mockApplicationProperties, options);
    }

    @AfterEach
    void tearDown() {
        connector.close();
        communicatorFactoryMock.close();
    }

    @Test
    void testSendsMeasurements() throws InterruptedException {
        Thread.sleep(1000);

        verify(mockCommunicator, atLeastOnce())
                .getMeasurementsOfStation(eq("123"), any());
    }
}