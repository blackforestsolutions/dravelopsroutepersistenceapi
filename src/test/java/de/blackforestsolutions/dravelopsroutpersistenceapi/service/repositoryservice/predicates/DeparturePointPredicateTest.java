package de.blackforestsolutions.dravelopsroutpersistenceapi.service.repositoryservice.predicates;

import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates.DeparturePointPredicate;
import org.junit.jupiter.api.Test;
import org.springframework.data.geo.Point;

import java.util.Map;

import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.JourneyObjectMother.getJourneyWithNoEmptyFieldsByDeparturePoint;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.ShaIdObjectMother.SHA_ID_1;
import static org.assertj.core.api.Assertions.assertThat;

class DeparturePointPredicateTest {

    private final Point departurePointToCompare = new Point(0.0d, 0.0d);

    private final DeparturePointPredicate classUnderTest = new DeparturePointPredicate(departurePointToCompare);

    @Test
    void test_apply_journeyEntry_returns_true_when_departurePointLongitude_and_departurePointLatitude_are_congruent() {
        Journey testData = getJourneyWithNoEmptyFieldsByDeparturePoint(new Point(0.0d, 0.0d));
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isTrue();
    }

    @Test
    void test_apply_journeyEntry_returns_false_when_departurePointLongitude_and_departurePointLatitude_are_not_congruent() {
        Journey testData = getJourneyWithNoEmptyFieldsByDeparturePoint(new Point(5.0d, 5.0d));
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isFalse();
    }

    @Test
    void test_apply_journeyEntry_returns_false_when_departurePointLatitude_is_not_congruent() {
        Journey testData = getJourneyWithNoEmptyFieldsByDeparturePoint(new Point(0.0d, 5.0d));
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isFalse();
    }

    @Test
    void test_apply_journeyEntry_returns_false_when_departurePointLongitude_is_not_congruent() {
        Journey testData = getJourneyWithNoEmptyFieldsByDeparturePoint(new Point(5.0d, 0.0d));
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isFalse();
    }
}
