package com.stm.pegelhub.configuration.metadata;

import com.stm.pegelhub.outbound.jpa.*;
import com.stm.pegelhub.outbound.metadata.*;
import com.stm.pegelhub.outbound.repository.metadata.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean configuration for the metadata repository layer.
 */
@Configuration
public class MetadataRepositoryConfiguration {

    /**
     * @param jpaConnectorRepository the {@link JpaConnectorRepository} that manages the storing and fetching
     *                               of the Connector table in the (current) Postgres database
     * @return the {@link JdbcConnectorRepository} that uses the {@link JpaConnectorRepository} to store and fetch the
     *                                data of the Connector table
     */
    @Bean
    public ConnectorRepository connectorRepository(JpaConnectorRepository jpaConnectorRepository) {
        return new JdbcConnectorRepository(jpaConnectorRepository);
    }

    /**
     * @param jpaContactRepository the {@link JpaContactRepository} that manages the storing and fetching
     *                               of the Contact table in the (current) Postgres database
     * @return the {@link JdbcContactRepository} that uses the {@link JpaContactRepository} to store and fetch the
     *                                data of the Contact table
     */
    @Bean
    public ContactRepository contactRepository(JpaContactRepository jpaContactRepository) {
        return new JdbcContactRepository(jpaContactRepository);
    }

    /**
     * @param jpaStationManufacturerRepository the {@link JpaStationManufacturerRepository} that manages the storing and fetching
     *                               of the StationManufacturer table in the (current) Postgres database
     * @return the {@link JdbcStationManufacturerRepository} that uses the {@link JpaStationManufacturerRepository} to store and fetch the
     *                                data of the StationManufacturer table
     */
    @Bean
    public StationManufacturerRepository stationManufacturerRepository(JpaStationManufacturerRepository jpaStationManufacturerRepository) {
        return new JdbcStationManufacturerRepository(jpaStationManufacturerRepository);
    }

    /**
     * @param jpaSupplierRepository the {@link JpaSupplierRepository} that manages the storing and fetching
     *                               of the Supplier table in the (current) Postgres database
     * @return the {@link JdbcSupplierRepository} that uses the {@link JpaSupplierRepository} to store and fetch the
     *                                data of the Supplier table
     */
    @Bean
    public SupplierRepository supplierRepository(JpaSupplierRepository jpaSupplierRepository) {
        return new JdbcSupplierRepository(jpaSupplierRepository);
    }

    /**
     * @param jpaTakerRepository the {@link JpaTakerRepository} that manages the storing and fetching
     *                               of the Taker table in the (current) Postgres database
     * @return the {@link JdbcTakerRepository} that uses the {@link JpaTakerRepository} to store and fetch the
     *                                data of the Taker table
     */
    @Bean
    public TakerRepository takerRepository(JpaTakerRepository jpaTakerRepository) {
        return new JdbcTakerRepository(jpaTakerRepository);
    }

    /**
     * @param jpaTakerServiceManufacturerRepository the {@link JpaTakerServiceManufacturerRepository} that manages the storing and fetching
     *                               of the TakerServiceManufacturer table in the (current) Postgres database
     * @return the {@link JdbcTakerServiceManufacturerRepository} that uses the {@link JpaTakerServiceManufacturerRepository} to store and fetch the
     *                                data of the TakerServiceManufacturer table
     */
    @Bean
    public TakerServiceManufacturerRepository takerServiceManufacturerRepository(JpaTakerServiceManufacturerRepository jpaTakerServiceManufacturerRepository) {
        return new JdbcTakerServiceManufacturerRepository(jpaTakerServiceManufacturerRepository);
    }

    /**
     * @param jpaApiTokenRepository the {@link JpaApiTokenRepository} that manages the storing and fetching of the API-tokens
     * @return the {@link JdbcApiTokenRepository} that uses the {@link JpaApiTokenRepository} to store and fetch the API-tokens
     */
    @Bean
    public ApiTokenRepository apiTokenRepository(JpaApiTokenRepository jpaApiTokenRepository) {
        return new JdbcApiTokenRepository(jpaApiTokenRepository);
    }
}
