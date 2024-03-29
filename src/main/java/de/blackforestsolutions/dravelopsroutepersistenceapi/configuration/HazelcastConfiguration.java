package de.blackforestsolutions.dravelopsroutepersistenceapi.configuration;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientUserCodeDeploymentConfig;
import com.hazelcast.core.HazelcastInstance;
import de.blackforestsolutions.dravelopsdatamodel.*;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Range;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Metrics;
import org.springframework.util.Assert;

import java.util.List;

@RefreshScope
@SpringBootConfiguration
public class HazelcastConfiguration {

    public static final String JOURNEY_MAP = "journey-map";
    public static final int JOURNEY_MAP_TTL_STANDARD = 1;
    public static final String HAZELCAST_INSTANCE = "hazelcastInstance";

    @Value("${graphql.playground.tabs.JOURNEY_QUERY.maxPastDaysInCalendar}")
    private int maxPastDaysInCalendar;
    @Value("${hazelcast.journeySearchWindowInMinutes}")
    private int journeySearchWindowInMinutes;
    @Value("${hazelcast.addresses}")
    private List<String> hazelcastAddresses;

    @RefreshScope
    @Bean
    public ApiToken hazelcastApiToken() {
        ApiToken apiToken = new ApiToken();
        apiToken.setMaxPastDaysInCalendar(maxPastDaysInCalendar);
        apiToken.setJourneySearchWindowInMinutes(journeySearchWindowInMinutes);
        return apiToken;
    }

    @Bean
    public ClientConfig config() {
        ClientConfig config = new ClientConfig();
        config.getNetworkConfig().setAddresses(hazelcastAddresses);
        config.setUserCodeDeploymentConfig(userCodeDeploymentConfig());
        return config;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(ClientConfig config) {
        return HazelcastClient.newHazelcastClient(config);
    }

    private ClientUserCodeDeploymentConfig userCodeDeploymentConfig() {
        ClientUserCodeDeploymentConfig clientUserCodeDeploymentConfig = new ClientUserCodeDeploymentConfig();
        // predicates
        clientUserCodeDeploymentConfig.addClass(DeparturePointPredicate.class);
        clientUserCodeDeploymentConfig.addClass(ArrivalPointPredicate.class);
        clientUserCodeDeploymentConfig.addClass(LanguagePredicate.class);
        clientUserCodeDeploymentConfig.addClass(DepartureTimePredicate.class);
        clientUserCodeDeploymentConfig.addClass(ArrivalTimePredicate.class);
        // spring geo
        clientUserCodeDeploymentConfig.addClass(Distance.class);
        clientUserCodeDeploymentConfig.addClass(Metric.class);
        clientUserCodeDeploymentConfig.addClass(Range.class);
        clientUserCodeDeploymentConfig.addClass(Metrics.class);
        clientUserCodeDeploymentConfig.addClass(Assert.class);
        // datamodel
        clientUserCodeDeploymentConfig.addClass(Journey.class);
        clientUserCodeDeploymentConfig.addClass(Journey.JourneyBuilder.class);
        clientUserCodeDeploymentConfig.addClass(Leg.class);
        clientUserCodeDeploymentConfig.addClass(Leg.LegBuilder.class);
        clientUserCodeDeploymentConfig.addClass(TravelPoint.class);
        clientUserCodeDeploymentConfig.addClass(TravelPoint.TravelPointBuilder.class);
        clientUserCodeDeploymentConfig.addClass(VehicleType.class);
        clientUserCodeDeploymentConfig.addClass(TravelProvider.class);
        clientUserCodeDeploymentConfig.addClass(TravelProvider.TravelProviderBuilder.class);
        clientUserCodeDeploymentConfig.addClass(Price.class);
        clientUserCodeDeploymentConfig.addClass(Price.PriceBuilder.class);
        clientUserCodeDeploymentConfig.addClass(PriceType.class);
        clientUserCodeDeploymentConfig.addClass(Point.class);
        clientUserCodeDeploymentConfig.addClass(Point.PointBuilder.class);
        clientUserCodeDeploymentConfig.addClass(WalkStep.class);
        clientUserCodeDeploymentConfig.addClass(WalkStep.WalkStepBuilder.class);
        clientUserCodeDeploymentConfig.addClass(WalkingDirection.class);
        clientUserCodeDeploymentConfig.addClass(CompassDirection.class);

        clientUserCodeDeploymentConfig.setEnabled(true);
        return clientUserCodeDeploymentConfig;
    }

}
