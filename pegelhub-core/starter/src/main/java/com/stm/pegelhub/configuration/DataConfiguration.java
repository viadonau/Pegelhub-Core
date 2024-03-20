package com.stm.pegelhub.configuration;

import com.stm.pegelhub.configuration.data.DataControllerConfiguration;
import com.stm.pegelhub.configuration.data.DataRepositoryConfiguration;
import com.stm.pegelhub.configuration.data.DataServiceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Base class for all data configurations. Triggers the configurations to be used
 */
@Configuration
@ComponentScan(basePackageClasses = {
        DataControllerConfiguration.class,
        DataRepositoryConfiguration.class,
        DataServiceConfiguration.class})
public class DataConfiguration {
}
