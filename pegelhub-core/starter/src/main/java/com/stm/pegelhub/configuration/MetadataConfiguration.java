package com.stm.pegelhub.configuration;

import com.stm.pegelhub.configuration.metadata.MetadataControllerConfiguration;
import com.stm.pegelhub.configuration.metadata.MetadataRepositoryConfiguration;
import com.stm.pegelhub.configuration.metadata.MetadataServiceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Base class for all metadata configurations.  Triggers the configurations to be used
 */
@Configuration
@ComponentScan(basePackageClasses = {
        MetadataControllerConfiguration.class,
        MetadataRepositoryConfiguration.class,
        MetadataServiceConfiguration.class})
public class MetadataConfiguration {
}
