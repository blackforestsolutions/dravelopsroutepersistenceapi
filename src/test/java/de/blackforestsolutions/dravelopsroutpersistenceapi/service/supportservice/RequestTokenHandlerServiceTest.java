package de.blackforestsolutions.dravelopsroutpersistenceapi.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.RequestTokenHandlerService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.RequestTokenHandlerServiceImpl;
import org.junit.jupiter.api.Test;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestTokenHandlerServiceTest {

    private final RequestTokenHandlerService classUnderTest = new RequestTokenHandlerServiceImpl();

    @Test
    void test_mergeJourneyApiTokensWith_routePersistenceApiToken_and_configuredOtpMapperApiToken_returns_merged_token_for_otpMapperService() {
        ApiToken requestTestData = getRoutePersistenceApiToken();
        ApiToken configuredOtpMapperTestData = getConfiguredOtpMapperApiToken();

        ApiToken result = classUnderTest.mergeJourneyApiTokensWith(requestTestData, configuredOtpMapperTestData);

        assertThat(result).isEqualToComparingFieldByFieldRecursively(getOtpMapperApiToken());
    }

    @Test
    void test_mergeJourneyApiTokensWith_routePersistenceApiToken_arrivalCoordinate_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder requestTestData = new ApiToken.ApiTokenBuilder(getRoutePersistenceApiToken());
        requestTestData.setArrivalCoordinate(null);
        ApiToken configuredOtpMapperTestData = getConfiguredOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(requestTestData.build(), configuredOtpMapperTestData));
    }

    @Test
    void test_mergeJourneyApiTokensWith_routePersistenceApiToken_departureCoordinate_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder requestTestData = new ApiToken.ApiTokenBuilder(getRoutePersistenceApiToken());
        requestTestData.setDepartureCoordinate(null);
        ApiToken configuredOtpMapperTestData = getConfiguredOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(requestTestData.build(), configuredOtpMapperTestData));
    }

    @Test
    void test_mergeJourneyApiTokensWith_routePersistenceApiToken_dateTime_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder requestTestData = new ApiToken.ApiTokenBuilder(getRoutePersistenceApiToken());
        requestTestData.setDateTime(null);
        ApiToken configuredOtpMapperTestData = getConfiguredOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(requestTestData.build(), configuredOtpMapperTestData));
    }

    @Test
    void test_mergeJourneyApiTokensWith_routePersistenceApiToken_optimize_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder requestTestData = new ApiToken.ApiTokenBuilder(getRoutePersistenceApiToken());
        requestTestData.setOptimize(null);
        ApiToken configuredOtpMapperTestData = getConfiguredOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(requestTestData.build(), configuredOtpMapperTestData));
    }

    @Test
    void test_mergeJourneyApiTokensWith_routePersistenceApiToken_language_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder requestTestData = new ApiToken.ApiTokenBuilder(getRoutePersistenceApiToken());
        requestTestData.setLanguage(null);
        ApiToken configuredOtpMapperTestData = getConfiguredOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(requestTestData.build(), configuredOtpMapperTestData));
    }

}
