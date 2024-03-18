package com.stm.pegelhub.inbound.metadata.controller.impl;

import com.stm.pegelhub.common.model.metadata.Connector;
import com.stm.pegelhub.inbound.metadata.dto.ConnectorDto;
import com.stm.pegelhub.logic.service.metadata.ConnectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.stm.pegelhub.common.util.ExampleData.CONNECTOR;
import static com.stm.pegelhub.inbound.dto.util.ExampleDtos.CREATE_CONNECTOR_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class HttpConnectorControllerTest {

    private HttpConnectorController sut;

    private static final ConnectorService SERVICE = mock(ConnectorService.class);

    @BeforeEach
    void setUp() {
        sut = new HttpConnectorController(SERVICE);
        reset(SERVICE);
    }

    @Test
    public void constructorShouldThrowNullPointerExceptionIfApiTokenServiceIsNull() {
        assertThrows(NullPointerException.class, () -> new HttpConnectorController(null));
    }

    @Test
    void testSaveConnector() {
        when(SERVICE.createConnector(any())).thenReturn(CONNECTOR);
        ConnectorDto expected = DomainToDtoConverter.convert(CONNECTOR);
        ConnectorDto actual = sut.saveConnector(CREATE_CONNECTOR_DTO);
        assertEquals(expected, actual);
    }

    @Test
    void testGetConnectorById() {
        UUID uuid = UUID.randomUUID();
        when(SERVICE.getConnectorById(uuid)).thenReturn(CONNECTOR);
        ConnectorDto expected = DomainToDtoConverter.convert(CONNECTOR);
        ConnectorDto actual = sut.getConnectorById(uuid);
        assertEquals(expected, actual);
    }

    @Test
    void testGetAllConnectors() {
        List<Connector> connectors = new ArrayList<>();
        connectors.add(CONNECTOR);
        when(SERVICE.getAllConnectors()).thenReturn(connectors);
        List<ConnectorDto> expected = DomainToDtoConverter.convert(connectors);
        List<ConnectorDto> actual = sut.getAllConnectors();
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteConnector() {
        UUID uuid = UUID.randomUUID();
        sut.deleteConnector(uuid);
        verify(SERVICE, times(1)).deleteConnector(uuid);
    }
}