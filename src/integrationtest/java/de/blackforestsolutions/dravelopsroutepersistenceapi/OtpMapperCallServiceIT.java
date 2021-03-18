package de.blackforestsolutions.dravelopsroutepersistenceapi;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Point;
import de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.OtpMapperTestConfiguration;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice.restcalls.CallService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsHttpCallBuilder.buildUrlWith;
import static org.assertj.core.api.Assertions.assertThat;

@Import(OtpMapperTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class OtpMapperCallServiceIT {

    @Autowired
    private CallService classUnderTest;

    @Autowired
    private ApiToken.ApiTokenBuilder journeyOtpMapperApiTokenIT;


    @Test
    void test_postMany_from_otpMapperService_returns_journeys() {

        Flux<Journey> result = classUnderTest.postMany(buildUrlWith(journeyOtpMapperApiTokenIT.build()).toString(), journeyOtpMapperApiTokenIT.build(), HttpHeaders.EMPTY, Journey.class);

        StepVerifier.create(result)
                .expectNextCount(1L)
                .thenConsumeWhile(journey -> true, journey -> assertThat(journey.getLegs().size()).isGreaterThan(0))
                .verifyComplete();
    }

    @Test
    void test_postMany_from_otpMapperService_without_being_inside_area_returns_no_journeys() {
        ApiToken.ApiTokenBuilder testData = new ApiToken.ApiTokenBuilder(journeyOtpMapperApiTokenIT.build());
        testData.setDepartureCoordinate(new Point.PointBuilder(0.0d, 0.0d).build());
        testData.setDeparture("middlepoint of earth");
        testData.setArrivalCoordinate(new Point.PointBuilder(0.1d, 0.1d).build());
        testData.setArrival("middlepoint of earth");

        Flux<Journey> result = classUnderTest.postMany(buildUrlWith(testData.build()).toString(), testData.build(), HttpHeaders.EMPTY, Journey.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }
}
