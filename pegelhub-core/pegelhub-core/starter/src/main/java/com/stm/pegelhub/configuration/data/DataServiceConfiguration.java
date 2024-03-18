package com.stm.pegelhub.configuration.data;

import com.stm.pegelhub.logic.data.service.impl.MeasurementServiceImpl;
import com.stm.pegelhub.logic.data.service.impl.TelemetryServiceImpl;
import com.stm.pegelhub.logic.service.data.MeasurementService;
import com.stm.pegelhub.logic.service.data.TelemetryService;
import com.stm.pegelhub.outbound.repository.data.MeasurementRepository;
import com.stm.pegelhub.outbound.repository.data.TelemetryRepository;
import com.stm.pegelhub.outbound.repository.metadata.SupplierRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean configuration for the data service layer.
 */
@Configuration
public class DataServiceConfiguration {

    /**
     * @param telemetryRepository the {@link TelemetryRepository} in which the
     *                            storing and fetching takes place.
     * @return the {@link TelemetryServiceImpl} which communicates with the
     *                            given {@link TelemetryRepository}
     */
    @Bean
    public TelemetryService telemetryService(TelemetryRepository telemetryRepository) {
        return new TelemetryServiceImpl(telemetryRepository);
    }

    /**
     * @param supplierRepository the {@link SupplierRepository} which is used for checking
     *                           whether the supplier of the measurements is valid.
     * @param measurementRepository the {@link MeasurementRepository} which is used
     *                              for storing and fetching the measurement data
     * @return the {@link TelemetryServiceImpl} which communicates with the
     *                            given {@link TelemetryRepository}
     */
    @Bean
    public MeasurementService measurementService(SupplierRepository supplierRepository, MeasurementRepository measurementRepository) {
        return new MeasurementServiceImpl(supplierRepository, measurementRepository);
    }
}
