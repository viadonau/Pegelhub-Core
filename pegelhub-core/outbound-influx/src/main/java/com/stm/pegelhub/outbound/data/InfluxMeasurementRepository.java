package com.stm.pegelhub.outbound.data;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.stm.pegelhub.common.model.data.Measurement;
import com.stm.pegelhub.outbound.db.DatabaseProperties;
import com.stm.pegelhub.outbound.influx.ConnectionHelper;
import com.stm.pegelhub.outbound.repository.data.MeasurementRepository;
import org.apache.commons.logging.Log;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Influx implementation for {@code MeasurementRepository}.
 * Implements the storing/adding of data to the time series database.
 * Needs to be rewritten if time series database is going to be exchanged.
 */
public class InfluxMeasurementRepository implements MeasurementRepository {

    private final InfluxDBClient client;
    private final DatabaseProperties properties;

    public InfluxMeasurementRepository(InfluxDBClient client, DatabaseProperties properties) {
        this.client = requireNonNull(client);
        this.properties = requireNonNull(properties);
    }

    /**
     * @param measurements to save.
     */
    @Override
    public void storeMeasurements(List<Measurement> measurements) {
        List<Point> dataPoints = new ArrayList<>(measurements.size());
        for (Measurement measurement : measurements) {
            Point measurementData = Point.measurement(measurement.measurement().toString()).time((measurement.timestamp().toInstant(ZoneOffset.UTC)), WritePrecision.MS);
            for (Map.Entry<String, String> entry : measurement.infos().entrySet()) {
                measurementData.addTag(entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, Double> entry : measurement.fields().entrySet()) {
                measurementData.addField(entry.getKey(), entry.getValue());
            }
            dataPoints.add(measurementData);
        }
        ConnectionHelper.writePoints(this.client, dataPoints);
    }

    /**
     * @param range in which the returned values reside.
     * @return the values inside the specified range
     */
    @Override
    public List<Measurement> getByRange(String range) {
        getSystemTime();
        String query = "from(bucket: \"" + properties.bucket() + "\") |> range(start: -" + range + ")";

        HashMap<String, HashMap<String, HashMap<String, Object>>> data = ConnectionHelper.queryData(this.client, query);
        return toMeasurement(data);
    }

    /**
     * @param id of the measurement.
     * @param range in which the returned values reside.
     * @return the value with the specified ID in the specified range
     */
    @Override
    public List<Measurement> getByIDAndRange(UUID id, String range) {
        String query = "from(bucket: \"" + properties.bucket() + "\") |> range(start: -" + range
                + ") |> filter(fn: (r) => r._measurement == \"" + id + "\")";

        HashMap<String, HashMap<String, HashMap<String, Object>>> data = ConnectionHelper.queryData(this.client, query);
        return toMeasurement(data);
    }

    /**
     *
     * @param uuid of the measurement.
     * @return the corresponding value to the specified {@link UUID}
     */
    @Override
    public Measurement getLastData(UUID uuid) {
        String query = "from(bucket: \"" + properties.bucket() + "\") |> range(start: -72h) |> filter(fn: (r) => r._measurement == \"" + uuid + "\") |> group() |> last()";
        List<Measurement> measurements = toMeasurement(ConnectionHelper.queryData(this.client, query));
        if (measurements.isEmpty()) {
            throw new InfluxException("No measurement found");
        }
        return measurements.get(0);
    }

    /**
     * @param data the data to be converted to measurements
     * @return the converted measurements
     */
    private List<Measurement> toMeasurement(HashMap<String, HashMap<String, HashMap<String, Object>>> data) {
        List<Measurement> measurements = new ArrayList<>();
        for (Map.Entry<String, HashMap<String, HashMap<String, Object>>> measurement : data.entrySet()) {
            for (Map.Entry<String, HashMap<String, Object>> measurementEntry : measurement.getValue().entrySet()) {
                HashMap<String, Object> measurementData = measurementEntry.getValue();
                HashMap<String, Double> fields = new HashMap<>(measurementData.entrySet().stream()
                        .filter(a -> a.getValue() instanceof Double)
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> (double) e.getValue())));
                HashMap<String, String> infos = new HashMap<>(measurementData.entrySet().stream()
                        .filter(a -> a.getValue() instanceof String)
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> (String) e.getValue())));
                measurements.add(
                        new Measurement(
                                UUID.fromString(measurement.getKey()),
                                LocalDateTime.ofInstant(Instant.parse(measurementEntry.getKey()), ZoneOffset.UTC),
                                fields,
                                infos));
            }
        }
        return measurements;
    }

    @Override
    public Timestamp getSystemTime() {
        String time = "import \"system\" import \"array\" array.from(rows: [{time: system.time()}])";

        QueryApi queryApi = this.client.getQueryApi();
        List<FluxTable> tables = queryApi.query(time);

        Instant returnResult = null;

        for(FluxTable table : tables)
        {
            List<FluxRecord> records = table.getRecords();
            for(FluxRecord record : records)
            {
                returnResult = (Instant) record.getValueByIndex(2);
            }
        }
        assert returnResult != null;
        return Timestamp.from(returnResult);
    }
}
