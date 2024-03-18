package com.stm.pegelhub.inbound.metadata.dto;

import java.util.UUID;

import static com.stm.pegelhub.common.util.Validations.requireNotEmpty;
import static com.stm.pegelhub.common.util.Validations.requireSEThan;

/**
 * DTO to create connector data.
 */
public record CreateConnectorDto(String connectorNumber, CreateContactDto manufacturer, String typeDescription,
                                 String softwareVersion, String worksFromDataVersion, String dataDefinition,
                                 CreateContactDto softwareManufacturer, CreateContactDto technicallyResponsible,
                                 CreateContactDto operationCompany, String notes, UUID apiToken) {
    public CreateConnectorDto {
        requireSEThan(requireNotEmpty(connectorNumber), 50);
        requireSEThan(typeDescription, 100);
        requireSEThan(softwareVersion, 20);
        requireSEThan(worksFromDataVersion, 20);
        requireSEThan(dataDefinition, 20);
        requireSEThan(notes, 255);
    }
}
