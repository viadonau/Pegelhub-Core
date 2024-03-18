package com.stm.pegelhub.inbound.data.dto;

import java.util.List;

import static com.stm.pegelhub.common.util.Validations.requireNotEmpty;

/**
 * Dto to write multiple measurements.
 */
public record WriteMeasurementsDto(List<WriteMeasurementDto> measurements) {

    public WriteMeasurementsDto {
        requireNotEmpty(measurements);
    }
}
