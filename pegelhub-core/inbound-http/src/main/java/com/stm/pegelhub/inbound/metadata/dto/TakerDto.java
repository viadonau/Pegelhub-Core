package com.stm.pegelhub.inbound.metadata.dto;

import java.util.UUID;

import static com.stm.pegelhub.common.util.Validations.requireNotEmpty;
import static com.stm.pegelhub.common.util.Validations.requireSEThan;
import static java.util.Objects.requireNonNull;

/**
 * DTO for taker data.
 */
public record TakerDto(UUID id, String stationNumber, Integer stationId,
                       TakerServiceManufacturerDto takerServiceManufacturer,
                       ConnectorDto connector, Long refreshRate) {
    public TakerDto {
        requireNonNull(id);
        requireSEThan(requireNotEmpty(stationNumber), 50);
        requireNonNull(stationId);
        requireNonNull(takerServiceManufacturer);
        requireNonNull(connector);
        requireNonNull(refreshRate);

    }
}
