package de.blackforestsolutions.dravelopsroutpersistenceapi.service.communicationservice;

import com.hazelcast.core.HazelcastException;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.exceptionhandling.ExceptionHandlerServiceImpl;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice.*;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyCreateRepositoryService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyCreateRepositoryServiceImpl;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyReadRepositoryService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyReadRepositoryServiceImpl;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.RequestTokenHandlerService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.RequestTokenHandlerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.stream.Stream;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getConfiguredOtpMapperApiToken;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getRoutePersistenceApiToken;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getFurtwangenToWaldkirchJourney;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JourneyHandlerServiceTest {

    private final RequestTokenHandlerService requestTokenHandlerService = new RequestTokenHandlerServiceImpl();
    private final JourneyCreateRepositoryService journeyCreateRepositoryService = mock(JourneyCreateRepositoryServiceImpl.class);
    private final JourneyReadRepositoryService journeyReadRepositoryService = mock(JourneyReadRepositoryServiceImpl.class);
    private final BackendApiService backendApiService = mock(BackendApiServiceImpl.class);
    private final ExceptionHandlerService exceptionHandlerService = spy(ExceptionHandlerServiceImpl.class);
    private final ApiToken configuredOtpMapperApiToken = getConfiguredOtpMapperApiToken();

    private final JourneyHandlerService classUnderTest = new JourneyHandlerServiceImpl(
            requestTokenHandlerService,
            journeyCreateRepositoryService,
            journeyReadRepositoryService,
            backendApiService,
            exceptionHandlerService,
            configuredOtpMapperApiToken
    );

    @BeforeEach
    void init() throws IOException {
        when(journeyCreateRepositoryService.writeJourneyToMapWith(any(Journey.class)))
                .thenReturn(null);

        when(journeyReadRepositoryService.getJourneysSortedByArrivalDateWith(any(ApiToken.class)))
                .thenReturn(Stream.of(getFurtwangenToWaldkirchJourney()));

        when(journeyReadRepositoryService.getJourneysSortedByDepartureDateWith(any(ApiToken.class)))
                .thenReturn(Stream.of(getFurtwangenToWaldkirchJourney()));

        when(backendApiService.getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(Journey.class)))
                .thenReturn(Flux.just(getFurtwangenToWaldkirchJourney()));
    }

    @Test
    void test_retrieveJourneysFromApiOrRepositoryService_returns_one_journey_when_two_journeys_are_equal() {
        ApiToken testData = getRoutePersistenceApiToken();

        Flux<Journey> result = classUnderTest.retrieveJourneysFromApiOrRepositoryService(testData);

        StepVerifier.create(result)
                .assertNext(journey -> assertThat(journey).isEqualToComparingFieldByFieldRecursively(getFurtwangenToWaldkirchJourney()))
                .verifyComplete();
    }

    @Test
    void test_retrieveJourneysFromApiOrRepositoryService_returns_one_journey_when_backend_has_no_result() {
        ApiToken testData = getRoutePersistenceApiToken();
        when(backendApiService.getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(Journey.class)))
                .thenReturn(Flux.empty());

        Flux<Journey> result = classUnderTest.retrieveJourneysFromApiOrRepositoryService(testData);

        StepVerifier.create(result)
                .assertNext(journey -> assertThat(journey).isEqualToComparingFieldByFieldRecursively(getFurtwangenToWaldkirchJourney()))
                .verifyComplete();
    }

    @Test
    void test_retrieveJourneysFromApiOrRepositoryService_returns_one_journey_when_hazelcast_has_no_results() {
        ApiToken testData = getRoutePersistenceApiToken();
        when(journeyReadRepositoryService.getJourneysSortedByDepartureDateWith(any(ApiToken.class)))
                .thenReturn(Stream.empty());

        Flux<Journey> result = classUnderTest.retrieveJourneysFromApiOrRepositoryService(testData);

        StepVerifier.create(result)
                .assertNext(journey -> assertThat(journey).isEqualToComparingFieldByFieldRecursively(getFurtwangenToWaldkirchJourney()))
                .verifyComplete();
    }


    @Test
    void test_retrieveJourneysFromApiOrRepositoryService_with_routePersistenceToke_is_executed_correctly() throws IOException {
        ApiToken testData = getRoutePersistenceApiToken();
        ArgumentCaptor<ApiToken> userRequestArg = ArgumentCaptor.forClass(ApiToken.class);
        ArgumentCaptor<ApiToken> configuredOtpMapperArg = ArgumentCaptor.forClass(ApiToken.class);
        ArgumentCaptor<Journey> journeyArg = ArgumentCaptor.forClass(Journey.class);

        classUnderTest.retrieveJourneysFromApiOrRepositoryService(testData).collectList().block();

        verify(journeyReadRepositoryService, times(0)).getJourneysSortedByArrivalDateWith(any(ApiToken.class));
        verify(journeyReadRepositoryService, times(1)).getJourneysSortedByDepartureDateWith(userRequestArg.capture());
        verify(backendApiService, times(1)).getManyBy(userRequestArg.capture(), configuredOtpMapperArg.capture(), any(RequestHandlerFunction.class), eq(Journey.class));
        verify(journeyCreateRepositoryService, times(1)).writeJourneyToMapWith(journeyArg.capture());
        assertThat(userRequestArg.getAllValues().size()).isEqualTo(2);
        assertThat(userRequestArg.getAllValues().get(0)).isEqualToComparingFieldByField(testData);
        assertThat(userRequestArg.getAllValues().get(1)).isEqualToComparingFieldByField(testData);
        assertThat(configuredOtpMapperArg.getValue()).isEqualToComparingFieldByField(configuredOtpMapperApiToken);
        assertThat(journeyArg.getValue()).isEqualToComparingFieldByFieldRecursively(getFurtwangenToWaldkirchJourney());
    }

    @Test
    void test_retrieveJourneysFromApiOrRepositoryService_with_routePersistenceToke_is_executed_correctly_when_arrivalRepository_is_called() {
        ApiToken.ApiTokenBuilder testData = new ApiToken.ApiTokenBuilder(getRoutePersistenceApiToken());
        testData.setIsArrivalDateTime(true);
        ArgumentCaptor<ApiToken> userRequestArg = ArgumentCaptor.forClass(ApiToken.class);

        classUnderTest.retrieveJourneysFromApiOrRepositoryService(testData.build()).collectList().block();

        verify(journeyReadRepositoryService, times(1)).getJourneysSortedByArrivalDateWith(userRequestArg.capture());
        verify(journeyReadRepositoryService, times(0)).getJourneysSortedByDepartureDateWith(any(ApiToken.class));
        assertThat(userRequestArg.getValue()).isEqualToComparingFieldByField(testData);
    }

    @Test
    void test_retrieveJourneysFromApiOrRepositoryService_returns_journeys_when_writeJourneyToMapWith_fails() throws IOException {
        ApiToken testData = getRoutePersistenceApiToken();
        when(journeyCreateRepositoryService.writeJourneyToMapWith(any(Journey.class)))
                .thenThrow(NullPointerException.class);

        Flux<Journey> result = classUnderTest.retrieveJourneysFromApiOrRepositoryService(testData);

        StepVerifier.create(result)
                .expectNextCount(1L)
                .verifyComplete();
        verify(exceptionHandlerService, times(1)).handleExceptions(any(NullPointerException.class));
    }

    @Test
    void test_retrieveJourneysFromApiOrRepositoryService_returns_journeys_from_repository_when_apiService_throws_exception() {
        ApiToken testData = getRoutePersistenceApiToken();
        when(backendApiService.getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(Journey.class)))
                .thenReturn(Flux.error(new Exception()));

        Flux<Journey> result = classUnderTest.retrieveJourneysFromApiOrRepositoryService(testData);

        StepVerifier.create(result)
                .expectNextCount(1L)
                .verifyComplete();
        verify(exceptionHandlerService, times(1)).handleExceptions(any(Exception.class));
    }

    @Test
    void test_retrieveJourneysFromApiOrRepositoryService_returns_journeys_from_api_when_departureDateRepositoryService_throws_exception() {
        ApiToken testData = getRoutePersistenceApiToken();
        when(journeyReadRepositoryService.getJourneysSortedByDepartureDateWith(any(ApiToken.class)))
                .thenThrow(new HazelcastException());

        Flux<Journey> result = classUnderTest.retrieveJourneysFromApiOrRepositoryService(testData);

        StepVerifier.create(result)
                .expectNextCount(1L)
                .verifyComplete();
        verify(exceptionHandlerService, times(1)).handleExceptions(any(HazelcastException.class));
    }

    @Test
    void test_retrieveJourneysFromApiOrRepositoryService_returns_journeys_from_api_when_arrivalDateRepositoryService_throws_exception() {
        ApiToken.ApiTokenBuilder testData = new ApiToken.ApiTokenBuilder(getRoutePersistenceApiToken());
        testData.setIsArrivalDateTime(true);
        when(journeyReadRepositoryService.getJourneysSortedByArrivalDateWith(any(ApiToken.class)))
                .thenThrow(new HazelcastException());

        Flux<Journey> result = classUnderTest.retrieveJourneysFromApiOrRepositoryService(testData.build());
        StepVerifier.create(result)
                .expectNextCount(1L)
                .verifyComplete();
        verify(exceptionHandlerService, times(1)).handleExceptions(any(HazelcastException.class));
    }

    @Test
    void test_retrieveJourneysFromApiOrRepositoryService_with_routePersistenceToken_returns_no_journeys_when_all_streams_are_empty() {
        ApiToken testData = getRoutePersistenceApiToken();
        when(backendApiService.getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(Journey.class)))
                .thenReturn(Flux.empty());
        when(journeyReadRepositoryService.getJourneysSortedByDepartureDateWith(any(ApiToken.class)))
                .thenReturn(Stream.empty());

        Flux<Journey> result = classUnderTest.retrieveJourneysFromApiOrRepositoryService(testData);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }


}
