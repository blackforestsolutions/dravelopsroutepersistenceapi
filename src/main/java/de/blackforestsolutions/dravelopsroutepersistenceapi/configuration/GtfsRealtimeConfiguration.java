package de.blackforestsolutions.dravelopsroutepersistenceapi.configuration;

import lombok.AccessLevel;
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
@ConfigurationProperties(prefix = "graphql.playground.tabs.JOURNEY_QUERY")
public class GtfsRealtimeConfiguration {

    @Getter(AccessLevel.NONE)
    private boolean hasGtfsRealtime;
    private Map<String, ApiToken> gtfsRealtimeTokens;

    public boolean hasGtfsRealtime() {
        return hasGtfsRealtime;
    }

    @Setter
    @Getter
    public static class ApiToken {

        private String url;
        private Map<String, String> headers;
    }
}
