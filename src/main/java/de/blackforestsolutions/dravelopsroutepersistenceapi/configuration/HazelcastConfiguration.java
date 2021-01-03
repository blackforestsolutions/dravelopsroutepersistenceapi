package de.blackforestsolutions.dravelopsroutepersistenceapi.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.hazelcast.DravelOpsPortableFactory;
import de.blackforestsolutions.dravelopsdatamodel.hazelcast.classdefinition.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import static de.blackforestsolutions.dravelopsdatamodel.hazelcast.DravelOpsPortableFactory.DRAVEL_OPS_FACTORY_ID;

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
    public Config config() {
        Config config = new Config();
//         UserCodeDeploymentConfig
//        config.setUserCodeDeploymentConfig(userCodeDeploymentConfig());

        // SerializationConfig
        config.getSerializationConfig().addPortableFactory(DRAVEL_OPS_FACTORY_ID, new DravelOpsPortableFactory());
//        config.getSerializationConfig().setPortableVersion(1);
        config.getSerializationConfig().addClassDefinition(JourneyClassDefinition.buildJourneyClassDefinition());
        config.getSerializationConfig().addClassDefinition(PriceClassDefinition.buildPriceClassDefinition());
        config.getSerializationConfig().addClassDefinition(LegClassDefinition.buildLegClassDefinition());
        config.getSerializationConfig().addClassDefinition(TravelPointClassDefinition.buildTravelPointClassDefinition());
        config.getSerializationConfig().addClassDefinition(PointClassDefinition.buildPointClassDefinition());
        config.getSerializationConfig().addClassDefinition(TravelProviderClassDefinition.buildTravelProviderClassDefinition());

        return config;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(Config config) {
        return Hazelcast.newHazelcastInstance(config);
    }

//    private ClientUserCodeDeploymentConfig userCodeDeploymentConfig() {
//        ClientUserCodeDeploymentConfig clientUserCodeDeploymentConfig = new ClientUserCodeDeploymentConfig();
//        // predicates
//        clientUserCodeDeploymentConfig.addClass(DeparturePointPredicate.class);
//        clientUserCodeDeploymentConfig.addClass(ArrivalPointPredicate.class);
//        clientUserCodeDeploymentConfig.addClass(LanguagePredicate.class);
//        clientUserCodeDeploymentConfig.addClass(DepartureTimePredicate.class);
//        clientUserCodeDeploymentConfig.addClass(ArrivalTimePredicate.class);
//        // spring geo
//        clientUserCodeDeploymentConfig.addClass(Distance.class);
//        clientUserCodeDeploymentConfig.addClass(Metric.class);
//        clientUserCodeDeploymentConfig.addClass(Range.class);
//        clientUserCodeDeploymentConfig.addClass(Metrics.class);
//        // datamodel
//        clientUserCodeDeploymentConfig.addClass(Journey.class);
//        clientUserCodeDeploymentConfig.addClass(Leg.class);
//        clientUserCodeDeploymentConfig.addClass(TravelPoint.class);
//        clientUserCodeDeploymentConfig.addClass(VehicleType.class);
//        clientUserCodeDeploymentConfig.addClass(TravelProvider.class);
//        clientUserCodeDeploymentConfig.addClass(Price.class);
//        clientUserCodeDeploymentConfig.addClass(PriceType.class);
//        clientUserCodeDeploymentConfig.addClass(Point.class);
//
//        clientUserCodeDeploymentConfig.setEnabled(true);
//        return clientUserCodeDeploymentConfig;
//    }

}
