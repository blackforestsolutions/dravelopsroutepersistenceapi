package de.blackforestsolutions.dravelopsroutpersistenceapi.service.repositoryservice;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.test.TestHazelcastInstanceFactory;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Point;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyReadRepositoryService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyReadRepositoryServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getApiTokenWithNoEmptyFieldsBy;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getHazelcastApiToken;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getJourneyWithNoEmptyFieldsBy;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getJourneyWithNoEmptyFieldsById;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.UUIDObjectMother.*;
import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.HazelcastConfiguration.JOURNEY_MAP;
import static org.assertj.core.api.Assertions.assertThat;

class JourneyReadRepositoryServiceTest {

    private HazelcastInstance hazelcastMock;
    private final ApiToken hazelcastApiToken = getHazelcastApiToken();

    private JourneyReadRepositoryService classUnderTest;

    @BeforeEach
    void init() {
        hazelcastMock = new TestHazelcastInstanceFactory(1).newHazelcastInstance();
        classUnderTest = new JourneyReadRepositoryServiceImpl(hazelcastMock, hazelcastApiToken);
    }

    @AfterEach
    void tearDown() {
        Hazelcast.shutdownAll();
    }

    @Test
    void test_getJourneyById_with_one_journey_in_hazelcast_returns_one_journey_when_uuid_is_equal() {
        IMap<UUID, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(TEST_UUID_1, getJourneyWithNoEmptyFieldsById(TEST_UUID_1));

        Journey result = classUnderTest.getJourneyById(TEST_UUID_1);

        assertThat(result).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsById(TEST_UUID_1));
    }

    @Test
    void test_getJourneyById_with_one_journey_in_hazelcast_returns_null_when_no_journey_is_found() {
        IMap<UUID, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(TEST_UUID_2, getJourneyWithNoEmptyFieldsById(TEST_UUID_2));

        Journey result = classUnderTest.getJourneyById(TEST_UUID_1);

        assertThat(result).isNull();
    }

    @Test
    void test_getJourneysSortedByDepartureDateWith_one_correct_journey_and_one_journey_with_wrong_language_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.now(),
                new Locale("de")
        );
        ApiToken wrongLanguageToken = new ApiToken(correctToken);
        wrongLanguageToken.setLanguage(new Locale("en"));
        Journey correctJourney = getJourneyWithNoEmptyFieldsBy(correctToken, TEST_UUID_1);
        Journey wrongJourney = getJourneyWithNoEmptyFieldsBy(wrongLanguageToken, TEST_UUID_2);
        IMap<UUID, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(correctJourney.getId(), correctJourney);
        hazelcastJourneys.put(wrongJourney.getId(), wrongJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByDepartureDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(correctToken, TEST_UUID_1));
    }

    @Test
    void test_getJourneysSortedByArrivalDateWithWith_one_correct_journey_and_one_journey_with_wrong_language_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.now(),
                new Locale("de")
        );
        ApiToken wrongLanguageToken = new ApiToken(correctToken);
        wrongLanguageToken.setLanguage(new Locale("en"));
        Journey correctJourney = getJourneyWithNoEmptyFieldsBy(correctToken, TEST_UUID_1);
        Journey wrongJourney = getJourneyWithNoEmptyFieldsBy(wrongLanguageToken, TEST_UUID_2);
        IMap<UUID, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(correctJourney.getId(), correctJourney);
        hazelcastJourneys.put(wrongJourney.getId(), wrongJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByArrivalDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(correctToken, TEST_UUID_1));
    }

    @Test
    void test_getJourneysSortedByDepartureDateWith_one_correct_journey_and_one_journey_with_wrong_arrivalPoint_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.now(),
                new Locale("de")
        );
        ApiToken wrongArrivalPointToken = new ApiToken(correctToken);
        wrongArrivalPointToken.setArrivalCoordinate(new Point.PointBuilder(5.0d, 5.0d).build());
        Journey correctJourney = getJourneyWithNoEmptyFieldsBy(correctToken, TEST_UUID_1);
        Journey wrongJourney = getJourneyWithNoEmptyFieldsBy(wrongArrivalPointToken, TEST_UUID_2);
        IMap<UUID, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(correctJourney.getId(), correctJourney);
        hazelcastJourneys.put(wrongJourney.getId(), wrongJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByDepartureDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(correctToken, TEST_UUID_1));
    }

    @Test
    void test_getJourneysSortedByArrivalDateWith_one_correct_journey_and_one_journey_with_wrong_arrivalPoint_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.now(),
                new Locale("de")
        );
        ApiToken wrongArrivalPointToken = new ApiToken(correctToken);
        wrongArrivalPointToken.setArrivalCoordinate(new Point.PointBuilder(5.0d, 5.0d).build());
        Journey correctJourney = getJourneyWithNoEmptyFieldsBy(correctToken, TEST_UUID_1);
        Journey wrongJourney = getJourneyWithNoEmptyFieldsBy(wrongArrivalPointToken, TEST_UUID_2);
        IMap<UUID, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(correctJourney.getId(), correctJourney);
        hazelcastJourneys.put(wrongJourney.getId(), wrongJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByArrivalDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(correctToken, TEST_UUID_1));
    }

    @Test
    void test_getJourneysSortedByDepartureDateWith_one_correct_journey_and_one_journey_with_wrong_departurePoint_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.now(),
                new Locale("de")
        );
        ApiToken wrongDeparturePointToken = new ApiToken(correctToken);
        wrongDeparturePointToken.setDepartureCoordinate(new Point.PointBuilder(5.0d, 5.0d).build());
        Journey correctJourney = getJourneyWithNoEmptyFieldsBy(correctToken, TEST_UUID_1);
        Journey wrongJourney = getJourneyWithNoEmptyFieldsBy(wrongDeparturePointToken, TEST_UUID_2);
        IMap<UUID, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(correctJourney.getId(), correctJourney);
        hazelcastJourneys.put(wrongJourney.getId(), wrongJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByDepartureDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(correctToken, TEST_UUID_1));
    }

    @Test
    void test_getJourneysSortedByArrivalDateWith_one_correct_journey_and_one_journey_with_wrong_departurePoint_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.now(),
                new Locale("de")
        );
        ApiToken wrongDeparturePointToken = new ApiToken(correctToken);
        wrongDeparturePointToken.setDepartureCoordinate(new Point.PointBuilder(5.0d, 5.0d).build());
        Journey correctJourney = getJourneyWithNoEmptyFieldsBy(correctToken, TEST_UUID_1);
        Journey wrongJourney = getJourneyWithNoEmptyFieldsBy(wrongDeparturePointToken, TEST_UUID_2);
        IMap<UUID, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(correctJourney.getId(), correctJourney);
        hazelcastJourneys.put(wrongJourney.getId(), wrongJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByArrivalDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(correctToken, TEST_UUID_1));
    }

    @Test
    void test_getJourneysSortedByDepartureDateWith_one_correct_journey_and_one_journey_with_wrong_departureTime_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.now(),
                new Locale("de")
        );
        ApiToken wrongDepartureTimeToken = new ApiToken(correctToken);
        wrongDepartureTimeToken.setDateTime(ZonedDateTime.now().plusDays(1));
        Journey correctJourney = getJourneyWithNoEmptyFieldsBy(correctToken, TEST_UUID_1);
        Journey wrongJourney = getJourneyWithNoEmptyFieldsBy(wrongDepartureTimeToken, TEST_UUID_2);
        IMap<UUID, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(correctJourney.getId(), correctJourney);
        hazelcastJourneys.put(wrongJourney.getId(), wrongJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByDepartureDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(correctToken, TEST_UUID_1));
    }

    @Test
    void test_getJourneysSortedByArrivalDateWith_one_correct_journey_and_one_journey_with_wrong_arrivalTime_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.now(),
                new Locale("de")
        );
        ApiToken wrongArrivalTimeToken = new ApiToken(correctToken);
        wrongArrivalTimeToken.setDateTime(ZonedDateTime.now().minusDays(1));
        Journey correctJourney = getJourneyWithNoEmptyFieldsBy(correctToken, TEST_UUID_1);
        Journey wrongJourney = getJourneyWithNoEmptyFieldsBy(wrongArrivalTimeToken, TEST_UUID_2);
        IMap<UUID, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(correctJourney.getId(), correctJourney);
        hazelcastJourneys.put(wrongJourney.getId(), wrongJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByArrivalDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(correctToken, TEST_UUID_1));
    }

    @Test
    void test_getJourneysSortedByDepartureDateWith_three_correct_journeys_returns_three_journeys_sorted_by_departureDate() {
        ApiToken firstCorrectToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.parse("2020-12-28T12:00:00+02:00[Europe/Berlin]"),
                new Locale("de")
        );
        ApiToken secondCorrectToken = new ApiToken(firstCorrectToken);
        secondCorrectToken.setDateTime(ZonedDateTime.parse("2020-12-28T13:00:00+02:00[Europe/Berlin]"));
        ApiToken thirdCorrectToken = new ApiToken(firstCorrectToken);
        thirdCorrectToken.setDateTime(ZonedDateTime.parse("2020-12-28T14:00:00+02:00[Europe/Berlin]"));
        Journey thirdCorrectJourney = getJourneyWithNoEmptyFieldsBy(thirdCorrectToken, TEST_UUID_1);
        Journey secondCorrectJourney = getJourneyWithNoEmptyFieldsBy(secondCorrectToken, TEST_UUID_2);
        Journey firstCorrectJourney = getJourneyWithNoEmptyFieldsBy(firstCorrectToken, TEST_UUID_3);
        IMap<UUID, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(thirdCorrectJourney.getId(), thirdCorrectJourney);
        hazelcastJourneys.put(secondCorrectJourney.getId(), secondCorrectJourney);
        hazelcastJourneys.put(firstCorrectJourney.getId(), firstCorrectJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByDepartureDateWith(firstCorrectToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(3);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(firstCorrectToken, TEST_UUID_3));
        assertThat(listResult.get(1)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(secondCorrectToken, TEST_UUID_2));
        assertThat(listResult.get(2)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(thirdCorrectToken, TEST_UUID_1));
    }

    @Test
    void test_getJourneysSortedByArrivalDateWith_three_correct_journeys_returns_three_journeys_sorted_by_arrivalDate() {
        ApiToken firstCorrectToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.parse("2020-12-28T12:00:00+02:00[Europe/Berlin]"),
                new Locale("de")
        );
        ApiToken secondCorrectToken = new ApiToken(firstCorrectToken);
        secondCorrectToken.setDateTime(ZonedDateTime.parse("2020-12-28T11:00:00+02:00[Europe/Berlin]"));
        ApiToken thirdCorrectToken = new ApiToken(firstCorrectToken);
        thirdCorrectToken.setDateTime(ZonedDateTime.parse("2020-12-28T10:00:00+02:00[Europe/Berlin]"));
        Journey thirdCorrectJourney = getJourneyWithNoEmptyFieldsBy(thirdCorrectToken, TEST_UUID_1);
        Journey secondCorrectJourney = getJourneyWithNoEmptyFieldsBy(secondCorrectToken, TEST_UUID_2);
        Journey firstCorrectJourney = getJourneyWithNoEmptyFieldsBy(firstCorrectToken, TEST_UUID_3);
        IMap<UUID, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(thirdCorrectJourney.getId(), thirdCorrectJourney);
        hazelcastJourneys.put(secondCorrectJourney.getId(), secondCorrectJourney);
        hazelcastJourneys.put(firstCorrectJourney.getId(), firstCorrectJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByArrivalDateWith(firstCorrectToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(3);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(firstCorrectToken, TEST_UUID_3));
        assertThat(listResult.get(1)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(secondCorrectToken, TEST_UUID_2));
        assertThat(listResult.get(2)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(thirdCorrectToken, TEST_UUID_1));
    }
}
