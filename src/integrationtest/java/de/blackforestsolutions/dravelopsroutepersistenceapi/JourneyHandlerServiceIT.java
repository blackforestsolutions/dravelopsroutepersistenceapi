package de.blackforestsolutions.dravelopsroutepersistenceapi;

import com.hazelcast.core.HazelcastInstance;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.JourneyHandlerServiceTestConfiguration;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice.JourneyHandlerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.CoordinateConfiguration.*;
import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.HazelcastConfiguration.HAZELCAST_INSTANCE;
import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.HazelcastConfiguration.JOURNEY_MAP;
import static org.assertj.core.api.Assertions.assertThat;

@Import(JourneyHandlerServiceTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class JourneyHandlerServiceIT {

    @Autowired
    private JourneyHandlerService classUnderTest;

    @Autowired
    private ApiToken routePersistenceApiToken;

    @Autowired
    @Qualifier(HAZELCAST_INSTANCE)
    private HazelcastInstance hazelcastInstance;

    @Test
    void test_retrieveJourneysFromApiOrRepositoryService_returns_journeys() {
        ApiToken testData = new ApiToken(routePersistenceApiToken);

        Flux<Journey> result = classUnderTest.retrieveJourneysFromApiOrRepositoryService(testData);

        StepVerifier.create(result)
                .expectNextCount(1L)
                .thenConsumeWhile(journey -> {
                    assertThat(journey.getLanguage().getLanguage().length()).isEqualTo(2);
                    assertThat(journey.getLegs().size()).isGreaterThan(0);
                    assertThat(journey.getLegs())
                            .allMatch(leg -> leg.getDelayInMinutes().toMillis() >= 0)
                            .allMatch(leg -> leg.getDistanceInKilometers().getValue() > 0)
                            .allMatch(leg -> leg.getVehicleType() != null)
                            .allMatch(leg -> leg.getWaypoints() != null)
                            .allMatch(leg -> leg.getWaypoints().stream().allMatch(waypoint -> waypoint.getX() >= MIN_WGS_84_LONGITUDE))
                            .allMatch(leg -> leg.getWaypoints().stream().allMatch(waypoint -> waypoint.getX() <= MAX_WGS_84_LONGITUDE))
                            .allMatch(leg -> leg.getWaypoints().stream().allMatch(waypoint -> waypoint.getY() >= MIN_WGS_84_LATITUDE))
                            .allMatch(leg -> leg.getWaypoints().stream().allMatch(waypoint -> waypoint.getY() <= MAX_WGS_84_LATITUDE))
                            .allMatch(leg -> leg.getVehicleName() != null)
                            .allMatch(leg -> leg.getVehicleNumber() != null)
                            .allMatch(leg -> leg.getDeparture() != null)
                            .allMatch(leg -> !leg.getDeparture().getName().isEmpty())
                            .allMatch(leg -> leg.getDeparture().getPoint() != null)
                            .allMatch(leg -> leg.getDeparture().getPoint().getX() >= MIN_WGS_84_LONGITUDE)
                            .allMatch(leg -> leg.getDeparture().getPoint().getX() <= MAX_WGS_84_LONGITUDE)
                            .allMatch(leg -> leg.getDeparture().getPoint().getY() >= MIN_WGS_84_LATITUDE)
                            .allMatch(leg -> leg.getDeparture().getPoint().getY() <= MAX_WGS_84_LATITUDE)
                            .allMatch(leg -> leg.getDeparture().getDistanceInKilometers() == null)
                            .allMatch(leg -> leg.getArrival() != null)
                            .allMatch(leg -> !leg.getArrival().getName().isEmpty())
                            .allMatch(leg -> leg.getArrival().getPoint() != null)
                            .allMatch(leg -> leg.getArrival().getPoint().getX() >= MIN_WGS_84_LONGITUDE)
                            .allMatch(leg -> leg.getArrival().getPoint().getX() <= MAX_WGS_84_LONGITUDE)
                            .allMatch(leg -> leg.getArrival().getPoint().getY() >= MIN_WGS_84_LATITUDE)
                            .allMatch(leg -> leg.getArrival().getPoint().getY() <= MAX_WGS_84_LATITUDE)
                            .allMatch(leg -> leg.getArrival().getDistanceInKilometers() == null);
                    assertThat(journey.getLegs())
                            .first()
                            .matches(leg -> leg.getDeparture().getArrivalTime() == null);
                    assertThat(journey.getLegs())
                            .last()
                            .matches(leg -> leg.getArrival().getDepartureTime() == null);
                    return true;
                })
                .verifyComplete();
    }

    @AfterEach
    void tearDown() {
        hazelcastInstance.getMap(JOURNEY_MAP).clear();
    }

}
