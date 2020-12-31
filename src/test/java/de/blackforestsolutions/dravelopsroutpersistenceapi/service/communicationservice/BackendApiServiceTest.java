package de.blackforestsolutions.dravelopsroutpersistenceapi.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.exceptionhandling.ExceptionHandlerServiceImpl;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice.BackendApiService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice.BackendApiServiceImpl;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice.restcalls.CallServiceImpl;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.RequestTokenHandlerService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.RequestTokenHandlerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getConfiguredOtpMapperApiToken;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getRoutePersistenceApiToken;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getFurtwangenToWaldkirchJourney;
import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BackendApiServiceTest {

    private final CallService callService = mock(CallServiceImpl.class);
    private final ExceptionHandlerService exceptionHandlerService = spy(ExceptionHandlerServiceImpl.class);
    private final RequestTokenHandlerService requestTokenHandlerService = new RequestTokenHandlerServiceImpl();

    private final BackendApiService classUnderTest = new BackendApiServiceImpl(callService, exceptionHandlerService);

    @BeforeEach
    void init() {
        Journey mockedJourney = getFurtwangenToWaldkirchJourney();
        when(callService.postMany(anyString(), any(ApiToken.class), any(HttpHeaders.class), eq(Journey.class)))
                .thenReturn(Flux.just(mockedJourney));
    }

    @Test
    void test_getManyBy_routePersistenceApiToken_and_configured_otpMapperApiToken_returns_journeys() {
        ApiToken configuredTestToken = getConfiguredOtpMapperApiToken();
        ApiToken routePersistenceToken = getRoutePersistenceApiToken();

        Flux<Journey> result = classUnderTest.getManyBy(routePersistenceToken, configuredTestToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class);

        StepVerifier.create(result)
                .assertNext(journey -> assertThat(toJson(journey)).isEqualTo(toJson(getFurtwangenToWaldkirchJourney())))
                .verifyComplete();
    }

    @Test
    void test_getManyBy_routePersistenceApiToken_and_configured_otpMapperApiToken_returns_no_journeys_when_otp_has_no_journeys_found() {
        ApiToken configuredTestToken = getConfiguredOtpMapperApiToken();
        ApiToken routePersistenceToken = getRoutePersistenceApiToken();
        when(callService.postMany(anyString(), any(ApiToken.class), any(HttpHeaders.class), eq(Journey.class)))
                .thenReturn(Flux.empty());

        Flux<Journey> result = classUnderTest.getManyBy(routePersistenceToken, configuredTestToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    void test_getManyBy_routePersistenceApiToken_and_configured_otpMapperApiToken_is_executed_correctly_when_journeys_are_returned() {
        ApiToken configuredTestToken = getConfiguredOtpMapperApiToken();
        ApiToken routePersistenceToken = getRoutePersistenceApiToken();
        ArgumentCaptor<String> urlArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ApiToken> bodyArg = ArgumentCaptor.forClass(ApiToken.class);
        ArgumentCaptor<HttpHeaders> httpHeadersArg = ArgumentCaptor.forClass(HttpHeaders.class);

        classUnderTest.getManyBy(routePersistenceToken, configuredTestToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class)
                .collectList()
                .block();

        verify(callService, times(1)).postMany(urlArg.capture(), bodyArg.capture(), httpHeadersArg.capture(), eq(Journey.class));
        assertThat(urlArg.getValue()).isEqualTo("http://localhost:8084/otp/journeys/get");
        assertThat(bodyArg.getValue()).isEqualToComparingFieldByField(getRoutePersistenceApiToken());
        assertThat(httpHeadersArg.getValue()).isEqualTo(HttpHeaders.EMPTY);
    }

    @Test
    void test_getManyBy_routePersistenceApiToken_with_language_as_null_and_configured_apiToken_returns_failed_call_status() {
        ArgumentCaptor<Throwable> exceptionArg = ArgumentCaptor.forClass(Throwable.class);
        ApiToken.ApiTokenBuilder routePersistenceToken = new ApiToken.ApiTokenBuilder(getRoutePersistenceApiToken());
        routePersistenceToken.setLanguage(null);
        ApiToken configuredTestToken = getConfiguredOtpMapperApiToken();

        Flux<Journey> result = classUnderTest.getManyBy(routePersistenceToken.build(), configuredTestToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
        verify(exceptionHandlerService, times(1)).handleExceptions(exceptionArg.capture());
        assertThat(exceptionArg.getValue()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_getManyBy_routePersistenceApiToken_and_configured_apiToken_and_host_as_null_returns_failed_call_status() {
        ArgumentCaptor<Throwable> exceptionArg = ArgumentCaptor.forClass(Throwable.class);
        ApiToken.ApiTokenBuilder configuredTestToken = new ApiToken.ApiTokenBuilder(getConfiguredOtpMapperApiToken());
        ApiToken routePersistenceToken = getRoutePersistenceApiToken();
        configuredTestToken.setHost(null);

        Flux<Journey> result = classUnderTest.getManyBy(routePersistenceToken, configuredTestToken.build(), requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
        verify(exceptionHandlerService, times(1)).handleExceptions(exceptionArg.capture());
        assertThat(exceptionArg.getValue()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_getManyBy_routePersistenceApiToken_as_null_and_configured_apiToken_as_null_returns_failed_call_status_when_exception_is_thrown_outside_of_stream() {

        Flux<Journey> result = classUnderTest.getManyBy(null, null, null, Journey.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    void test_getManyBy_apiToken_and_error_by_call_service_returns_failed_call_status() {
        ApiToken configuredTestToken = getConfiguredOtpMapperApiToken();
        ApiToken routePersistenceToken = getRoutePersistenceApiToken();
        when(callService.postMany(anyString(), any(ApiToken.class), any(HttpHeaders.class), eq(Journey.class)))
                .thenReturn(Flux.error(new Exception()));

        Flux<Journey> result = classUnderTest.getManyBy(routePersistenceToken, configuredTestToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }


}
