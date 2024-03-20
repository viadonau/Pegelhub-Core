package com.stm.pegelhub.common.model.data;

import static com.stm.pegelhub.common.util.Validations.requirePositive;
import static java.util.Objects.requireNonNull;

/**
 * Data class for telemetry which represents an entry in the time series database (InfluxDB) in the "telemetry" (telemetry) bucket.
 */
public record Telemetry(String measurement, String stationIPAddressIntern, String stationIPAddressExtern,
                        String timestamp, Integer cycleTime, Double temperatureWater, Double temperatureAir,
                        Double performanceVoltageBattery, Double performanceVoltageSupply,
                        Double performanceElectricityBattery, Double performanceElectricitySupply,
                        Double fieldStrengthTransmission) {
    public Telemetry {
        requireNonNull(measurement);
        requireNonNull(stationIPAddressIntern);
        requireNonNull(stationIPAddressExtern);
        requireNonNull(timestamp);
    }
}
