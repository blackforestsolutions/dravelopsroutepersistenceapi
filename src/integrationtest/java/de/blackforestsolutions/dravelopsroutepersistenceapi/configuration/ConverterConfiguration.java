package de.blackforestsolutions.dravelopsroutepersistenceapi.configuration;

import de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.converter.DistanceConverter;
import de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.converter.ZonedDateTimeConverter;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ConverterConfiguration {

    @Bean
    @ConfigurationPropertiesBinding
    public ZonedDateTimeConverter zonedDateTimeConverter() {
        return new ZonedDateTimeConverter();
    }

    @Bean
    @ConfigurationPropertiesBinding
    public DistanceConverter distanceConverter() {
        return new DistanceConverter();
    }
}