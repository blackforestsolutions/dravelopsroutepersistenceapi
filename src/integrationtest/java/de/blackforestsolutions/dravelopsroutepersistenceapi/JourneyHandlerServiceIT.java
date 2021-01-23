package de.blackforestsolutions.dravelopsroutepersistenceapi;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.JourneyHandlerServiceTestConfiguration;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice.JourneyHandlerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@Import(JourneyHandlerServiceTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JourneyHandlerServiceIT {

    @Autowired
    private JourneyHandlerService classUnderTest;

    @Autowired
    private ApiToken.ApiTokenBuilder routePersistenceApiToken;

    @Test
    void test_retrieveJourneysFromApiOrRepositoryService_returns_journeys() {
        ApiToken testData = routePersistenceApiToken.build();

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
                            .allMatch(leg -> leg.getWaypoints().size() > 0)
                            .allMatch(leg -> leg.getIntermediateStops().size() == 0 || leg.getIntermediateStops().size() > 0)
                            .allMatch(leg -> leg.getVehicleName() != null)
                            .allMatch(leg -> leg.getVehicleNumber() != null)
                            .allMatch(leg -> leg.getDeparture() != null)
                            .allMatch(leg -> !leg.getDeparture().getName().isEmpty())
                            .allMatch(leg -> leg.getDeparture().getPoint() != null)
                            .allMatch(leg -> leg.getArrival() != null)
                            .allMatch(leg -> !leg.getArrival().getName().isEmpty())
                            .allMatch(leg -> leg.getArrival().getPoint() != null);
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
}
