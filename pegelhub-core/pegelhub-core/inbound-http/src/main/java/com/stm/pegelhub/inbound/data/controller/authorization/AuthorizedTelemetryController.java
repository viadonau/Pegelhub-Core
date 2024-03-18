package com.stm.pegelhub.inbound.data.controller.authorization;

import com.stm.pegelhub.common.model.data.Measurement;
import com.stm.pegelhub.common.model.data.Telemetry;
import com.stm.pegelhub.inbound.data.controller.TelemetryController;
import com.stm.pegelhub.logic.service.metadata.AuthorizationService;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of the Interface {@code TelemetryController}.
 * Performs authorization of requests where necessary before they are delegated to the underlying controller.
 */
public class AuthorizedTelemetryController implements TelemetryController {

    private final AuthorizationService authorizationService;
    private final TelemetryController delegate;

    public AuthorizedTelemetryController(AuthorizationService authorizationService, TelemetryController delegate) {
        this.authorizationService = requireNonNull(authorizationService);
        this.delegate = requireNonNull(delegate);
    }

    /**
     * authorizes the request and forwards it to for further handling
     * @param apiKey the key which is used for Authorization
     * @param telemetry the telemetry to be saved
     */
    @Override
    public Telemetry writeTelemetryData(String apiKey, Telemetry telemetry) {
        authorizationService.authorize(apiKey);
        return delegate.writeTelemetryData(apiKey, telemetry);
    }

    /**
     * authorizes the request and forwards it to for further handling
     * @param apiKey the key which is used for Authorization
     * @param range the range in which the desired telemetry reside
     * @return the telemetry found in the specified range
     */
    @Override
    public List<Telemetry> findTelemetryInRange(String apiKey, String range) {
        authorizationService.authorize(apiKey);
        return delegate.findTelemetryInRange(apiKey, range);
    }

    /**
     * authorizes the request and forwards it to for further handling
     * @param apiKey the key which is used for Authorization
     * @param uuid the {@link UUID} of the telemetry to be searched for
     * @return the corresponding {@link Telemetry} to the specified {@link UUID}
     */
    @Override
    public Telemetry findTelemetryById(String apiKey, UUID uuid) {
        authorizationService.authorize(apiKey);
        return delegate.findTelemetryById(apiKey, uuid);
    }
}
