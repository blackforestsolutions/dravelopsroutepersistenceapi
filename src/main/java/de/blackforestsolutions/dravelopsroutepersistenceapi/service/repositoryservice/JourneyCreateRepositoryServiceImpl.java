package de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.HazelcastConfiguration.*;

@Service
public class JourneyCreateRepositoryServiceImpl implements JourneyCreateRepositoryService {

    private static final int UNALLOWED_TTL_HAZELCAST = 0;

    private final HazelcastInstance hazelcastInstance;
    private final ApiToken hazelcastApiToken;

    @Autowired
    public JourneyCreateRepositoryServiceImpl(@Qualifier(HAZELCAST_INSTANCE) HazelcastInstance hazelcastInstance, ApiToken hazelcastApiToken) {
        this.hazelcastInstance = hazelcastInstance;
        this.hazelcastApiToken = hazelcastApiToken;
    }

    @Override
    public Journey writeJourneyToMapWith(Journey journey) {
        IMap<String, Journey> hazelcastJourneys = hazelcastInstance.getMap(JOURNEY_MAP);
        long ttl = calculateTimeToLiveInSecondsWith(journey);
        return hazelcastJourneys.putIfAbsent(journey.getId(), journey, ttl, TimeUnit.SECONDS);
    }

    private long calculateTimeToLiveInSecondsWith(Journey journey) {
        long ttl = Duration.between(ZonedDateTime.now(), journey.getLegs().getLast().getArrival().getArrivalTime())
                .plusDays(hazelcastApiToken.getMaxPastDaysInCalendar())
                .getSeconds();
        if (ttl <= UNALLOWED_TTL_HAZELCAST) {
            return JOURNEY_MAP_TTL_STANDARD;
        }
        return ttl;
    }

}
