package com.stm.pegelhub.configuration.data;

import com.influxdb.client.InfluxDBClient;
import com.stm.pegelhub.outbound.repository.data.MeasurementRepository;
import com.stm.pegelhub.outbound.repository.data.TelemetryRepository;
import com.stm.pegelhub.outbound.data.InfluxMeasurementRepository;
import com.stm.pegelhub.outbound.data.InfluxTelemetryRepository;
import com.stm.pegelhub.outbound.db.DatabaseProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean configuration for the data repository layer.
 */
@Configuration
public class DataRepositoryConfiguration {

    /**
     * @param client: The {@link InfluxDBClient} which houses the API-calls.
     * @param properties: the {@link DatabaseProperties} which store the necessary
     *                    properties (url, org, bucket and token) to connect to the
     *                    time series database (currently InfluxDB).
     * @return returns an {@link InfluxTelemetryRepository} which is a
     *                  datarepository for the telemetry data (e.g. power usage, ip address, ...).
     */
    @Bean
    public TelemetryRepository telemetryRepository(
            @Qualifier("telemetryClient") InfluxDBClient client,
            @Qualifier("telemetryConfiguration") DatabaseProperties properties) {
        return new InfluxTelemetryRepository(client, properties);
    }

    /**
     * @param client: The {@link InfluxDBClient} which houses the API-calls.
     * @param properties: the {@link DatabaseProperties} which store the necessary
     *                          properties (url, org, bucket and token) to connect to the
     *                          time series database (currently InfluxDB).
     * @return returns an {@link InfluxMeasurementRepository} which is a
     *         datarepository for the measurement (e.g. water height...) data
     *         (Currently InfluxDB, name of bucket: data)
     */
    @Bean
    public MeasurementRepository measurementRepository(
            @Qualifier("dataClient") InfluxDBClient client,
            @Qualifier("dataConfiguration") DatabaseProperties properties) {
        return new InfluxMeasurementRepository(client, properties);
    }
}
