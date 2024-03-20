package com.stm.pegelhub.logic.metadata;

import com.stm.pegelhub.common.model.metadata.Connector;
import com.stm.pegelhub.logic.service.metadata.ConnectorService;
import com.stm.pegelhub.outbound.repository.metadata.ConnectorRepository;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Default implementation for {@code ConnectorService}.
 */
public final class ConnectorServiceImpl implements ConnectorService {

    private final ConnectorRepository connectorRepository;

    public ConnectorServiceImpl(ConnectorRepository connectorRepository) {
        this.connectorRepository = requireNonNull(connectorRepository);
    }

    /**
     * @param connector to save.
     * @return the saved {@link Connector}
     */
    @Override
    public Connector createConnector(Connector connector) {
        return connectorRepository.saveConnector(connector);
    }

    /**
     * @param uuid {@link UUID} of the connector.
     * @return the corresponding {@link Connector} to the specified {@link UUID}
     */
    @Override
    public Connector getConnectorById(UUID uuid) {
        return connectorRepository.getById(uuid);
    }

    /**
      * @return all saved {@link Connector}s
     */
    @Override
    public List<Connector> getAllConnectors() {
        return connectorRepository.getAllConnectors();
    }

    /**
     * @param connector to update.
     * @return the updated {@link Connector}
     */
    @Override
    public Connector updateConnectors(Connector connector) {
        return connectorRepository.update(connector);
    }

    /**
     * @param uuid of the connector to delete.
     */
    @Override
    public void deleteConnector(UUID uuid) {
        connectorRepository.deleteConnector(uuid);

    }
}
