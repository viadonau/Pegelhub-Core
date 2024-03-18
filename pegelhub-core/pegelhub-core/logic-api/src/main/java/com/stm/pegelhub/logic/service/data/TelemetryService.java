package com.stm.pegelhub.logic.service.data;

import com.stm.pegelhub.common.model.data.Telemetry;

import java.util.List;
import java.util.UUID;

/**
 * Service for all {@code Telemetry}s.
 */
public interface TelemetryService {

    /**
     * Saves a telemetry.
     *
     * @param telemetry to save.
     * @return the saved telemetry.
     */
    Telemetry saveTelemetry(Telemetry telemetry);

    /**
     * Queries a telemetry.
     *
     * @param range in which the returned values reside.
     * @return the telemetrys in that range.
     */
    List<Telemetry> getByRange(String range);

    /**
     * Queries the last telemetry.
     *
     * @param uuid of the telemetry.
     * @return the last telemetry with that uuid.
     */
    Telemetry getLastData(UUID uuid);
}

