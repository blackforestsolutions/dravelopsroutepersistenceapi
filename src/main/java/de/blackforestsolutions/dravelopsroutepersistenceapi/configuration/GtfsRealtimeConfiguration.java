package de.blackforestsolutions.dravelopsroutepersistenceapi.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@RefreshScope
@Component
@ConfigurationProperties(prefix = "gtfs")
public class GtfsRealtimeConfiguration {

    private Map<String, ApiToken> apitokens;

    @Setter
    @Getter
    public static class ApiToken {

        private String gtfsRealtimeUrl;
        private Boolean hasFrequencyEntries;
        private Map<String, String> headers;
    }
}
