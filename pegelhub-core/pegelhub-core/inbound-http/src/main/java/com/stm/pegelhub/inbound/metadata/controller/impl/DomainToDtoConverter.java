package com.stm.pegelhub.inbound.metadata.controller.impl;

import com.stm.pegelhub.common.model.metadata.*;
import com.stm.pegelhub.inbound.metadata.dto.*;

import java.util.List;

/**
 * Provider, which has methods to turn domain objects to dtos.
 */
final class DomainToDtoConverter {

    static ContactDto convert(Contact contact) {
        return new ContactDto(
                contact.getId(),
                contact.getOrganization(),
                contact.getContactPerson(),
                contact.getContactStreet(),
                contact.getContactPlz(),
                contact.getLocation(),
                contact.getContactCountry(),
                contact.getEmergencyNumber(),
                contact.getEmergencyNumberTwo(),
                contact.getEmergencyMail(),
                contact.getServiceNumber(),
                contact.getServiceNumberTwo(),
                contact.getServiceMail(),
                contact.getAdministrationPhoneNumber(),
                contact.getAdministrationPhoneNumberTwo(),
                contact.getAdministrationMail(),
                contact.getContactNodes()
        );
    }

    static ConnectorDto convert(Connector connector) {
        return new ConnectorDto(
                connector.getId(),
                connector.getConnectorNumber(),
                convert(connector.getManufacturer()),
                connector.getTypeDescription(),
                connector.getSoftwareVersion(),
                connector.getWorksFromDataVersion(),
                connector.getDataDefinition(),
                convert(connector.getSoftwareManufacturer()),
                convert(connector.getTechnicallyResponsible()),
                convert(connector.getOperationCompany()),
                connector.getNotes());
    }

    static StationManufacturerDto convert(StationManufacturer stationManufacturer) {
        return new StationManufacturerDto(
                stationManufacturer.getId(),
                stationManufacturer.getStationManufacturerName(),
                stationManufacturer.getStationManufacturerType(),
                stationManufacturer.getStationManufacturerFirmwareVersion(),
                stationManufacturer.getStationRemark()
        );
    }

    static SupplierDto convert(Supplier supplier) {
        return new SupplierDto(
                supplier.getId(),
                supplier.getStationNumber(),
                supplier.getStationId(),
                supplier.getStationName(),
                supplier.getStationWater(),
                supplier.getStationWaterType(),
                convert(supplier.getStationManufacturer()),
                convert(supplier.getConnector()),
                supplier.getRefreshRate().toMillis(),
                supplier.getAccuracy(),
                supplier.getMainUsage(),
                supplier.getDataCritically(),
                supplier.getStationBaseReferenceLevel(),
                supplier.getStationReferencePlace(),
                supplier.getStationWaterKilometer(),
                supplier.getStationWaterSide(),
                supplier.getStationWaterLatitude(),
                supplier.getStationWaterLongitude(),
                supplier.getStationWaterLatitudem(),
                supplier.getStationWaterLongitudem(),
                supplier.getHsw100(),
                supplier.getHsw(),
                supplier.getHswReference(),
                supplier.getMw(),
                supplier.getMwReference(),
                supplier.getRnw(),
                supplier.getRnwReference(),
                supplier.getHsq100(),
                supplier.getHsq(),
                supplier.getMq(),
                supplier.getRnq(),
                supplier.getChannelUse()
        );
    }

    static TakerDto convert(Taker taker) {
        return new TakerDto(
                taker.getId(),
                taker.getStationNumber(),
                taker.getStationId(),
                convert(taker.getTakerServiceManufacturer()),
                convert(taker.getConnector()),
                taker.getRefreshRate().toMillis()
        );
    }

    static TakerServiceManufacturerDto convert(TakerServiceManufacturer takerServiceManufacturer) {
        return new TakerServiceManufacturerDto(
                takerServiceManufacturer.getId(),
                takerServiceManufacturer.getTakerManufacturerName(),
                takerServiceManufacturer.getTakerSystemName(),
                takerServiceManufacturer.getStationManufacturerFirmwareVersion(),
                takerServiceManufacturer.getRequestRemark()
        );
    }

    static Object convert(Object object) {
        if (object instanceof ApiToken token) {
            return convert(token);
        } else if (object instanceof Contact contact) {
            return convert(contact);
        } else if (object instanceof Connector connector) {
            return convert(connector);
        } else if (object instanceof StationManufacturer stationManufacturer) {
            return convert(stationManufacturer);
        } else if (object instanceof Supplier supplier) {
            return convert(supplier);
        } else if (object instanceof Taker taker) {
            return convert(taker);
        } else if (object instanceof TakerServiceManufacturer takerServiceManufacturer) {
            return convert(takerServiceManufacturer);
        }
        return object;
    }

    static <S, T> List<S> convert(List<T> elements) {
        return (List<S>) elements.stream().map(DomainToDtoConverter::convert).toList();
    }
}
