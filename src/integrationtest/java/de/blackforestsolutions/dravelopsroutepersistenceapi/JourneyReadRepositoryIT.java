package de.blackforestsolutions.dravelopsroutepersistenceapi;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyReadRepositoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getApiTokenWithNoEmptyFields;
import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.toJson;
import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.HazelcastConfiguration.HAZELCAST_INSTANCE;
import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.HazelcastConfiguration.JOURNEY_MAP;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.ApiTokenObjectMother.getApiTokenWithNoEmptyFieldsBy;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.JourneyObjectMother.getJourneyWithNoEmptyFieldsBy;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.ShaIdObjectMother.SHA_ID_1;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JourneyReadRepositoryIT {

    @Value("${hazelcast.timeRangeInMinutes}")
    private int timeRangeInMinutes;

    @Autowired
    @Qualifier(HAZELCAST_INSTANCE)
    private HazelcastInstance hazelcastInstance;

    @Autowired
    private JourneyReadRepositoryService classUnderTest;

    @Test
    void test_getJourneysSortedByDepartureDateWith_correct_apiToken_returns_one_correct_journey() {
        ApiToken correctTestToken = getApiTokenWithNoEmptyFieldsBy(
                new Point(0.0d, 0.0d),
                new Point(0.0d, 0.0d),
                ZonedDateTime.now().plusMinutes(timeRangeInMinutes),
                new Locale("de")
        );
        IMap<String, Journey> hazelcastJourneys = hazelcastInstance.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(SHA_ID_1, getJourneyWithNoEmptyFieldsBy(correctTestToken));

        Stream<Journey> result = classUnderTest.getJourneysSortedByDepartureDateWith(correctTestToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(toJson(listResult.get(0))).isEqualTo(toJson(getJourneyWithNoEmptyFieldsBy(correctTestToken)));
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
                new Point(0.0d, 0.0d),
                new Point(0.0d, 0.0d),
                ZonedDateTime.now().minusMinutes(timeRangeInMinutes),
                new Locale("de")
        );
        IMap<String, Journey> hazelcastJourneys = hazelcastInstance.getMap(JOURNEY_MAP);
        hazelcastJourneys.put(SHA_ID_1, getJourneyWithNoEmptyFieldsBy(correctTestToken));

        Stream<Journey> result = classUnderTest.getJourneysSortedByArrivalDateWith(correctTestToken);
        List<Journey> listResult = result.collect(Collectors.toList());

        assertThat(listResult.size()).isEqualTo(1);
        assertThat(toJson(listResult.get(0))).isEqualTo(toJson(getJourneyWithNoEmptyFieldsBy(correctTestToken)));
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
