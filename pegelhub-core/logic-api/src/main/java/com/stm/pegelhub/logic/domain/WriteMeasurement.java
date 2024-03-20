package com.stm.pegelhub.logic.domain;

import java.time.LocalDateTime;
import java.util.Map;

import static com.stm.pegelhub.common.util.Validations.requireNotEmpty;
import static java.util.Objects.requireNonNull;

/**
 * Domain class to create a single measurement.
 */
public record WriteMeasurement(LocalDateTime timestamp, Map<String, Double> fields, Map<String, String> infos) {

    public WriteMeasurement {
        requireNonNull(timestamp);
        requireNotEmpty(fields);
        requireNonNull(infos);
    }
}
