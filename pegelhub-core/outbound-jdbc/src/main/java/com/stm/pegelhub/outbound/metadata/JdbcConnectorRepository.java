package com.stm.pegelhub.outbound.metadata;

import com.stm.pegelhub.common.model.metadata.Connector;
import com.stm.pegelhub.outbound.jpa.JpaConnectorRepository;
import com.stm.pegelhub.outbound.repository.metadata.ConnectorRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JDBC Implementation of the Interface {@code ConnectorRepository}.
 */
public class JdbcConnectorRepository implements ConnectorRepository {

    private final JpaConnectorRepository jpaConnectorRepository;

    public JdbcConnectorRepository(JpaConnectorRepository jpaConnectorRepository) {
        this.jpaConnectorRepository = jpaConnectorRepository;
    }

    /**
     * @param connector to save.
     * @return the saved {@link Connector}
     */
    @Override
    public Connector saveConnector(Connector connector) {
        if (connector.getId() == null) {
            connector = connector.withId(UUID.randomUUID());
        }
        return JpaToDomainConverter.convert(jpaConnectorRepository.save(DomainToJpaConverter.convert(connector)));
    }

    /**
     * @param uuid {@link UUID} of the {@Connector} to be searched for.
     * @return the token corresponding to the specified {@link UUID}
     */
    @Override
    public Connector getById(UUID uuid) {
        return jpaConnectorRepository.findById(uuid).map(JpaToDomainConverter::convert).orElse(null);
    }

    /**
     * @return all saved {@link Connector}s
     */
    @Override
    public List<Connector> getAllConnectors() {
        return JpaToDomainConverter.convert(jpaConnectorRepository.findAll());
    }

    /**
     * @param connector {@link Connector} to update.
     * @return the updated {@link Connector}
     */
    @Override
    public Connector update(Connector connector) {
        return JpaToDomainConverter.convert(jpaConnectorRepository.save(DomainToJpaConverter.convert(connector)));
    }

    /**
     * @param uuid of the {@link Connector} to delete.
     */
    @Override
    public void deleteConnector(UUID uuid) {
        jpaConnectorRepository.delete(jpaConnectorRepository.findById(uuid).get());
    }

    /**
     * @param connectorNumber the name of the {@link Connector}.
     * @return the {@link Connector} corresponding to the specified connectorNumber
     */
    @Override
    public Optional<Connector> findByConnectorNumber(String connectorNumber) {
        return jpaConnectorRepository.findFirstByConnectorNumber(connectorNumber).map(JpaToDomainConverter::convert);
    }

    public Optional<Connector> findByUUUId(UUID uuid){
        return jpaConnectorRepository.findByUUID(uuid).map(JpaToDomainConverter::convert);
    }

}
