package de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates.ArrivalPointPredicate;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates.ArrivalTimePredicate;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates.DeparturePointPredicate;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates.DepartureTimePredicate;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.ShaIdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.HazelcastConfiguration.HAZELCAST_INSTANCE;
import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.HazelcastConfiguration.JOURNEY_MAP;

@Slf4j
@Service
public class JourneyRepositoryServiceImpl implements JourneyRepositoryService {

    private final HazelcastInstance hazelcastInstance;
    private final ApiToken hazelcastApiToken;
    private final ShaIdService shaIdService;

    @Autowired
    public JourneyRepositoryServiceImpl(@Qualifier(HAZELCAST_INSTANCE) HazelcastInstance hazelcastInstance, ShaIdService shaIdService, ApiToken hazelcastApiToken) {
        this.hazelcastInstance = hazelcastInstance;
        this.shaIdService = shaIdService;
        this.hazelcastApiToken = hazelcastApiToken;
    }

    @Override
    public Stream<Journey> getJourneysSortedByDepartureDateWith(ApiToken apiToken) {
        IMap<String, Journey> hazelcastJourneys = retrieveMapBy(apiToken.getLanguage());
        return hazelcastJourneys.values(Predicates.and(buildBaseJourneyQueryWith(apiToken), new DepartureTimePredicate(apiToken.getDateTime(), hazelcastApiToken.getHazelcastTimeRangeInMinutes())))
                .stream()
                .sorted(Comparator.comparing(journey -> journey.getLegs().getFirst().getDeparture().getDepartureTime()));
    }

    @Override
    public Stream<Journey> getJourneysSortedByArrivalDateWith(ApiToken apiToken) {
        IMap<String, Journey> hazelcastJourneys = retrieveMapBy(apiToken.getLanguage());
        return hazelcastJourneys.values(Predicates.and(buildBaseJourneyQueryWith(apiToken), new ArrivalTimePredicate(apiToken.getDateTime(), hazelcastApiToken.getHazelcastTimeRangeInMinutes())))
                .stream()
                .sorted(Comparator.comparing(journey -> journey.getLegs().getLast().getArrival().getArrivalTime(), Comparator.reverseOrder()));
    }

    @Override
    public Journey writeJourneyToMapWith(ApiToken apiToken, Journey journey) throws IOException {
        String key = shaIdService.generateShaIdWith(journey);
        IMap<String, Journey> hazelcastJourneys = retrieveMapBy(apiToken.getLanguage());
        return hazelcastJourneys.putIfAbsent(
                key,
                journey,
                calculateTimeToLiveInSecondsWith(journey),
                TimeUnit.SECONDS
        );
    }

    private Predicate<String, Journey> buildBaseJourneyQueryWith(ApiToken apiToken) {
        Predicate<String, Journey> departurePointCondition = new DeparturePointPredicate(apiToken.getDepartureCoordinate());
        Predicate<String, Journey> arrivalPointCondition = new ArrivalPointPredicate(apiToken.getArrivalCoordinate());

        return Predicates.and(departurePointCondition, arrivalPointCondition);
    }

    private long calculateTimeToLiveInSecondsWith(Journey journey) {
        return Duration.between(journey.getLegs().getLast().getArrival().getArrivalTime(), ZonedDateTime.now())
                .plusDays(hazelcastApiToken.getMaxPastDaysInCalendar())
                .getSeconds();
    }

    private IMap<String, Journey> retrieveMapBy(Locale language) {
        return hazelcastInstance.getMap(language.getISO3Language().concat("-").concat(JOURNEY_MAP));
    }

}
