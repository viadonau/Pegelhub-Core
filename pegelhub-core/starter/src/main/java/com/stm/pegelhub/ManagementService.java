package com.stm.pegelhub;

import com.stm.pegelhub.configuration.DataConfiguration;
import com.stm.pegelhub.configuration.MetadataConfiguration;
import com.stm.pegelhub.configuration.inbound.WebConfiguration;
import com.stm.pegelhub.configuration.influxdb.InfluxDBConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Main Class for the PegelHub core. Triggers all the Configurations to be run and starts the Service
 */
@SpringBootApplication(
        scanBasePackageClasses = {
                WebConfiguration.class,
                DataConfiguration.class,
                MetadataConfiguration.class,
                InfluxDBConfiguration.class}
)
public class ManagementService {
    public static void main(String[] args) {
        SpringApplication.run(ManagementService.class, args);
    }
}