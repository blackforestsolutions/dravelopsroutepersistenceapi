package de.blackforestsolutions.dravelopsroutepersistenceapi;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Point;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyReadRepositoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getApiTokenWithNoEmptyFields;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getApiTokenWithNoEmptyFieldsBy;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getJourneyWithNoEmptyFieldsBy;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.UUIDObjectMother.TEST_UUID_1;
import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.HazelcastConfiguration.HAZELCAST_INSTANCE;
import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.HazelcastConfiguration.JOURNEY_MAP;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JourneyReadRepositoryIT {

    @Value("${hazelcast.journeySearchWindowInMinutes}")
    private int timeRangeInMinutes;

    @Autowired
    @Qualifier(HAZELCAST_INSTANCE)
    private HazelcastInstance hazelcastInstance;

    @Autowired
    private JourneyReadRepositoryService classUnderTest;

    @Test
    void test_getJourneysSortedByDepartureDateWith_correct_apiToken_returns_one_correct_journey() {
        ApiToken correctTestToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(10.0d, 10.0d).build(),
                new Point.PointBuilder(1.0d, 1.0d).build(),
                ZonedDateTime.now().plusMinutes(timeRangeInMinutes),
                new Locale("de")
        );
        Journey correctJourney = getJourneyWithNoEmptyFieldsBy(correctTestToken, TEST_UUID_1);
        IMap<UUID, Journey> hazelcastJourneys = hazelcastInstance.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(correctJourney.getId(), correctJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByDepartureDateWith(correctTestToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(correctTestToken, TEST_UUID_1));
    }

    @Test
    void test_getJourneysSortedByDepartureDateWith_apiToken_and_empty_map_returns_zero_journeys() {
        ApiToken correctTestToken = getApiTokenWithNoEmptyFields();

        Stream<Journey> result = classUnderTest.getJourneysSortedByDepartureDateWith(correctTestToken);

        assertThat(result.count()).isEqualTo(0);
    }

    @Test
    void test_getJourneysSortedByArrivalDateWith_correct_apiToken_returns_one_correct_journey() {
        ApiToken correctTestToken = getApiTokenWithNoEmptyFieldsBy(
                new Point.PointBuilder(10.0d, 10.0d).build(),
                new Point.PointBuilder(1.0d, 1.0d).build(),
                ZonedDateTime.now().minusMinutes(timeRangeInMinutes),
                new Locale("de")
        );
        IMap<UUID, Journey> hazelcastJourneys = hazelcastInstance.getMap(JOURNEY_MAP);
        Journey correctJourney = getJourneyWithNoEmptyFieldsBy(correctTestToken, TEST_UUID_1);
        hazelcastJourneys.put(correctJourney.getId(), correctJourney);

        Stream<Journey> result = classUnderTest.getJourneysSortedByArrivalDateWith(correctTestToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(listResult.get(0)).isEqualToComparingFieldByFieldRecursively(getJourneyWithNoEmptyFieldsBy(correctTestToken, TEST_UUID_1));
    }

    @Test
    void test_getJourneysSortedByArrivalDateWith_apiToken_and_empty_map_returns_zero_journeys() {
        ApiToken correctTestToken = getApiTokenWithNoEmptyFields();

        Stream<Journey> result = classUnderTest.getJourneysSortedByArrivalDateWith(correctTestToken);

        assertThat(result.count()).isEqualTo(0);
    }

    @AfterEach
    void tearDown() {
        hazelcastInstance.getMap(JOURNEY_MAP).clear();
    }
}
