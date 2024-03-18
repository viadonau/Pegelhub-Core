package com.stm.pegelhub.inbound.data.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Map;

import static com.stm.pegelhub.common.util.Validations.requireNotEmpty;
import static java.util.Objects.requireNonNull;

/**
 * Dto to create a single measurement.
 */
public record WriteMeasurementDto(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp,
                                  Map<String, Double> fields, Map<String, String> infos) {

    public WriteMeasurementDto {
        requireNonNull(timestamp);
        requireNotEmpty(fields);
        requireNonNull(infos);
    }
}
