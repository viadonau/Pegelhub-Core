package com.stm.pegelhub.connector.iec;

import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.internal.ApplicationPropertiesImpl;
import com.stm.pegelhub.lib.internal.dto.SupplierSendDto;
import com.stm.pegelhub.lib.model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.ASduType;
import org.openmuc.j60870.ie.IeScaledValue;
import org.openmuc.j60870.ie.InformationElement;
import org.openmuc.j60870.ie.InformationObject;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class AsduMeasurementHandlerTest {

    @Mock PegelHubCommunicator communicator;
    @Mock ConnectorOptions options;
    @Mock ApplicationPropertiesImpl properties;

    AsduMeasurementHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(communicator.getSystemTime()).thenReturn(Timestamp.valueOf(LocalDateTime.now()));

        handler = new AsduMeasurementHandler(communicator, options, properties);
    }

    /**
     * given a valid ASdu of type M_ME_NB_1 with a valid supplier and measurement,
     * the measurement should be processed and sent without errors.
     */
    @Test
    void validAsdu() {
        SupplierSendDto supplierDto = mock(SupplierSendDto.class);
        when(supplierDto.stationNumber()).thenReturn("123");
        when(properties.getSupplier()).thenReturn(supplierDto);

        Supplier supplier = new Supplier();
        supplier.setStationNumber("123");
        supplier.setChannelUse("WATER");
        when(communicator.getSuppliers()).thenReturn(List.of(supplier));

        IeScaledValue scaledValue = mock(IeScaledValue.class);
        when(scaledValue.getNormalizedValue()).thenReturn(5.5);
        when(scaledValue.getUnnormalizedValue()).thenReturn(550);

        InformationElement quality = mock(InformationElement.class);
        when(quality.toString()).thenReturn("GOOD");

        InformationElement[][] elements = { { scaledValue, quality } };

        InformationObject infoObject = mock(InformationObject.class);
        when(infoObject.getInformationObjectAddress()).thenReturn(10);
        when(infoObject.getInformationElements()).thenReturn(elements);

        ASdu aSdu = mock(ASdu.class);
        when(aSdu.getTypeIdentification()).thenReturn(ASduType.M_ME_NB_1);
        when(aSdu.getInformationObjects()).thenReturn(new InformationObject[] { infoObject });

        handler.handle(aSdu);

        verify(communicator, times(1)).sendMeasurements(argThat(measurements ->
                measurements.size() == 1 &&
                        measurements.get(0).getFields().get("NormalizedValue") == 5.5
        ));
    }

    /**
     * when no supplier is configured, no measurements are sent.
     */
    @Test
    void noSupplierConfigured() {
        when(properties.getSupplier()).thenReturn(null);

        ASdu aSdu = mock(ASdu.class);
        when(aSdu.getTypeIdentification()).thenReturn(ASduType.M_ME_NB_1);

        handler.handle(aSdu);
        verify(communicator, never()).sendMeasurements(any());
    }
}
