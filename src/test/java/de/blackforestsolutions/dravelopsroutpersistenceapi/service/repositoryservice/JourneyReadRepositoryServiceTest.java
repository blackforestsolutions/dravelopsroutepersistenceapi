package de.blackforestsolutions.dravelopsroutpersistenceapi.service.repositoryservice;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.test.TestHazelcastInstanceFactory;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Point;
import de.blackforestsolutions.dravelopsdatamodel.hazelcast.DravelOpsPortableFactory;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyReadRepositoryService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyReadRepositoryServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.blackforestsolutions.dravelopsdatamodel.hazelcast.DravelOpsPortableFactory.DRAVEL_OPS_FACTORY_ID;
import static de.blackforestsolutions.dravelopsdatamodel.hazelcast.classdefinition.JourneyClassDefinition.buildJourneyClassDefinition;
import static de.blackforestsolutions.dravelopsdatamodel.hazelcast.classdefinition.LegClassDefinition.buildLegClassDefinition;
import static de.blackforestsolutions.dravelopsdatamodel.hazelcast.classdefinition.PointClassDefinition.buildPointClassDefinition;
import static de.blackforestsolutions.dravelopsdatamodel.hazelcast.classdefinition.PriceClassDefinition.buildPriceClassDefinition;
import static de.blackforestsolutions.dravelopsdatamodel.hazelcast.classdefinition.TravelPointClassDefinition.buildTravelPointClassDefinition;
import static de.blackforestsolutions.dravelopsdatamodel.hazelcast.classdefinition.TravelProviderClassDefinition.buildTravelProviderClassDefinition;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getApiTokenWithNoEmptyFieldsBy;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getHazelcastApiToken;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getJourneyWithNoEmptyFieldsBy;
import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.HazelcastConfiguration.JOURNEY_MAP;
import static org.assertj.core.api.Assertions.assertThat;

class JourneyReadRepositoryServiceTest {

    private HazelcastInstance hazelcastMock;
    private final ApiToken hazelcastApiToken = getHazelcastApiToken();

    private JourneyReadRepositoryService classUnderTest;

    @BeforeEach
    void init() {
        Config config = new Config();
        config.getSerializationConfig().addPortableFactory(DRAVEL_OPS_FACTORY_ID, new DravelOpsPortableFactory());
        config.getSerializationConfig().addClassDefinition(buildJourneyClassDefinition());
        config.getSerializationConfig().addClassDefinition(buildPriceClassDefinition());
        config.getSerializationConfig().addClassDefinition(buildLegClassDefinition());
        config.getSerializationConfig().addClassDefinition(buildTravelPointClassDefinition());
        config.getSerializationConfig().addClassDefinition(buildPointClassDefinition());
        config.getSerializationConfig().addClassDefinition(buildTravelProviderClassDefinition());

        hazelcastMock = new TestHazelcastInstanceFactory(1).newHazelcastInstance(config);

        classUnderTest = new JourneyReadRepositoryServiceImpl(hazelcastMock, hazelcastApiToken);
    }

    @AfterEach
    void tearDown() {
        Hazelcast.shutdownAll();
    }

    @Test
    void test_getJourneysSortedByDepartureDateWith_one_correct_journey_and_one_journey_with_wrong_language_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.now(),
                new Locale("de")
        );
        ApiToken wrongLanguageToken = new ApiToken.ApiTokenBuilder(correctToken).setLanguage(new Locale("en")).build();
        Journey correctJourney = getJourneyWithNoEmptyFieldsBy(correctToken);
        Journey wrongJourney = getJourneyWithNoEmptyFieldsBy(wrongLanguageToken);
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(correctJourney.getId(), correctJourney);
        hazelcastJourneys.put(wrongJourney.getId(), wrongJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByDepartureDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(correctToken));
    }

    @Test
    void test_getJourneysSortedByArrivalDateWithWith_one_correct_journey_and_one_journey_with_wrong_language_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.now(),
                new Locale("de")
        );
        ApiToken wrongLanguageToken = new ApiToken.ApiTokenBuilder(correctToken).setLanguage(new Locale("en")).build();
        Journey correctJourney = getJourneyWithNoEmptyFieldsBy(correctToken);
        Journey wrongJourney = getJourneyWithNoEmptyFieldsBy(wrongLanguageToken);
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(correctJourney.getId(), correctJourney);
        hazelcastJourneys.put(wrongJourney.getId(), wrongJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByArrivalDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(correctToken));
    }

    @Test
    void test_getJourneysSortedByDepartureDateWith_one_correct_journey_and_one_journey_with_wrong_arrivalPoint_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.now(),
                new Locale("de")
        );
        ApiToken wrongArrivalPointToken = new ApiToken.ApiTokenBuilder(correctToken)
                .setArrivalCoordinate(new Point.PointBuilder(5.0d, 5.0d).build())
                .build();
        Journey correctJourney = getJourneyWithNoEmptyFieldsBy(correctToken);
        Journey wrongJourney = getJourneyWithNoEmptyFieldsBy(wrongArrivalPointToken);
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(correctJourney.getId(), correctJourney);
        hazelcastJourneys.put(wrongJourney.getId(), wrongJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByDepartureDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(correctToken));
    }

    @Test
    void test_getJourneysSortedByArrivalDateWith_one_correct_journey_and_one_journey_with_wrong_arrivalPoint_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.now(),
                new Locale("de")
        );
        ApiToken wrongArrivalPointToken = new ApiToken.ApiTokenBuilder(correctToken)
                .setArrivalCoordinate(new Point.PointBuilder(5.0d, 5.0d).build())
                .build();
        Journey correctJourney = getJourneyWithNoEmptyFieldsBy(correctToken);
        Journey wrongJourney = getJourneyWithNoEmptyFieldsBy(wrongArrivalPointToken);
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(correctJourney.getId(), correctJourney);
        hazelcastJourneys.put(wrongJourney.getId(), wrongJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByArrivalDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(correctToken));
    }

    @Test
    void test_getJourneysSortedByDepartureDateWith_one_correct_journey_and_one_journey_with_wrong_departurePoint_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.now(),
                new Locale("de")
        );
        ApiToken wrongDeparturePointToken = new ApiToken.ApiTokenBuilder(correctToken)
                .setDepartureCoordinate(new Point.PointBuilder(5.0d, 5.0d).build())
                .build();
        Journey correctJourney = getJourneyWithNoEmptyFieldsBy(correctToken);
        Journey wrongJourney = getJourneyWithNoEmptyFieldsBy(wrongDeparturePointToken);
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(correctJourney.getId(), correctJourney);
        hazelcastJourneys.put(wrongJourney.getId(), wrongJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByDepartureDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(correctToken));
    }

    @Test
    void test_getJourneysSortedByArrivalDateWith_one_correct_journey_and_one_journey_with_wrong_departurePoint_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.now(),
                new Locale("de")
        );
        ApiToken wrongDeparturePointToken = new ApiToken.ApiTokenBuilder(correctToken)
                .setDepartureCoordinate(new Point.PointBuilder(5.0d, 5.0d).build())
                .build();
        Journey correctJourney = getJourneyWithNoEmptyFieldsBy(correctToken);
        Journey wrongJourney = getJourneyWithNoEmptyFieldsBy(wrongDeparturePointToken);
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(correctJourney.getId(), correctJourney);
        hazelcastJourneys.put(wrongJourney.getId(), wrongJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByArrivalDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(correctToken));
    }

    @Test
    void test_getJourneysSortedByDepartureDateWith_one_correct_journey_and_one_journey_with_wrong_departureTime_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.now(),
                new Locale("de")
        );
        ApiToken wrongDepartureTimeToken = new ApiToken.ApiTokenBuilder(correctToken)
                .setDateTime(ZonedDateTime.now().plusDays(1))
                .build();
        Journey correctJourney = getJourneyWithNoEmptyFieldsBy(correctToken);
        Journey wrongJourney = getJourneyWithNoEmptyFieldsBy(wrongDepartureTimeToken);
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(correctJourney.getId(), correctJourney);
        hazelcastJourneys.put(wrongJourney.getId(), wrongJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByDepartureDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(correctToken));
    }

    @Test
    void test_getJourneysSortedByArrivalDateWith_one_correct_journey_and_one_journey_with_wrong_arrivalTime_returns_one_journey() {
        ApiToken correctToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.now(),
                new Locale("de")
        );
        ApiToken wrongArrivalTimeToken = new ApiToken.ApiTokenBuilder(correctToken).setDateTime(ZonedDateTime.now().minusDays(1)).build();
        Journey correctJourney = getJourneyWithNoEmptyFieldsBy(correctToken);
        Journey wrongJourney = getJourneyWithNoEmptyFieldsBy(wrongArrivalTimeToken);
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(correctJourney.getId(), correctJourney);
        hazelcastJourneys.put(wrongJourney.getId(), wrongJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByArrivalDateWith(correctToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(correctToken));
    }

    @Test
    void test_getJourneysSortedByDepartureDateWith_three_correct_journeys_returns_three_journeys_sorted_by_departureDate() {
        ApiToken firstCorrectToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.parse("2020-12-28T12:00:00+02:00[Europe/Berlin]"),
                new Locale("de")
        );
        ApiToken secondCorrectToken = new ApiToken.ApiTokenBuilder(firstCorrectToken)
                .setDateTime(ZonedDateTime.parse("2020-12-28T13:00:00+02:00[Europe/Berlin]"))
                .build();
        ApiToken thirdCorrectToken = new ApiToken.ApiTokenBuilder(firstCorrectToken)
                .setDateTime(ZonedDateTime.parse("2020-12-28T14:00:00+02:00[Europe/Berlin]"))
                .build();
        Journey thirdCorrectJourney = getJourneyWithNoEmptyFieldsBy(thirdCorrectToken);
        Journey secondCorrectJourney = getJourneyWithNoEmptyFieldsBy(secondCorrectToken);
        Journey firstCorrectJourney = getJourneyWithNoEmptyFieldsBy(firstCorrectToken);
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(thirdCorrectJourney.getId(), thirdCorrectJourney);
        hazelcastJourneys.put(secondCorrectJourney.getId(), secondCorrectJourney);
        hazelcastJourneys.put(firstCorrectJourney.getId(), firstCorrectJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByDepartureDateWith(firstCorrectToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(3);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(firstCorrectToken));
        assertThat(listResult.get(1)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(secondCorrectToken));
        assertThat(listResult.get(2)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(thirdCorrectToken));
    }

    @Test
    void test_getJourneysSortedByArrivalDateWith_three_correct_journeys_returns_three_journeys_sorted_by_arrivalDate() {
        ApiToken firstCorrectToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(0.0d, 0.0d).build(),
                new Point.PointBuilder(0.0d, 0.0d).build(),
                ZonedDateTime.parse("2020-12-28T12:00:00+02:00[Europe/Berlin]"),
                new Locale("de")
        );
        ApiToken secondCorrectToken = new ApiToken.ApiTokenBuilder(firstCorrectToken)
                .setDateTime(ZonedDateTime.parse("2020-12-28T11:00:00+02:00[Europe/Berlin]"))
                .build();
        ApiToken thirdCorrectToken = new ApiToken.ApiTokenBuilder(firstCorrectToken)
                .setDateTime(ZonedDateTime.parse("2020-12-28T10:00:00+02:00[Europe/Berlin]"))
                .build();
        Journey thirdCorrectJourney = getJourneyWithNoEmptyFieldsBy(thirdCorrectToken);
        Journey secondCorrectJourney = getJourneyWithNoEmptyFieldsBy(secondCorrectToken);
        Journey firstCorrectJourney = getJourneyWithNoEmptyFieldsBy(firstCorrectToken);
        IMap<String, Journey> hazelcastJourneys = hazelcastMock.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(thirdCorrectJourney.getId(), thirdCorrectJourney);
        hazelcastJourneys.put(secondCorrectJourney.getId(), secondCorrectJourney);
        hazelcastJourneys.put(firstCorrectJourney.getId(), firstCorrectJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByArrivalDateWith(firstCorrectToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(3);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(firstCorrectToken));
        assertThat(listResult.get(1)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(secondCorrectToken));
        assertThat(listResult.get(2)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(thirdCorrectToken));
    }
}
