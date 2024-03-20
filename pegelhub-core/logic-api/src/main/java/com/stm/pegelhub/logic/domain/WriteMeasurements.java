package com.stm.pegelhub.logic.domain;

import java.util.List;

import static com.stm.pegelhub.common.util.Validations.requireNotEmpty;

/**
 * Domain class to write multiple measurements.
 */
public record WriteMeasurements(List<WriteMeasurement> measurements) {

    public WriteMeasurements {
        requireNotEmpty(measurements);
    }
}
