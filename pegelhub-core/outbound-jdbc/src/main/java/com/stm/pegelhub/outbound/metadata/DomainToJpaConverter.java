package com.stm.pegelhub.outbound.metadata;

import com.stm.pegelhub.common.model.metadata.*;
import com.stm.pegelhub.logic.holder.AuthTokenIdHolder;
import com.stm.pegelhub.outbound.entity.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Maps domain classes to their respective JPA classes.
 */

class DomainToJpaConverter {

    static JpaContact convert(Contact contact) {
        return new JpaContact(
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
                contact.getContactNodes());
    }

    static JpaConnector convert(Connector connector) {
        return new JpaConnector(
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
                connector.getNotes(),
                AuthTokenIdHolder.get()

        );
    }

    static Set<JpaConnector> convert(Set<Connector> connector) {
        Set<JpaConnector> returnValue = new HashSet<>();
        for(Connector c: connector)
        {
            JpaConnector work = new JpaConnector(c.getId(),
                    c.getConnectorNumber(),
                    convert(c.getManufacturer()),
                    c.getTypeDescription(),
                    c.getSoftwareVersion(),
                    c.getWorksFromDataVersion(),
                    c.getDataDefinition(),
                    convert(c.getSoftwareManufacturer()),
                    convert(c.getTechnicallyResponsible()),
                    convert(c.getOperationCompany()),
                    c.getNotes(),
                    AuthTokenIdHolder.get());
            returnValue.add(work);
        }
        return returnValue;
    }

    static JpaStationManufacturer convert(StationManufacturer stationManufacturer) {
        return new JpaStationManufacturer(
                stationManufacturer.getId(),
                stationManufacturer.getStationManufacturerName(),
                stationManufacturer.getStationManufacturerType(),
                stationManufacturer.getStationManufacturerFirmwareVersion(),
                stationManufacturer.getStationRemark()
        );
    }

    static JpaSupplier convert(Supplier supplier) {
        return new JpaSupplier(
                supplier.getId(),
                supplier.getStationNumber(),
                supplier.getStationId(),
                supplier.getStationName(),
                supplier.getStationWater(),
                supplier.getStationWaterType(),
                convert(supplier.getStationManufacturer()),
                convert(supplier.getConnector()),
                supplier.getRefreshRate(),
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
                supplier.getChannelUse(),
                supplier.getUtcIsUsed(),
                supplier.getIsSummertime());
    }

    static JpaTaker convert(Taker taker) {
        return new JpaTaker(
                taker.getId(),
                taker.getStationNumber(),
                taker.getStationId(),
                convert(taker.getTakerServiceManufacturer()),
                convert(taker.getConnector()),
                taker.getRefreshRate()
        );
    }

    static JpaApiToken convert(ApiToken token) {
        return new JpaApiToken(
                token.getId(),
                token.getHashedToken(),
                token.getSalt(),
                token.isActivated(),
                token.getExpiresAt()
        );
    }

    static JpaTakerServiceManufacturer convert(TakerServiceManufacturer takerServiceManufacturer) {
        return new JpaTakerServiceManufacturer(
                takerServiceManufacturer.getId(),
                takerServiceManufacturer.getTakerManufacturerName(),
                takerServiceManufacturer.getTakerSystemName(),
                takerServiceManufacturer.getStationManufacturerFirmwareVersion(),
                takerServiceManufacturer.getRequestRemark()
        );
    }
}
