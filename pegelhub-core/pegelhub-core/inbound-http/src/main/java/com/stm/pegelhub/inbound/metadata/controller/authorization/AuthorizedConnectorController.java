package com.stm.pegelhub.inbound.metadata.controller.authorization;

import com.stm.pegelhub.inbound.metadata.controller.ConnectorController;
import com.stm.pegelhub.inbound.metadata.dto.ConnectorDto;
import com.stm.pegelhub.inbound.metadata.dto.CreateConnectorDto;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

//TODO: why does this class even exist? It doesn't authorize anything. It just forwards to the underlying controller?
/**
 * Implementation of the Interface {@code ConnectorController}.
 * Performs authorization of requests where necessary before they are delegated to the underlying controller.
 */
public class AuthorizedConnectorController implements ConnectorController {

    private final ConnectorController delegate;

    public AuthorizedConnectorController(ConnectorController delegate) {
        this.delegate = requireNonNull(delegate);
    }

    public ConnectorDto saveConnector(CreateConnectorDto connector) {
        return delegate.saveConnector(connector);
    }

    public ConnectorDto getConnectorById(UUID uuid) {
        return delegate.getConnectorById(uuid);
    }

    public List<ConnectorDto> getAllConnectors() {
        return delegate.getAllConnectors();
    }

    public void deleteConnector(UUID uuid) {
        delegate.deleteConnector(uuid);
    }
}
