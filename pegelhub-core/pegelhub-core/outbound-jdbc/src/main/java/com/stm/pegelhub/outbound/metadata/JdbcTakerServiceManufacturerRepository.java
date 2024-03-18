package com.stm.pegelhub.outbound.metadata;

import com.stm.pegelhub.common.model.metadata.TakerServiceManufacturer;
import com.stm.pegelhub.outbound.jpa.JpaTakerServiceManufacturerRepository;
import com.stm.pegelhub.outbound.repository.metadata.TakerServiceManufacturerRepository;

import java.util.List;
import java.util.UUID;

/**
 * JDBC Implementation of the Interface {@code ApiTokenRepository}.
 */
public class JdbcTakerServiceManufacturerRepository implements TakerServiceManufacturerRepository {
    private final JpaTakerServiceManufacturerRepository jpaTakerServiceManufacturerRepository;

    public JdbcTakerServiceManufacturerRepository(JpaTakerServiceManufacturerRepository jpaTakerServiceManufacturerRepository) {
        this.jpaTakerServiceManufacturerRepository = jpaTakerServiceManufacturerRepository;
    }

    /**
     * @param takerServiceManufacturer to save.
     * @return the saved {@link TakerServiceManufacturer}
     */
    @Override
    public TakerServiceManufacturer saveTakerServiceManufacturer(TakerServiceManufacturer takerServiceManufacturer) {
        if (takerServiceManufacturer.getId() == null) {
            takerServiceManufacturer = takerServiceManufacturer.withId(UUID.randomUUID());
        }
        return JpaToDomainConverter.convert(jpaTakerServiceManufacturerRepository.save(DomainToJpaConverter.convert(takerServiceManufacturer)));
    }

    /**
     * @param uuid of the taker service manufacturer.
     * @return the corresponding {@link TakerServiceManufacturer}
     */
    @Override
    public TakerServiceManufacturer getById(UUID uuid) {
        return jpaTakerServiceManufacturerRepository.findById(uuid).map(JpaToDomainConverter::convert).orElse(null);
    }

    /**
     * @return all saved {@link TakerServiceManufacturer}s
     */
    @Override
    public List<TakerServiceManufacturer> getAllTakerServiceManufacturers() {
        return JpaToDomainConverter.convert(jpaTakerServiceManufacturerRepository.findAll());
    }

    /**
     *
     * @param takerServiceManufacturer to update.
     * @return the updated {@link TakerServiceManufacturer}
     */
    @Override
    public TakerServiceManufacturer update(TakerServiceManufacturer takerServiceManufacturer) {
        return JpaToDomainConverter.convert(jpaTakerServiceManufacturerRepository.save(DomainToJpaConverter.convert(takerServiceManufacturer)));
    }

    /**
     * @param uuid of the {@link TakerServiceManufacturer} to delete.
     */
    @Override
    public void deleteTakerServiceManufacturer(UUID uuid) {
        jpaTakerServiceManufacturerRepository.delete(jpaTakerServiceManufacturerRepository.findById(uuid).get());
    }
}
