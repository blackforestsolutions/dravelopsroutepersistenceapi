package de.blackforestsolutions.dravelopsroutepersistenceapi.configuration;

import com.hazelcast.client.config.ClientConfig;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class HazelcastConfiguration {

    public static final String JOURNEY_MAP = "journey-map";
    public static final String HAZELCAST_INSTANCE = "hazelcastInstance";

    @Value("${hazelcast.maxPastDaysInCalendar}")
    private int maxPastDaysInCalendar;
    @Value("${hazelcast.timeRangeInMinutes}")
    private int hazelcastTimeRangeInMinutes;

    @Bean
    public ClientConfig clientConfig() {
        ClientConfig clientConfig = new ClientConfig();
        return clientConfig;
    }

    @Bean(name = "hazelcastApiToken")
    public ApiToken apiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setMaxPastDaysInCalendar(maxPastDaysInCalendar)
                .setHazelcastTimeRangeInMinutes(hazelcastTimeRangeInMinutes)
                .build();
    }

}
