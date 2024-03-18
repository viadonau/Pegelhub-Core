package com.stm.pegelhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Main class for the Routing Proxy.
 * This module is needed, so that any request provided to the PegelHub is sent to a valid core instance.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableEurekaClient
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
