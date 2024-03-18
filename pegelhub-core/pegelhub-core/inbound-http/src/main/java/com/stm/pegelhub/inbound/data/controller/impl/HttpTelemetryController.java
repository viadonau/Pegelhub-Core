package com.stm.pegelhub.inbound.data.controller.impl;

import com.stm.pegelhub.common.model.data.Measurement;
import com.stm.pegelhub.common.model.data.Telemetry;
import com.stm.pegelhub.inbound.data.controller.TelemetryController;
import com.stm.pegelhub.inbound.data.dto.WriteMeasurementsDto;
import com.stm.pegelhub.logic.service.data.TelemetryService;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Controller implementation for {@code TelemetryController}.
 */
public class HttpTelemetryController implements TelemetryController {

    private final TelemetryService telemetryService;

    public HttpTelemetryController(TelemetryService telemetryService) {
        this.telemetryService = requireNonNull(telemetryService);
    }

    /**
     * @param apiKey needed because of interface, not used. The AuthorizedTelemetryController handles it
     * @param telemetry the {@link Telemetry} to be saved
     * @return the saved {@link Telemetry} data
     */
    @Override
    public Telemetry writeTelemetryData(String apiKey, Telemetry telemetry) {
        return telemetryService.saveTelemetry(telemetry);
    }

    /**
     * @param apiKey needed because of interface, not used. The AuthorizedTelemetryController handles it
     * @param range the range in which the desired telemetry reside
     * @return the {@link Telemetry}s found in the specified range
     */
    @Override
    public List<Telemetry> findTelemetryInRange(String apiKey, String range) {
        return telemetryService.getByRange(range);
    }

    /**
     * @param apiKey eeded because of interface, not used. The AuthorizedTelemetryController handles it
     * @param uuid the {@link UUID} of the telemetry to be searched for
     * @return the corresponding {@link Telemetry} to the specified {@link UUID}
     */
    @Override
    public Telemetry findTelemetryById(String apiKey, UUID uuid) {
        return telemetryService.getLastData(uuid);
    }
}
