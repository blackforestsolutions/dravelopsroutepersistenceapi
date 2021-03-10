package de.blackforestsolutions.dravelopsroutpersistenceapi.service.repositoryservice.predicates;

import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Point;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates.DeparturePointPredicate;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getJourneyWithNoEmptyFieldsByDeparturePoint;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.UUIDObjectMother.TEST_UUID_1;
import static org.assertj.core.api.Assertions.assertThat;

class DeparturePointPredicateTest {

    private final Point departurePointToCompare = new Point.PointBuilder(0.0d, 0.0d).build();

    private final DeparturePointPredicate classUnderTest = new DeparturePointPredicate(departurePointToCompare);

    @Test
    void test_apply_journeyEntry_returns_true_when_departurePointLongitude_and_departurePointLatitude_are_congruent() {
        Journey testData = getJourneyWithNoEmptyFieldsByDeparturePoint(new Point.PointBuilder(0.0d, 0.0d).build(), TEST_UUID_1);
        Map.Entry<UUID, Journey> testEntry = Map.entry(testData.getId(), testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isTrue();
    }

    @Test
    void test_applyEntry_returns_true_when_departurePointLongitude_and_departurePointLatitude_are_equal_edge_case() {
        Journey testData = getJourneyWithNoEmptyFieldsByDeparturePoint(new Point.PointBuilder(0.000001d, 0.000001d).build(), TEST_UUID_1);
        Map.Entry<UUID, Journey> testEntry = Map.entry(testData.getId(), testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isTrue();
    }

    @Test
    void test_apply_journeyEntry_returns_false_when_departurePointLongitude_and_departurePointLatitude_are_not_congruent() {
        Journey testData = getJourneyWithNoEmptyFieldsByDeparturePoint(new Point.PointBuilder(5.0d, 5.0d).build(), TEST_UUID_1);
        Map.Entry<UUID, Journey> testEntry = Map.entry(testData.getId(), testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isFalse();
    }

    @Test
    void test_apply_journeyEntry_returns_false_when_departurePointLatitude_is_not_congruent() {
        Journey testData = getJourneyWithNoEmptyFieldsByDeparturePoint(new Point.PointBuilder(0.0d, 5.0d).build(), TEST_UUID_1);
        Map.Entry<UUID, Journey> testEntry = Map.entry(testData.getId(), testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isFalse();
    }

    @Test
    void test_apply_journeyEntry_returns_false_when_departurePointLongitude_is_not_congruent() {
        Journey testData = getJourneyWithNoEmptyFieldsByDeparturePoint(new Point.PointBuilder(5.0d, 0.0d).build(), TEST_UUID_1);
        Map.Entry<UUID, Journey> testEntry = Map.entry(testData.getId(), testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isFalse();
    }
}
