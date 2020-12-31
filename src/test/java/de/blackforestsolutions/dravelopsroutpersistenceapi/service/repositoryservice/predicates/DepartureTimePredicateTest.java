package de.blackforestsolutions.dravelopsroutpersistenceapi.service.repositoryservice.predicates;

import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates.DepartureTimePredicate;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Map;

import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.JourneyObjectMother.getJourneyWithNoEmptyFieldsByDepartureTime;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.ShaIdObjectMother.SHA_ID_1;
import static org.assertj.core.api.Assertions.assertThat;

class DepartureTimePredicateTest {

    private final ZonedDateTime minDepartureTimeToCompare = ZonedDateTime.parse("2020-12-24T12:00:00+02:00[Europe/Berlin]");
    private final int maxDepartureTimeInMinutes = 120;

    private final DepartureTimePredicate classUnderTest = new DepartureTimePredicate(minDepartureTimeToCompare, maxDepartureTimeInMinutes);

    @Test
    void test_test_apply_journeyEntry_returns_true_when_minDepartureTimeToCompare_and_testDepartureTime_are_equal() {
        ZonedDateTime testDepartureTime = ZonedDateTime.parse("2020-12-24T12:00:00+02:00[Europe/Berlin]");
        Journey testData = getJourneyWithNoEmptyFieldsByDepartureTime(testDepartureTime);
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isTrue();
    }

    @Test
    void test_test_apply_journeyEntry_returns_true_when_maxDepartureTimeToCompare_and_testDepartureTime_are_equal() {
        ZonedDateTime testDepartureTime = ZonedDateTime.parse("2020-12-24T14:00:00+02:00[Europe/Berlin]");
        Journey testData = getJourneyWithNoEmptyFieldsByDepartureTime(testDepartureTime);
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isTrue();
    }

    @Test
    void test_apply_journeyEntry_returns_true_when_testDepartureTime_is_between_minDepartureTimeToCompare_and_maxDepartureTimeToCompare() {
        ZonedDateTime testDepartureTime = ZonedDateTime.parse("2020-12-24T13:00:00+02:00[Europe/Berlin]");
        Journey testData = getJourneyWithNoEmptyFieldsByDepartureTime(testDepartureTime);
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isTrue();
    }

    @Test
    void test_apply_journeyEntry_returns_true_when_testDepartureTime_isBefore_minDepartureTimeToCompare() {
        ZonedDateTime testDepartureTime = ZonedDateTime.parse("2020-12-24T11:59:59+02:00[Europe/Berlin]");
        Journey testData = getJourneyWithNoEmptyFieldsByDepartureTime(testDepartureTime);
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isFalse();
    }

    @Test
    void test_apply_journeyEntry_returns_true_when_testDepartureTime_isAfter_maxDepartureTimeToCompare() {
        ZonedDateTime testDepartureTime = ZonedDateTime.parse("2020-12-24T14:00:01+02:00[Europe/Berlin]");
        Journey testData = getJourneyWithNoEmptyFieldsByDepartureTime(testDepartureTime);
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isFalse();
    }
}
