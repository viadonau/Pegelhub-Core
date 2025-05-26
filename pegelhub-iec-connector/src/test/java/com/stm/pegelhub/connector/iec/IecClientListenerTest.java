package com.stm.pegelhub.connector.iec;

import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.internal.ApplicationPropertiesImpl;
import com.stm.pegelhub.lib.internal.dto.SupplierSendDto;
import com.stm.pegelhub.lib.model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.Connection;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IecClientEventListenerTest {

    @Mock
    PegelHubCommunicator communicator;

    @Mock
    ApplicationPropertiesImpl properties;

    @Mock
    ConnectorOptions options;

    @Mock
    Connection connection;

    IecClientEventListener listener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(options.telemetryCycleTime()).thenReturn(Duration.ofSeconds(1));
        when(communicator.getSystemTime()).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
    }

    /**
     * checks if newAsdu() is delegating correct to the class AsduMeasurementHandler
     */
    @Test
    void newASduToMeasurementHandler() {
        ASdu mockAsdu = mock(ASdu.class);
        AsduMeasurementHandler mockHandler = mock(AsduMeasurementHandler.class);

        IecClientEventListener listenerWithMockedHandler = new IecClientEventListener(communicator, options, properties, connection) {
            final AsduMeasurementHandler measurementHandler = mockHandler;

            @Override
            public void newASdu(ASdu aSdu) {
                measurementHandler.handle(aSdu);
            }
        };

        listenerWithMockedHandler.newASdu(mockAsdu);
        verify(mockHandler, times(1)).handle(mockAsdu);
    }

    /**
     * if a supplier is given, a telemetry message should be sent to communicator
     */
    @Test
    void sendTelemetryValidSupplier() throws Exception {
        Supplier supplier = new Supplier();
        supplier.setId(UUID.randomUUID().toString());
        supplier.setStationNumber("123");

        when(communicator.getSuppliers()).thenReturn(List.of(supplier));
        SupplierSendDto mockSupplierDto = mock(SupplierSendDto.class);
        when(mockSupplierDto.stationNumber()).thenReturn("123");
        when(properties.getSupplier()).thenReturn(mockSupplierDto);
        when(options.telemetryCycleTime()).thenReturn(Duration.ofSeconds(1));

        listener = new IecClientEventListener(communicator, options, properties, connection);
        listener.sendTelemetry();

        verify(communicator, atLeastOnce()).sendTelemetry(any());

    }

    /**
     * if no supplier is given, no telemetry should be sent
     */
    @Test
    void sendTelemetryNoMatchingSupplier() {
        when(communicator.getSuppliers()).thenReturn(List.of()); // keine Supplier
        SupplierSendDto mockSupplierDto = mock(SupplierSendDto.class);
        when(mockSupplierDto.stationNumber()).thenReturn("999");
        when(properties.getSupplier()).thenReturn(mockSupplierDto);
        when(options.telemetryCycleTime()).thenReturn(Duration.ofSeconds(1));

        listener = new IecClientEventListener(communicator, options, properties, connection);
        listener.sendTelemetry();

        verify(communicator, never()).sendTelemetry(any());
    }
}
