package com.stm.pegelhub.outbound.metadata;

import com.stm.pegelhub.common.model.metadata.Supplier;
import com.stm.pegelhub.outbound.jpa.JpaSupplierRepository;
import com.stm.pegelhub.outbound.repository.metadata.SupplierRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.stm.pegelhub.outbound.metadata.JpaToDomainConverter.convert;

/**
 * JDBC Implementation of the Interface {@code SupplierRepository}.
 */
public class JdbcSupplierRepository implements SupplierRepository {
    private final JpaSupplierRepository jpaSupplierRepository;

    public JdbcSupplierRepository(JpaSupplierRepository jpaSupplierRepository) {
        this.jpaSupplierRepository = jpaSupplierRepository;
    }

    /**
     * @param supplier to save.
     * @return the saved {@link Supplier}
     */
    @Override
    public Supplier saveSupplier(Supplier supplier) {
        if (supplier.getId() == null) {
            supplier = supplier.withId(UUID.randomUUID());
        }
        return convert(jpaSupplierRepository.save(DomainToJpaConverter.convert(supplier)));
    }

    /**
     *
     * @param uuid of the supplier.
     * @return the corresponding {@Supplier} to the specified UUID
     */
    @Override
    public Supplier getById(UUID uuid) {
        return jpaSupplierRepository.findById(uuid).map(JpaToDomainConverter::convert).orElse(null);
    }

    /**
     * @return all saved {@link Supplier}s
     */
    @Override
    public List<Supplier> getAllSuppliers() {
        return convert(jpaSupplierRepository.findAll());
    }

    /**
     * @param supplier to update.
     * @return the updated {@link Supplier}
     */
    @Override
    public Supplier update(Supplier supplier) {
        return convert(jpaSupplierRepository.save(DomainToJpaConverter.convert(supplier)));
    }

    /**
     * @param uuid of the {@link Supplier} to delete.
     */
    @Override
    public void deleteSupplier(UUID uuid) {
        jpaSupplierRepository.delete(jpaSupplierRepository.findById(uuid).get());
    }

    /**
     * @param uuid of the {@link Supplier} for authentication
     * @return the corresponding {@link Supplier} to the given {@link UUID}
     */
    @Override
    public UUID getSupplierIdForAuthId(UUID uuid) {
       return jpaSupplierRepository.getSupplier(uuid);
    }

    /**
     * @param stationNumber of the desired {@link Supplier}
     * @return the corresponding {@link Supplier} to the given station.
     */
    @Override
    public Optional<Supplier> findByStationNumber(String stationNumber) {
        return jpaSupplierRepository.findFirstByStationNumber(stationNumber).map(JpaToDomainConverter::convert);
    }

    @Override
    public UUID getConnectorID(UUID uuid) {
        return jpaSupplierRepository.getConnectorID(uuid);
    }
}
