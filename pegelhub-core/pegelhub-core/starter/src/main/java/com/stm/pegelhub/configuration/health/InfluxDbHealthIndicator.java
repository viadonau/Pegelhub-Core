package com.stm.pegelhub.configuration.health;

import org.influxdb.InfluxDB;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Component Class that performs the check on the InfluxDb.
 * Needs to be changed if another time series database is going to be used.
 */
@Component
public class InfluxDbHealthIndicator extends AbstractHealthIndicator {

    private final InfluxDB influxDb;

    public InfluxDbHealthIndicator(InfluxDB influxDb) {
        super("InfluxDB health check failed");
        Assert.notNull(influxDb, "InfluxDB must not be null");
        this.influxDb = influxDb;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        try {
            influxDb.ping();
            builder.up();
        } catch (Exception e) {
            builder.down(e);
        }
    }
}

