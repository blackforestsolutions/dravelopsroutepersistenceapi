package de.blackforestsolutions.dravelopsroutpersistenceapi.controller;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.controller.JourneyController;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice.JourneyHandlerService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice.JourneyHandlerServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getRoutePersistenceApiToken;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getJourneyWithEmptyFields;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getJourneyWithNoEmptyFieldsById;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.UUIDObjectMother.TEST_UUID_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JourneyControllerTest {

    private final JourneyHandlerService journeyHandlerService = mock(JourneyHandlerServiceImpl.class);

    private final JourneyController classUnderTest = new JourneyController(journeyHandlerService);

    @Test
    void test_getJourneyById_is_executed_correctly_and_returns_a_journey() {
        when(journeyHandlerService.getJourneyById(any(UUID.class)))
                .thenReturn(Mono.just(getJourneyWithNoEmptyFieldsById(TEST_UUID_1)));

        Mono<Journey> result = classUnderTest.getJourneyById(TEST_UUID_1);

        StepVerifier.create(result)
                .assertNext(journey -> assertThat(journey).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsById(TEST_UUID_1)))
                .verifyComplete();
    }

    @Test
    void test_getJourneyById__is_executed_correctly_when_no_journey_is_available() {
        when(journeyHandlerService.getJourneyById(any(UUID.class)))
                .thenReturn(Mono.empty());

        Mono<Journey> result = classUnderTest.getJourneyById(TEST_UUID_1);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    void test_getJourneysBy_apiToken_is_executed_correctly_and_returns_journeys() {
        ApiToken testData = getRoutePersistenceApiToken();
        ArgumentCaptor<ApiToken> requestArg = ArgumentCaptor.forClass(ApiToken.class);
        when(journeyHandlerService.retrieveJourneysFromApiOrRepositoryService(any(ApiToken.class)))
                .thenReturn(Flux.just(getJourneyWithEmptyFields()));

        Flux<Journey> result = classUnderTest.getJourneysBy(testData);

        verify(journeyHandlerService, times(1)).retrieveJourneysFromApiOrRepositoryService(requestArg.capture());
        assertThat(requestArg.getValue()).isEqualToComparingFieldByFieldRecursively(getRoutePersistenceApiToken());
        StepVerifier.create(result)
                .assertNext(journey -> assertThat(journey).isEqualToComparingFieldByFieldRecursively(getJourneyWithEmptyFields()))
                .verifyComplete();
    }

    @Test
    void test_getJourneysBy_apiToken_is_executed_correctly_when_no_results_are_available() {
        ApiToken testData = getRoutePersistenceApiToken();
        when(journeyHandlerService.retrieveJourneysFromApiOrRepositoryService(any(ApiToken.class)))
                .thenReturn(Flux.empty());

        Flux<Journey> result = classUnderTest.getJourneysBy(testData);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }
}
