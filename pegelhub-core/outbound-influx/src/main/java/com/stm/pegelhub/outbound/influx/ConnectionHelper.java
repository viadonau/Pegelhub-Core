package com.stm.pegelhub.outbound.influx;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class, which handles the basic communication with the influxDB.
 * Needs to be rewritten if the time series database is to be exchanged
 */
public final class ConnectionHelper {


    public static void writePoints(InfluxDBClient client, List<Point> dataPoints) {
        WriteApiBlocking writeApi = client.getWriteApiBlocking();
        writeApi.writePoints(dataPoints);

    }

    public static void writePointbyPoint(InfluxDBClient influxDBClient, Point dataPoint) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
        writeApi.writePoint(dataPoint);
    }

    public static HashMap<String, HashMap<String, HashMap<String, Object>>> queryData(InfluxDBClient influxDBClient, String query) {
        List<FluxTable> tables = influxDBClient.getQueryApi().query(query);
        int i = 1;
        HashMap<String, HashMap<String, HashMap<String, Object>>> points = new HashMap<>();
        //     measurement,    timestamp,        field,   value
        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                i++;
                var time = record.getTime().toString();
                var measurement = record.getMeasurement();
                var field = record.getField();
                var allValues = record.getValues();

                var value = record.getValue();

                HashMap<String, HashMap<String, Object>> valuesTime = new HashMap<>();
                HashMap<String, Object> fieldValue = new HashMap<>();
                fieldValue.put(field, value);

                for (var key : allValues.entrySet()) {
                    if (!key.getKey().contains("_") && !(key.getKey().matches("result")) && !(key.getKey().matches("table"))) {
                        fieldValue.put(key.getKey(), key.getValue());
                    }
                }

                valuesTime.put(time, fieldValue);

                if (!points.containsKey(measurement)) {
                    points.put(measurement, valuesTime);
                } else if (points.containsKey(measurement) && !points.get(measurement).containsKey(time)) {
                    points.get(measurement).put(time, fieldValue);
                } else if (points.containsKey(measurement) && points.get(measurement).containsKey(time)) {
                    points.get(measurement).get(time).put(field, value);
                }

            }
        }

        return points;
    }
}
