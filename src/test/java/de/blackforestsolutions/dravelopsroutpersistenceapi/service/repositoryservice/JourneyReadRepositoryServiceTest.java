package de.blackforestsolutions.dravelopsroutpersistenceapi.service.repositoryservice;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.test.TestHazelcastInstanceFactory;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyReadRepositoryService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyReadRepositoryServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.geo.Point;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getHazelcastApiToken;
import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.toJson;
import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.HazelcastConfiguration.JOURNEY_MAP;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.ApiTokenObjectMother.getApiTokenWithNoEmptyFieldsBy;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.JourneyObjectMother.getJourneyWithNoEmptyFieldsBy;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.ShaIdObjectMother.*;
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
    void test_getJourneysSortedByDepartureDateWith_one_correct_journey_and_one_journey_with_wrong_language_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(new Point(0.0d, 0.0d), new Point(0.0d, 0.0d), ZonedDateTime.now(), new Locale("de"));
        ApiToken wrongLanguageToken = new ApiToken.ApiTokenBuilder(correctToken).setLanguage(new Locale("en")).build();
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(SHA_ID_1, getJourneyWithNoEmptyFieldsBy(correctToken));
        hazelcastJourneys.put(SHA_ID_2, getJourneyWithNoEmptyFieldsBy(wrongLanguageToken));

        Stream<Journey> result = classUnderTest.getJourneysSortedByDepartureDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(toJson(listResult.get(0))).isEqualTo(toJson(getJourneyWithNoEmptyFieldsBy(correctToken)));
    }

    @Test
    void test_getJourneysSortedByArrivalDateWithWith_one_correct_journey_and_one_journey_with_wrong_language_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(new Point(0.0d, 0.0d), new Point(0.0d, 0.0d), ZonedDateTime.now(), new Locale("de"));
        ApiToken wrongLanguageToken = new ApiToken.ApiTokenBuilder(correctToken).setLanguage(new Locale("en")).build();
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(SHA_ID_1, getJourneyWithNoEmptyFieldsBy(correctToken));
        hazelcastJourneys.put(SHA_ID_2, getJourneyWithNoEmptyFieldsBy(wrongLanguageToken));

        Stream<Journey> result = classUnderTest.getJourneysSortedByArrivalDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(toJson(listResult.get(0))).isEqualTo(toJson(getJourneyWithNoEmptyFieldsBy(correctToken)));
    }

    @Test
    void test_getJourneysSortedByDepartureDateWith_one_correct_journey_and_one_journey_with_wrong_arrivalPoint_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(new Point(0.0d, 0.0d), new Point(0.0d, 0.0d), ZonedDateTime.now(), new Locale("de"));
        ApiToken wrongArrivalPointToken = new ApiToken.ApiTokenBuilder(correctToken).setArrivalCoordinate(new Point(5.0d, 5.0d)).build();
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(SHA_ID_1, getJourneyWithNoEmptyFieldsBy(correctToken));
        hazelcastJourneys.put(SHA_ID_2, getJourneyWithNoEmptyFieldsBy(wrongArrivalPointToken));

        Stream<Journey> result = classUnderTest.getJourneysSortedByDepartureDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(toJson(listResult.get(0))).isEqualTo(toJson(getJourneyWithNoEmptyFieldsBy(correctToken)));
    }

    @Test
    void test_getJourneysSortedByArrivalDateWith_one_correct_journey_and_one_journey_with_wrong_arrivalPoint_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(new Point(0.0d, 0.0d), new Point(0.0d, 0.0d), ZonedDateTime.now(), new Locale("de"));
        ApiToken wrongArrivalPointToken = new ApiToken.ApiTokenBuilder(correctToken).setArrivalCoordinate(new Point(5.0d, 5.0d)).build();
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(SHA_ID_1, getJourneyWithNoEmptyFieldsBy(correctToken));
        hazelcastJourneys.put(SHA_ID_2, getJourneyWithNoEmptyFieldsBy(wrongArrivalPointToken));

        Stream<Journey> result = classUnderTest.getJourneysSortedByArrivalDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(toJson(listResult.get(0))).isEqualTo(toJson(getJourneyWithNoEmptyFieldsBy(correctToken)));
    }

    @Test
    void test_getJourneysSortedByDepartureDateWith_one_correct_journey_and_one_journey_with_wrong_departurePoint_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(new Point(0.0d, 0.0d), new Point(0.0d, 0.0d), ZonedDateTime.now(), new Locale("de"));
        ApiToken wrongDeparturePointToken = new ApiToken.ApiTokenBuilder(correctToken).setDepartureCoordinate(new Point(5.0d, 5.0d)).build();
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(SHA_ID_1, getJourneyWithNoEmptyFieldsBy(correctToken));
        hazelcastJourneys.put(SHA_ID_2, getJourneyWithNoEmptyFieldsBy(wrongDeparturePointToken));

        Stream<Journey> result = classUnderTest.getJourneysSortedByDepartureDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(toJson(listResult.get(0))).isEqualTo(toJson(getJourneyWithNoEmptyFieldsBy(correctToken)));
    }

    @Test
    void test_getJourneysSortedByArrivalDateWith_one_correct_journey_and_one_journey_with_wrong_departurePoint_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(new Point(0.0d, 0.0d), new Point(0.0d, 0.0d), ZonedDateTime.now(), new Locale("de"));
        ApiToken wrongDeparturePointToken = new ApiToken.ApiTokenBuilder(correctToken).setDepartureCoordinate(new Point(5.0d, 5.0d)).build();
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(SHA_ID_1, getJourneyWithNoEmptyFieldsBy(correctToken));
        hazelcastJourneys.put(SHA_ID_2, getJourneyWithNoEmptyFieldsBy(wrongDeparturePointToken));

        Stream<Journey> result = classUnderTest.getJourneysSortedByArrivalDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(toJson(listResult.get(0))).isEqualTo(toJson(getJourneyWithNoEmptyFieldsBy(correctToken)));
    }

    @Test
    void test_getJourneysSortedByDepartureDateWith_one_correct_journey_and_one_journey_with_wrong_departureTime_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(new Point(0.0d, 0.0d), new Point(0.0d, 0.0d), ZonedDateTime.now(), new Locale("de"));
        ApiToken wrongDepartureTimeToken = new ApiToken.ApiTokenBuilder(correctToken).setDateTime(ZonedDateTime.now().plusDays(1)).build();
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(SHA_ID_1, getJourneyWithNoEmptyFieldsBy(correctToken));
        hazelcastJourneys.put(SHA_ID_2, getJourneyWithNoEmptyFieldsBy(wrongDepartureTimeToken));

        Stream<Journey> result = classUnderTest.getJourneysSortedByDepartureDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(toJson(listResult.get(0))).isEqualTo(toJson(getJourneyWithNoEmptyFieldsBy(correctToken)));
    }

    @Test
    void test_getJourneysSortedByArrivalDateWith_one_correct_journey_and_one_journey_with_wrong_arrivalTime_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(new Point(0.0d, 0.0d), new Point(0.0d, 0.0d), ZonedDateTime.now(), new Locale("de"));
        ApiToken wrongArrivalTimeToken = new ApiToken.ApiTokenBuilder(correctToken).setDateTime(ZonedDateTime.now().minusDays(1)).build();
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(SHA_ID_1, getJourneyWithNoEmptyFieldsBy(correctToken));
        hazelcastJourneys.put(SHA_ID_2, getJourneyWithNoEmptyFieldsBy(wrongArrivalTimeToken));

        Stream<Journey> result = classUnderTest.getJourneysSortedByArrivalDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(toJson(listResult.get(0))).isEqualTo(toJson(getJourneyWithNoEmptyFieldsBy(correctToken)));
    }

    @Test
    void test_getJourneysSortedByDepartureDateWith_three_correct_journeys_returns_three_journeys_sorted_by_departureDate() {
        ApiToken firstCorrectToken = getApiTokenWithNoEmptyFieldsBy(
                new Point(0.0d, 0.0d),
                new Point(0.0d, 0.0d),
                ZonedDateTime.parse("2020-12-28T12:00:00+02:00[Europe/Berlin]"),
                new Locale("de")
        );
        ApiToken secondCorrectToken = new ApiToken.ApiTokenBuilder(firstCorrectToken).setDateTime(ZonedDateTime.parse("2020-12-28T13:00:00+02:00[Europe/Berlin]")).build();
        ApiToken thirdCorrectToken = new ApiToken.ApiTokenBuilder(firstCorrectToken).setDateTime(ZonedDateTime.parse("2020-12-28T14:00:00+02:00[Europe/Berlin]")).build();
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(SHA_ID_3, getJourneyWithNoEmptyFieldsBy(thirdCorrectToken));
        hazelcastJourneys.put(SHA_ID_2, getJourneyWithNoEmptyFieldsBy(secondCorrectToken));
        hazelcastJourneys.put(SHA_ID_1, getJourneyWithNoEmptyFieldsBy(firstCorrectToken));

        Stream<Journey> result = classUnderTest.getJourneysSortedByDepartureDateWith(firstCorrectToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(3);
        assertThat(toJson(listResult.get(0))).isEqualTo(toJson(getJourneyWithNoEmptyFieldsBy(firstCorrectToken)));
        assertThat(toJson(listResult.get(1))).isEqualTo(toJson(getJourneyWithNoEmptyFieldsBy(secondCorrectToken)));
        assertThat(toJson(listResult.get(2))).isEqualTo(toJson(getJourneyWithNoEmptyFieldsBy(thirdCorrectToken)));
    }

    @Test
    void test_getJourneysSortedByArrivalDateWith_three_correct_journeys_returns_three_journeys_sorted_by_arrivalDate() {
        ApiToken firstCorrectToken = getApiTokenWithNoEmptyFieldsBy(
                new Point(0.0d, 0.0d),
                new Point(0.0d, 0.0d),
                ZonedDateTime.parse("2020-12-28T12:00:00+02:00[Europe/Berlin]"),
                new Locale("de")
        );
        ApiToken secondCorrectToken = new ApiToken.ApiTokenBuilder(firstCorrectToken).setDateTime(ZonedDateTime.parse("2020-12-28T11:00:00+02:00[Europe/Berlin]")).build();
        ApiToken thirdCorrectToken = new ApiToken.ApiTokenBuilder(firstCorrectToken).setDateTime(ZonedDateTime.parse("2020-12-28T10:00:00+02:00[Europe/Berlin]")).build();
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(SHA_ID_3, getJourneyWithNoEmptyFieldsBy(thirdCorrectToken));
        hazelcastJourneys.put(SHA_ID_2, getJourneyWithNoEmptyFieldsBy(secondCorrectToken));
        hazelcastJourneys.put(SHA_ID_1, getJourneyWithNoEmptyFieldsBy(firstCorrectToken));

        Stream<Journey> result = classUnderTest.getJourneysSortedByArrivalDateWith(firstCorrectToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(3);
        assertThat(toJson(listResult.get(0))).isEqualTo(toJson(getJourneyWithNoEmptyFieldsBy(firstCorrectToken)));
        assertThat(toJson(listResult.get(1))).isEqualTo(toJson(getJourneyWithNoEmptyFieldsBy(secondCorrectToken)));
        assertThat(toJson(listResult.get(2))).isEqualTo(toJson(getJourneyWithNoEmptyFieldsBy(thirdCorrectToken)));
    }
}
