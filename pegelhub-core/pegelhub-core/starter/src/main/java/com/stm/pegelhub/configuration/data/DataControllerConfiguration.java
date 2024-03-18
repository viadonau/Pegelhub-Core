package com.stm.pegelhub.configuration.data;

import com.stm.pegelhub.inbound.data.controller.MeasurementController;
import com.stm.pegelhub.inbound.data.controller.TelemetryController;
import com.stm.pegelhub.inbound.data.controller.authorization.AuthorizedMeasurementController;
import com.stm.pegelhub.inbound.data.controller.authorization.AuthorizedTelemetryController;
import com.stm.pegelhub.inbound.data.controller.impl.HttpMeasurementController;
import com.stm.pegelhub.inbound.data.controller.impl.HttpTelemetryController;
import com.stm.pegelhub.logic.service.data.MeasurementService;
import com.stm.pegelhub.logic.service.data.TelemetryService;
import com.stm.pegelhub.logic.service.metadata.AuthorizationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean configuration for the data controller layer.
 */
@Configuration
public class DataControllerConfiguration {

    /**
     * @param authorizationService: the authorizationService which checks
     *                              the validity of the apiKey.
     * @param telemetryService: the telemetryService which processes the
     *                          measurement data before storing/fetching it.
     *                          Also handles Excpetions.
     * @return {@link AuthorizedTelemetryController }: Controller for storing and
     *                                                   fetching the telemetry data in the
     *                                                   time series database (currently InfluxDB,
     *                                                   name of Bucket: telemetry)
     */
    @Bean
    public TelemetryController telemetryController(AuthorizationService authorizationService, TelemetryService telemetryService) {
        return new AuthorizedTelemetryController(authorizationService, new HttpTelemetryController(telemetryService));
    }

    /**
     * @param authorizationService: the authorizationService which checks
     *                              the validity of the apiKey.
     * @param measurementService: the measurementService which processes the
     *                            measurement data before storing/fetching it.
     *                            Also handles Excpetions.
     * @return {@link AuthorizedMeasurementController }: Controller for storing and
     *                                                   fetching the measurement data in the
     *                                                   time series database (currently InfluxDB,
     *                                                   name of Bucket: data)
     */
    @Bean
    public MeasurementController measurementController(AuthorizationService authorizationService, MeasurementService measurementService) {
        return new AuthorizedMeasurementController(authorizationService, new HttpMeasurementController(measurementService));
    }
}
