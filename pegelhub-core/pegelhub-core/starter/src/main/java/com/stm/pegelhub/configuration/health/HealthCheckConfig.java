package com.stm.pegelhub.configuration.health;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration that checks the status of the InfluxDB
 * Needs to be changed if another time series database is going to be used
 */
@Configuration
public class HealthCheckConfig {

    @Value("${datasource.telemetry.url}")
    private String connectionUrl;

    @Value("${datasource.login.user}")
    private String username;

    @Value("${datasource.login.password}")
    private String pw;

    @Bean
    public InfluxDB influxDbMethod() {
        return InfluxDBFactory.connect(connectionUrl, username, pw);
    }

    @Bean
    public HealthIndicator influxDbHealthIndicatorMethod(InfluxDB influxDb) {
        return new InfluxDbHealthIndicator(influxDb);
    }
}

