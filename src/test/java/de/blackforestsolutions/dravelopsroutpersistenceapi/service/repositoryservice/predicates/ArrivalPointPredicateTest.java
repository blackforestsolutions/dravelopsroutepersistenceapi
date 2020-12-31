package de.blackforestsolutions.dravelopsroutpersistenceapi.service.repositoryservice.predicates;

import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates.ArrivalPointPredicate;
import org.junit.jupiter.api.Test;
import org.springframework.data.geo.Point;

import java.util.Map;

import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.JourneyObjectMother.getJourneyWithNoEmptyFieldsByArrivalPoint;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.ShaIdObjectMother.SHA_ID_1;
import static org.assertj.core.api.Assertions.assertThat;

class ArrivalPointPredicateTest {

    private final Point arrivalPointToCompare = new Point(0.0d, 0.0d);

    private final ArrivalPointPredicate classUnderTest = new ArrivalPointPredicate(arrivalPointToCompare);

    @Test
    void test_apply_journeyEntry_returns_true_when_arrivalPointLongitude_and_arrivalPointLatitude_are_congruent() {
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalPoint(new Point(0.0d, 0.0d));
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isTrue();
    }

    @Test
    void test_apply_journeyEntry_returns_false_when_arrivalPointLongitude_and_arrivalPointLatitude_are_not_congruent() {
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalPoint(new Point(5.0d, 5.0d));
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isFalse();
    }

    @Test
    void test_apply_journeyEntry_returns_false_when_arrivalPointLatitude_is_not_congruent() {
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalPoint(new Point(0.0d, 5.0d));
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isFalse();
    }

    @Test
    void test_apply_journeyEntry_returns_false_when_arrivalPointLongitude_is_not_congruent() {
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalPoint(new Point(5.0d, 0.0d));
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isFalse();
    }

}
