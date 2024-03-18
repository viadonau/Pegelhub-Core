package com.stm.pegelhub.inbound.metadata.controller.impl;

import com.stm.pegelhub.inbound.metadata.controller.ConnectorController;
import com.stm.pegelhub.inbound.metadata.dto.ConnectorDto;
import com.stm.pegelhub.inbound.metadata.dto.CreateConnectorDto;
import com.stm.pegelhub.logic.service.metadata.ConnectorService;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of the Interface {@code ConnectorController}.
 */
public class HttpConnectorController implements ConnectorController {

    private final ConnectorService connectorService;

    public HttpConnectorController(ConnectorService connectorService) {
        this.connectorService = requireNonNull(connectorService);
    }

    @Override
    public ConnectorDto saveConnector(CreateConnectorDto connector) {
        return DomainToDtoConverter.convert(connectorService.createConnector(DtoToDomainConverter.convert(connector)));
    }

    @Override
    public ConnectorDto getConnectorById(UUID uuid) {
        return DomainToDtoConverter.convert(connectorService.getConnectorById(uuid));
    }

    @Override
    public List<ConnectorDto> getAllConnectors() {
        return DomainToDtoConverter.convert(connectorService.getAllConnectors());
    }

    @Override
    public void deleteConnector(UUID uuid) {
        connectorService.deleteConnector(uuid);
    }
}
