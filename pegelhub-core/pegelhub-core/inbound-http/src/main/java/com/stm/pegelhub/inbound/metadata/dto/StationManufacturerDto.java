package com.stm.pegelhub.inbound.metadata.dto;

import java.util.UUID;

import static com.stm.pegelhub.common.util.Validations.requireSEThan;
import static java.util.Objects.requireNonNull;

/**
 * DTO for station manufacturer data.
 */
public record StationManufacturerDto(UUID id, String stationManufacturerName, String stationManufacturerType,
                                     String stationManufacturerFirmwareVersion, String stationRemark) {
    public StationManufacturerDto {
        requireNonNull(id);
        requireSEThan(stationManufacturerName, 100);
        requireSEThan(stationManufacturerType, 100);
        requireSEThan(stationManufacturerFirmwareVersion, 50);
        requireSEThan(stationRemark, 255);
    }
}
