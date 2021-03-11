package de.blackforestsolutions.dravelopsroutpersistenceapi.service.repositoryservice;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.test.TestHazelcastInstanceFactory;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.objectmothers.UUIDObjectMother;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyCreateRepositoryService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyCreateRepositoryServiceImpl;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.ZonedDateTime;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getHazelcastApiToken;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getJourneyWithNoEmptyFieldsByArrivalTime;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.UUIDObjectMother.TEST_UUID_1;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.UUIDObjectMother.TEST_UUID_4;
import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.HazelcastConfiguration.JOURNEY_MAP;
import static org.assertj.core.api.Assertions.assertThat;

class JourneyCreateRepositoryServiceTest {

    private HazelcastInstance hazelcastMock;
    private final ApiToken hazelcastApiToken = getHazelcastApiToken();

    private JourneyCreateRepositoryService classUnderTest;

    @BeforeEach
    void init() {
        hazelcastMock = new TestHazelcastInstanceFactory(1).newHazelcastInstance();
        classUnderTest = new JourneyCreateRepositoryServiceImpl(hazelcastMock, hazelcastApiToken);
    }

    @AfterEach
    void tearDown() {
        Hazelcast.shutdownAll();
    }

    @Test
    void test_writeJourneyToMapWith_testJourney_returns_journey_when_ttl_is_positive() throws IOException {
        IMap<String, Journey> testMap = hazelcastMock.getMap(JOURNEY_MAP);
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalTime(ZonedDateTime.now().plusSeconds(5L), TEST_UUID_1);

        classUnderTest.writeJourneyToMapWith(testData);

        assertThat(testMap.values().stream().findFirst().get()).isEqualToComparingFieldByFieldRecursively(testData);
    }

    @Test
    void test_writeJourneyToMapWith_testJourney_returns_no_journey_when_ttl_is_zero() throws IOException {
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalTime(ZonedDateTime.now().minusDays(2L).minusSeconds(1L), TEST_UUID_1);

        classUnderTest.writeJourneyToMapWith(testData);

        Awaitility.await()
                .untilAsserted(() -> {
                    IMap<String, Journey> testMap = hazelcastMock.getMap(JOURNEY_MAP);
                    assertThat(testMap.size()).isEqualTo(0);
                });
    }

    @Test
    void test_writeJourneyToMapWith_testJourneys_inserts_only_one_journey_when_journeys_are_equal() throws IOException {
        IMap<String, Journey> testMap = hazelcastMock.getMap(JOURNEY_MAP);
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalTime(ZonedDateTime.now().plusSeconds(5), TEST_UUID_1);

        classUnderTest.writeJourneyToMapWith(testData);
        classUnderTest.writeJourneyToMapWith(testData);

        assertThat(testMap.size()).isEqualTo(1);
    }
}
