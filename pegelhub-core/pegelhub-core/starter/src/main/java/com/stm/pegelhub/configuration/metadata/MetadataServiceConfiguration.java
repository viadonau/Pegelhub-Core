package com.stm.pegelhub.configuration.metadata;

import com.stm.pegelhub.logic.metadata.*;
import com.stm.pegelhub.logic.service.metadata.*;
import com.stm.pegelhub.outbound.repository.metadata.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean configuration for the metadata service layer.
 */
@Configuration
public class MetadataServiceConfiguration {

    /**
     * @param connectorRepository the {@link ConnectorRepository} in which the service stores and fetches the data
     * @return {@link ConnectorServiceImpl} which uses the given {@link ConnectorRepository} to store and fetch data
     */
    @Bean
    public ConnectorService connectorService(ConnectorRepository connectorRepository) {
        return new ConnectorServiceImpl(connectorRepository);
    }
    /**
     * @param contactRepository the {@link ContactRepository} in which the service stores and fetches the data
     * @return {@link ContactServiceImpl} which uses the given {@link ContactRepository} to store and fetch data
     */
    @Bean
    public ContactService contactService(ContactRepository contactRepository) {
        return new ContactServiceImpl(contactRepository);
    }

    /**
     * @param stationManufacturerRepository the {@link StationManufacturerRepository} in which the service stores and fetches the data
     * @return {@link StationManufacturerServiceImpl} which uses the given {@link StationManufacturerRepository} to store and fetch data
     */
    @Bean
    public StationManufacturerService stationManufacturerService(StationManufacturerRepository stationManufacturerRepository) {
        return new StationManufacturerServiceImpl(stationManufacturerRepository);
    }

    /**
     * @param supplierRepository {@link SupplierRepository} that is used to store and fetch supplier-specific data
     * @param stationManufacturerRepository {@link StationManufacturerRepository} that is used to fetch stationManufacturer-specific
     *                                                                           data and update the corresponding supplier-dataset
     * @param connectorRepository {@link ConnectorRepository} that is used to fetch connector-specific data and update the
     *                                                       corresponding supplier-dataset
     * @param contactRepository {@link ContactRepository} that is used to fetch contact-specific data and update the
     *                                                   corresponding supplier-dataset
     * @return {@link SupplierServiceImpl} which uses the {@link SupplierRepository} to store and fetch supplier-specific data
     */
    @Bean
    public SupplierService supplierService(SupplierRepository supplierRepository, StationManufacturerRepository stationManufacturerRepository, ConnectorRepository connectorRepository, ContactRepository contactRepository) {
        return new SupplierServiceImpl(supplierRepository, stationManufacturerRepository, connectorRepository, contactRepository);
    }

    /**
     * @param takerRepository {@link TakerRepository} that is used to store and fetch taker-specific data
     * @param takerServiceManufacturerRepository {@link TakerServiceManufacturerRepository} that is used to fetch takerServiceManufacturer-specific
     *                                                                           data and update the corresponding taker-dataset
     * @param connectorRepository {@link ConnectorRepository} that is used to fetch connector-specific data and update the
     *                                                       corresponding taker-dataset
     * @param contactRepository {@link ContactRepository} that is used to fetch contact-specific data and update the
     *                                                   corresponding taker-dataset
     * @return {@link TakerServiceImpl} which uses the {@link TakerRepository} to store and fetch taker-specific data
     */
    @Bean
    public TakerService takerService(TakerRepository takerRepository, TakerServiceManufacturerRepository takerServiceManufacturerRepository, ConnectorRepository connectorRepository, ContactRepository contactRepository) {
        return new TakerServiceImpl(takerRepository, takerServiceManufacturerRepository, connectorRepository, contactRepository);
    }

    /**
     * @param takerServiceManufacturerRepository {@link TakerServiceManufacturerRepository} that is used to store and fetch
     *                                                                                     the data from the takerServiceManufacturer table
     * @return {@link TakerServiceManufacturerServiceImpl} which uses {@link TakerServiceManufacturerRepository} to store and fetch
     * the data form the takerServiceManufacturer table
     */
    @Bean
    public TakerServiceManufacturerService takerServiceManufacturerService(TakerServiceManufacturerRepository takerServiceManufacturerRepository) {
        return new TakerServiceManufacturerServiceImpl(takerServiceManufacturerRepository);
    }

    /**
     * @param apiTokenRepository {@link ApiTokenRepository} that is used to manage the API-tokens
     * @return {@link ApiTokenServiceImpl} which uses {@link ApiTokenRepository} to manage the API-tokens
     */
    @Bean
    public ApiTokenService apiTokenService(ApiTokenRepository apiTokenRepository, ConnectorRepository connectorRepository) {
        return new ApiTokenServiceImpl(apiTokenRepository, connectorRepository);
    }

    /**
     * @param apiTokenRepository {@link ApiTokenRepository} that is used to manage the API-tokens
     * @return {@link AuthorizationServiceImpl} which uses {@link ApiTokenRepository} to authorize the user
     */
    @Bean
    public AuthorizationService authorizationService(ApiTokenRepository apiTokenRepository) {
        return new AuthorizationServiceImpl(apiTokenRepository);
    }
}
