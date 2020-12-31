package de.blackforestsolutions.dravelopsroutpersistenceapi.service.repositoryservice.predicates;

import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates.ArrivalTimePredicate;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Map;

import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.JourneyObjectMother.getJourneyWithNoEmptyFieldsByArrivalTime;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.ShaIdObjectMother.SHA_ID_1;
import static org.assertj.core.api.Assertions.assertThat;

class ArrivalTimePredicateTest {

    private final ZonedDateTime maxArrivalTimeToCompare = ZonedDateTime.parse("2020-12-28T12:00:00+02:00[Europe/Berlin]");
    private final int minArrivalTimeInMinutes = 120;

    private final ArrivalTimePredicate classUnderTest = new ArrivalTimePredicate(maxArrivalTimeToCompare, minArrivalTimeInMinutes);

    @Test
    void test_apply_journeyEntry_returns_true_when_maxArrivalTimeToCompare_and_testArrivalTime_are_equal() {
        ZonedDateTime testArrivalTime = ZonedDateTime.parse("2020-12-28T12:00:00+02:00[Europe/Berlin]");
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalTime(testArrivalTime);
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isTrue();
    }

    @Test
    void test_apply_journeyEntry_returns_true_when_minArrivalTimeToCompare_and_testArrivalTime_are_equal() {
        ZonedDateTime testArrivalTime = ZonedDateTime.parse("2020-12-28T10:00:00+02:00[Europe/Berlin]");
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalTime(testArrivalTime);
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isTrue();
    }

    @Test
    void test_apply_journeyEntry_returns_true_when_testArrivalTime_is_between_minArrivalTimeToCompare_and_maxArrivalTimeToCompare() {
        ZonedDateTime testArrivalTime = ZonedDateTime.parse("2020-12-28T11:00:00+02:00[Europe/Berlin]");
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalTime(testArrivalTime);
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isTrue();
    }

    @Test
    void test_apply_journeyEntry_returns_false_when_testArrivalTime_isBefore_minArrivalTimeToCompare() {
        ZonedDateTime testArrivalTime = ZonedDateTime.parse("2020-12-28T09:59:59+02:00[Europe/Berlin]");
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalTime(testArrivalTime);
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isFalse();
    }

    @Test
    void test_apply_journeyEntry_returns_false_when_testArrivalTime_isAfter_maxArrivalTimeToCompare() {
        ZonedDateTime testArrivalTime = ZonedDateTime.parse("2020-12-28T12:00:01+02:00[Europe/Berlin]");
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalTime(testArrivalTime);
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isFalse();
    }

}

