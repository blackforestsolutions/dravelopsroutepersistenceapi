package de.blackforestsolutions.dravelopsroutepersistenceapi;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyCreateRepositoryService;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getJourneyWithNoEmptyFieldsByArrivalTime;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.UUIDObjectMother.TEST_UUID_1;
import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.HazelcastConfiguration.HAZELCAST_INSTANCE;
import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.HazelcastConfiguration.JOURNEY_MAP;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JourneyCreateRepositoryServiceIT {

    @Autowired
    @Qualifier(HAZELCAST_INSTANCE)
    private HazelcastInstance hazelcastInstance;

    @Autowired
    private JourneyCreateRepositoryService classUnderTest;

    @Test
    void test_writeJourneyToMapWith_testJourney_lives_one_second_in_hazelcast_server() throws IOException {
        IMap<String, Journey> hazelcastJourneys = hazelcastInstance.getMap(JOURNEY_MAP);
        Journey testData = getJourneyWithNoEmptyFieldsByArrivalTime(Instant.ofEpochMilli(Long.MIN_VALUE).atZone(ZoneOffset.UTC), TEST_UUID_1);

        Journey result = classUnderTest.writeJourneyToMapWith(testData);

        assertThat(result).isNull();
        assertThat(hazelcastJourneys.size()).isEqualTo(1);
        Awaitility.await()
                .untilAsserted(() -> assertThat(hazelcastJourneys.size()).isEqualTo(0));
    }

    @AfterEach
    void tearDown() {
        hazelcastInstance.getMap(JOURNEY_MAP).clear();
    }
}
