package com.stm.pegelhub.inbound.metadata.dto;

import static com.stm.pegelhub.common.util.Validations.requireSEThan;

/**
 * DTO to create taker service manufacturer data.
 */
public record CreateTakerServiceManufacturerDto(String takerManufacturerName, String takerSystemName,
                                                String stationManufacturerFirmwareVersion, String requestRemark
) {
    public CreateTakerServiceManufacturerDto {
        requireSEThan(takerManufacturerName, 100);
        requireSEThan(takerSystemName, 100);
        requireSEThan(stationManufacturerFirmwareVersion, 50);
        requireSEThan(requestRemark, 255);
    }
}
