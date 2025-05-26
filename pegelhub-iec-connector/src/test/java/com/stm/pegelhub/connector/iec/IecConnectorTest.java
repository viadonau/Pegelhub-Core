package com.stm.pegelhub.connector.iec;

import com.stm.pegelhub.lib.PegelHubCommunicator;
import com.stm.pegelhub.lib.PegelHubCommunicatorFactory;
import com.stm.pegelhub.lib.model.Measurement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class IecConnectorTest {

    private IecConnector connector;
    private ConnectorOptions options;
    private PegelHubCommunicator mockCommunicator;
    private MockedStatic<PegelHubCommunicatorFactory> communicatorFactoryMock;

    @BeforeEach
    void setUp() throws Exception {
        mockCommunicator = mock(PegelHubCommunicator.class);
        communicatorFactoryMock = Mockito.mockStatic(PegelHubCommunicatorFactory.class);
        communicatorFactoryMock.when(() -> PegelHubCommunicatorFactory.create(any(URL.class), anyString()))
                .thenReturn(mockCommunicator);

        String configPath = "src/test/resources/ConnectorTest.properties";
        options = ConfigLoader.readArguments(new String[]{configPath});

        when(mockCommunicator.getSystemTime())
                .thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(mockCommunicator.getMeasurementsOfStation(eq("123"), any()))
                .thenReturn(List.of(
                        new Measurement(
                                Collections.singletonMap("level", 1.23),
                                Collections.singletonMap("info", "test")
                        )
                ));

        connector = new IecConnector(options);

        communicatorFactoryMock.clearInvocations();
    }


    @AfterEach
    void tearDown() {
        if (connector != null) {
            connector.close();
        }
        if (communicatorFactoryMock != null) {
            communicatorFactoryMock.close();
        }
    }

    @Test
    void testSendsMeasurements() throws InterruptedException {
        Thread.sleep(1000);

        verify(mockCommunicator, atLeastOnce())
                .getMeasurementsOfStation(eq("123"), any());
    }

    @Test
    void testGetCommunicator() throws Exception {
        Field commField = IecConnector.class.getDeclaredField("communicator");
        commField.setAccessible(true);
        commField.set(connector, null);

        Method getComm = IecConnector.class.getDeclaredMethod("getCommunicator", ConnectorOptions.class);
        getComm.setAccessible(true);
        PegelHubCommunicator returned = (PegelHubCommunicator) getComm.invoke(connector, options);

        assertSame(mockCommunicator, returned);

        communicatorFactoryMock.verify(() ->
                        PegelHubCommunicatorFactory.create(
                                new URL(String.format("https://%s:%d/",
                                        options.coreAddress().getHostAddress(),
                                        options.corePort()
                                )),
                                options.propertyFileName()
                        ),
                times(1)
        );
    }
}
