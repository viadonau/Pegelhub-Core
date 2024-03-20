package com.stm.pegelhub.configuration.metadata;

import com.stm.pegelhub.inbound.metadata.controller.*;
import com.stm.pegelhub.inbound.metadata.controller.authorization.*;
import com.stm.pegelhub.inbound.metadata.controller.impl.*;
import com.stm.pegelhub.logic.service.metadata.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean configuration for the metadata controller layer.
 */
@Configuration
public class MetadataControllerConfiguration {

    /**
     * @param connectorService The {@link ConnectorService} that implements the calls to
     *                         the {@link com.stm.pegelhub.outbound.repository.metadata.ConnectorRepository ConnectorRepository}.
     * @return the {@link AuthorizedConnectorController} that calls the {@link ConnectorService}.
     */
    @Bean
    public ConnectorController connectorController(ConnectorService connectorService) {
        return new AuthorizedConnectorController(new HttpConnectorController(connectorService));
    }

    /**
     * @param contactService The {@link ContactService} that implements the calls to
     *                         the {@link com.stm.pegelhub.outbound.repository.metadata.ContactRepository ContactRepository}.
     * @return the {@link AuthorizedContactController} that calls the {@link ContactService}.
     */
    @Bean
    public ContactController contactController(ContactService contactService) {
        return new AuthorizedContactController(new HttpContactController(contactService));
    }

    /**
     * @param stationManufacturerService The {@link StationManufacturerService} that implements the calls to
     *                         the {@link com.stm.pegelhub.outbound.repository.metadata.StationManufacturerRepository StationManufacturerRepository}.
     * @return the {@link AuthorizedStationManufacturerController} that calls the {@link StationManufacturerService}.
     */
    @Bean
    public StationManufacturerController stationManufacturerController(StationManufacturerService stationManufacturerService) {
        return new AuthorizedStationManufacturerController(new HttpStationManufacturerController(stationManufacturerService));
    }

    /**
     * @param authorizationService The {@link AuthorizationService} that checks whether the API-token from the supplier is valid.
     * @param supplierService the {@link SupplierService}  that stores and fetches the supplier
     * @return the {@link AuthorizedSupplierController} that calls the {@link SupplierService} if the API-token is valid.
     */
    @Bean
    public SupplierController supplierController(AuthorizationService authorizationService, SupplierService supplierService) {
        return new AuthorizedSupplierController(authorizationService, new HttpSupplierController(supplierService));
    }

    /**
     * @param authorizationService The {@link AuthorizationService} that checks whether the API-token from the taker is valid.
     * @param takerService the {@link TakerService}  that stores and fetches the taker
     * @return the {@link AuthorizedTakerController} that calls the {@link TakerService} if the API-token is valid.
     */
    @Bean
    public TakerController takerController(AuthorizationService authorizationService, TakerService takerService) {
        return new AuthorizedTakerController(authorizationService, new HttpTakerController(takerService));
    }

    /**
     * @param takerServiceManufacturerService The {@link TakerServiceManufacturerService} that implements the calls to
     *      *                         the {@link com.stm.pegelhub.outbound.repository.metadata.TakerServiceManufacturerRepository TakerServiceManufacturerRepository}
     * @return the {@link AuthorizedTakerServiceManufacturerController} that calls the {@link TakerServiceManufacturerService}.
     */
    //TODO rename method to "takerServiceManufacturerController"
    @Bean
    public TakerServiceManufacturerController takerControllerManufacturerController(TakerServiceManufacturerService takerServiceManufacturerService) {
        return new AuthorizedTakerServiceManufacturerController(new HttpTakerServiceManufacturerController(takerServiceManufacturerService));
    }

    /**
     * @param apiTokenService The {@link ApiTokenService} that manages the API-Tokens (validate, create, invalidate, store...)
     * @return the {@link AuthorizedApiTokenController} that calls the {@link ApiTokenService}.
     */
    @Bean
    public ApiTokenController apiTokenController(ApiTokenService apiTokenService) {
        return new AuthorizedApiTokenController(new HttpApiTokenController(apiTokenService));
    }

}
