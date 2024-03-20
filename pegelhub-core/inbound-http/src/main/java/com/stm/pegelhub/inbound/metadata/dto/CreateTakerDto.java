package com.stm.pegelhub.inbound.metadata.dto;

import static com.stm.pegelhub.common.util.Validations.requireNotEmpty;
import static com.stm.pegelhub.common.util.Validations.requireSEThan;
import static java.util.Objects.requireNonNull;

/**
 * DTO to create taker data.
 */
public record CreateTakerDto(String stationNumber, Integer stationId,
                             CreateTakerServiceManufacturerDto takerServiceManufacturer,
                             CreateConnectorDto connector, Long refreshRate) {
    public CreateTakerDto {
        requireSEThan(requireNotEmpty(stationNumber), 50);
        requireNonNull(stationId);
        requireNonNull(takerServiceManufacturer);
        requireNonNull(connector);
        requireNonNull(refreshRate);

    }
}
