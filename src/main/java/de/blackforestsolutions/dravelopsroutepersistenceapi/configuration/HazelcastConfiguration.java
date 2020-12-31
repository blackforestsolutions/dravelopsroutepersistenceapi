package de.blackforestsolutions.dravelopsroutepersistenceapi.configuration;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientUserCodeDeploymentConfig;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.core.HazelcastInstance;
import de.blackforestsolutions.dravelopsdatamodel.*;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Range;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;

@SpringBootConfiguration
public class HazelcastConfiguration {

    public static final String JOURNEY_MAP = "journey-map";
    public static final int JOURNEY_MAP_TTL_STANDARD = 1;
    public static final String HAZELCAST_INSTANCE = "hazelcastInstance";

    @Value("${hazelcast.maxPastDaysInCalendar}")
    private int maxPastDaysInCalendar;
    @Value("${hazelcast.timeRangeInMinutes}")
    private int hazelcastTimeRangeInMinutes;

    @Bean(name = "hazelcastApiToken")
    public ApiToken apiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setMaxPastDaysInCalendar(maxPastDaysInCalendar)
                .setHazelcastTimeRangeInMinutes(hazelcastTimeRangeInMinutes)
                .build();
    }

    @Bean
    public ClientConfig clientConfig() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClassLoader(Thread.currentThread().getContextClassLoader());
        clientConfig.setUserCodeDeploymentConfig(userCodeDeploymentConfig());
        return clientConfig;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(ClientConfig clientConfig) {
        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
//        hazelcastInstance.getConfig().addMapConfig(mapConfig());
        return hazelcastInstance;
    }

    private ClientUserCodeDeploymentConfig userCodeDeploymentConfig() {
        ClientUserCodeDeploymentConfig clientUserCodeDeploymentConfig = new ClientUserCodeDeploymentConfig();

        clientUserCodeDeploymentConfig.addClass(DeparturePointPredicate.class);
        clientUserCodeDeploymentConfig.addClass(Point.class);
        clientUserCodeDeploymentConfig.addClass(Distance.class);
        clientUserCodeDeploymentConfig.addClass(ArrivalPointPredicate.class);
        clientUserCodeDeploymentConfig.addClass(LanguagePredicate.class);
        clientUserCodeDeploymentConfig.addClass(DepartureTimePredicate.class);
        clientUserCodeDeploymentConfig.addClass(ArrivalTimePredicate.class);
        clientUserCodeDeploymentConfig.addClass(Journey.class);
        clientUserCodeDeploymentConfig.addClass(Leg.class);
        clientUserCodeDeploymentConfig.addClass(TravelPoint.class);
        clientUserCodeDeploymentConfig.addClass(VehicleType.class);
        clientUserCodeDeploymentConfig.addClass(TravelProvider.class);
        clientUserCodeDeploymentConfig.addClass(Metric.class);
        clientUserCodeDeploymentConfig.addClass(Range.class);
        clientUserCodeDeploymentConfig.addClass(Metrics.class);
        clientUserCodeDeploymentConfig.addClass(Price.class);
        clientUserCodeDeploymentConfig.addClass(PriceType.class);

        clientUserCodeDeploymentConfig.setEnabled(true);

        return clientUserCodeDeploymentConfig;
    }

//    private MapConfig mapConfig() {
//        return new MapConfig(JOURNEY_MAP)
//                .setName(JOURNEY_MAP)
////                .setEvictionConfig(evictionConfig())
//                .setTimeToLiveSeconds(JOURNEY_MAP_TTL_STANDARD);
//    }

    private EvictionConfig evictionConfig() {
        return new EvictionConfig()
                .setEvictionPolicy(EvictionPolicy.LFU);
    }

}
