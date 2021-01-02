package de.blackforestsolutions.dravelopsroutpersistenceapi.service.repositoryservice.predicates;

import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Point;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates.ArrivalPointPredicate;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getJourneyWithNoEmptyFieldsByArrivalPoint;
import static org.assertj.core.api.Assertions.assertThat;

class ArrivalPointPredicateTest {

    private final Point arrivalPointToCompare = new Point.PointBuilder(0.0d, 0.0d).build();

    private final ArrivalPointPredicate classUnderTest = new ArrivalPointPredicate(arrivalPointToCompare);

    @Test
    void test_apply_journeyEntry_returns_true_when_arrivalPointLongitude_and_arrivalPointLatitude_are_congruent() {
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalPoint(new Point.PointBuilder(0.0d, 0.0d).build());
        Map.Entry<String, Journey> testEntry = Map.entry(testData.getId(), testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isTrue();
    }

    @Test
    void test_applyEntry_returns_true_when_arrivalPointLongitude_and_arrivalPointLatitude_are_equal_edge_case() {
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalPoint(new Point.PointBuilder(0.000001d, 0.000001d).build());
        Map.Entry<String, Journey> testEntry = Map.entry(testData.getId(), testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isTrue();
    }

    @Test
    void test_apply_journeyEntry_returns_false_when_arrivalPointLongitude_and_arrivalPointLatitude_are_not_congruent() {
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalPoint(new Point.PointBuilder(5.0d, 5.0d).build());
        Map.Entry<String, Journey> testEntry = Map.entry(testData.getId(), testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isFalse();
    }

    @Test
    void test_apply_journeyEntry_returns_false_when_arrivalPointLatitude_is_not_congruent() {
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalPoint(new Point.PointBuilder(0.0d, 5.0d).build());
        Map.Entry<String, Journey> testEntry = Map.entry(testData.getId(), testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isFalse();
    }

    @Test
    void test_apply_journeyEntry_returns_false_when_arrivalPointLongitude_is_not_congruent() {
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalPoint(new Point.PointBuilder(5.0d, 0.0d).build());
        Map.Entry<String, Journey> testEntry = Map.entry(testData.getId(), testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isFalse();
    }

}
