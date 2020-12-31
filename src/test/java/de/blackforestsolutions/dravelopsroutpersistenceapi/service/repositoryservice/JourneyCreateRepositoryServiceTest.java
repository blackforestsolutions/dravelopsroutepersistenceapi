package de.blackforestsolutions.dravelopsroutpersistenceapi.service.repositoryservice;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.test.TestHazelcastInstanceFactory;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyCreateRepositoryService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyCreateRepositoryServiceImpl;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.ShaIdService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.ShaIdServiceImpl;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.time.ZonedDateTime;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getHazelcastApiToken;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getFurtwangenToWaldkirchJourney;
import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.toJson;
import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.HazelcastConfiguration.JOURNEY_MAP;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.JourneyObjectMother.getJourneyWithNoEmptyFieldsByArrivalTime;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.ShaIdObjectMother.SHA_ID_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class JourneyCreateRepositoryServiceTest {

    private HazelcastInstance hazelcastMock;
    private final ShaIdService shaIdService = mock(ShaIdServiceImpl.class);
    private final ApiToken hazelcastApiToken = getHazelcastApiToken();

    private JourneyCreateRepositoryService classUnderTest;

    @BeforeEach
    void init() throws IOException {
        hazelcastMock = new TestHazelcastInstanceFactory(1).newHazelcastInstance();
        classUnderTest = new JourneyCreateRepositoryServiceImpl(hazelcastMock, shaIdService, hazelcastApiToken);

        when(shaIdService.generateShaIdWith(any(Journey.class))).thenReturn(SHA_ID_1);
    }

    @AfterEach
    void tearDown() {
        Hazelcast.shutdownAll();
    }

    @Test
    void test_writeJourneyToMapWith_testJourney_returns_journey_when_ttl_is_positive_one() throws IOException {
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalTime(ZonedDateTime.now().plusSeconds(5L));

        classUnderTest.writeJourneyToMapWith(testData);

        Awaitility.await()
                .untilAsserted(() -> {
                    IMap<String, Journey> testM = hazelcastMock.getMap(JOURNEY_MAP);
                    assertThat(toJson(testData)).isEqualTo(toJson(testM.get(SHA_ID_1)));
                });
    }

    @Test
    void test_writeJourneyToMapWith_testJourney_is_executed_correctly() throws IOException {
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalTime(ZonedDateTime.now().plusSeconds(5L));
        ArgumentCaptor<Journey> journeyArg = ArgumentCaptor.forClass(Journey.class);

        classUnderTest.writeJourneyToMapWith(testData);

        verify(shaIdService, times(1)).generateShaIdWith(journeyArg.capture());
        assertThat(toJson(journeyArg.getValue())).isEqualTo(toJson(testData));
    }

    @Test
    void test_writeJourneyToMapWith_testJourney_returns_no_journey_when_ttl_is_zero() throws IOException {
        // plusSeconds 1L because there is a delay from one second
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalTime(ZonedDateTime.now().minusDays(2L).plusSeconds(1L));

        classUnderTest.writeJourneyToMapWith(testData);

        Awaitility.await()
                .untilAsserted(() -> {
                    IMap<String, Journey> testM = hazelcastMock.getMap(JOURNEY_MAP);
                    assertThat(toJson(testData)).isNotEqualTo(toJson(testM.get(SHA_ID_1)));
                });
    }

    @Test
    void test_writeJourneyToMapWith__testJourney_returns_no_journey_when_ttl_is_minus_one() throws IOException {
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalTime(ZonedDateTime.now().minusDays(2L).minusSeconds(1L));

        classUnderTest.writeJourneyToMapWith(testData);

        Awaitility.await()
                .untilAsserted(() -> {
                    IMap<String, Journey> testM = hazelcastMock.getMap(JOURNEY_MAP);
                    assertThat(toJson(testData)).isNotEqualTo(toJson(testM.get(SHA_ID_1)));
                });
    }

    @Test
    void test_writeJourneyToMapWith_testJourney_throws_an_exception_when_shaId_could_not_be_generated() throws IOException {
        Journey testData = getFurtwangenToWaldkirchJourney();
        doThrow(IOException.class).when(shaIdService).generateShaIdWith(any(Journey.class));

        assertThrows(IOException.class, () -> classUnderTest.writeJourneyToMapWith(testData));
    }
}
