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
        ApiToken configuredOtpMapperTestData = getConfiguredJourneyOtpMapperApiToken();

        ApiToken result = classUnderTest.mergeJourneyApiTokensWith(requestTestData, configuredOtpMapperTestData);

        assertThat(result).isEqualToComparingFieldByFieldRecursively(getJourneyOtpMapperApiToken());
    }

    @Test
    void test_mergeJourneyApiTokensWith_routePersistenceApiToken_arrivalCoordinate_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken requestTestData = new ApiToken(getRoutePersistenceApiToken());
        requestTestData.setArrivalCoordinate(null);
        ApiToken configuredOtpMapperTestData = getConfiguredJourneyOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(requestTestData, configuredOtpMapperTestData));
    }

    @Test
    void test_mergeJourneyApiTokensWith_routePersistenceApiToken_departureCoordinate_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken requestTestData = new ApiToken(getRoutePersistenceApiToken());
        requestTestData.setDepartureCoordinate(null);
        ApiToken configuredOtpMapperTestData = getConfiguredJourneyOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(requestTestData, configuredOtpMapperTestData));
    }

    @Test
    void test_mergeJourneyApiTokensWith_routePersistenceApiToken_dateTime_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken requestTestData = new ApiToken(getRoutePersistenceApiToken());
        requestTestData.setDateTime(null);
        ApiToken configuredOtpMapperTestData = getConfiguredJourneyOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(requestTestData, configuredOtpMapperTestData));
    }

    @Test
    void test_mergeJourneyApiTokensWith_routePersistenceApiToken_isArrivalDateTime_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken requestTestData = new ApiToken(getRoutePersistenceApiToken());
        requestTestData.setIsArrivalDateTime(null);
        ApiToken configuredOtpMapperTestData = getConfiguredJourneyOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(requestTestData, configuredOtpMapperTestData));
    }

    @Test
    void test_mergeJourneyApiTokensWith_routePersistenceApiToken_language_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken requestTestData = new ApiToken(getRoutePersistenceApiToken());
        requestTestData.setLanguage(null);
        ApiToken configuredOtpMapperTestData = getConfiguredJourneyOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(requestTestData, configuredOtpMapperTestData));
    }

}
