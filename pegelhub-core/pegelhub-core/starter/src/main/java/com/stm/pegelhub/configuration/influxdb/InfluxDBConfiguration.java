package com.stm.pegelhub.configuration.influxdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.stm.pegelhub.outbound.db.DatabaseProperties;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

/**
 * Bean configuration for the influx db connection properties.
 * Configures the conections (url, org, bucket, API-token) for the InfluxDB
 * and the corresponding buckets.
 */
@Configuration
@ConfigurationProperties(prefix = "datasource")
@Data
public class InfluxDBConfiguration {
    private static final Logger LOGGER = LogManager.getLogger(InfluxDBConfiguration.class);

    private DatabaseProperties telemetry;
    private DatabaseProperties data;

    @PostConstruct
    private void reloadPropertiesFromInfluxFile() {
        if (System.getenv("INFLUX_FILE") != null && !System.getenv("INFLUX_FILE").isEmpty()) {
            LOGGER.info("Reading influx configuration from environmentally given file");
            try {
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                Configuration config = mapper.readValue(
                       new File(System.getenv("INFLUX_FILE")),
                        Configuration.class
               );

                this.telemetry = new DatabaseProperties(telemetry.url(), config.telemetry.org(), config.telemetry.bucket(), config.telemetry.token());
                this.data = new DatabaseProperties(data.url(), config.data.org(), config.data.bucket(), config.data.token());
                //TODO decrypt what this LOGGER.info means.
                LOGGER.info("using alternate influx configuration");
                LOGGER.info(data.url());
                LOGGER.info(data.org());
                LOGGER.info(data.bucket());
                LOGGER.info(data.token());


            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    @Bean("telemetryClient")
    public InfluxDBClient telemetryClient() {
        LOGGER.trace("creating telemetry client with properties: " + telemetry);
        return createClient(telemetry);
    }

    @Bean("dataClient")
    public InfluxDBClient dataClient() {
        LOGGER.trace("creating data client with properties: " + data);
        return createClient(data);
    }

    @Bean("telemetryConfiguration")
    public DatabaseProperties telemetryConfiguration() {
        return this.telemetry;
    }

    @Bean("dataConfiguration")
    public DatabaseProperties dataConfiguration() {
        return this.data;
    }

    @Data
    private static final class Configuration {
        private DatabaseProperties telemetry;
        private DatabaseProperties data;
    }


    private InfluxDBClient createClient(DatabaseProperties properties) {
        return InfluxDBClientFactory.create(
                properties.url(),
                properties.token().toCharArray(),
                properties.org(),
                properties.bucket());
    }
}
