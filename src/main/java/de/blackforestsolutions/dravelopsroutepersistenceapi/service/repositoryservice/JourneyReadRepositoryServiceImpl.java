package de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.HazelcastConfiguration.HAZELCAST_INSTANCE;
import static de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.HazelcastConfiguration.JOURNEY_MAP;

@Service
public class JourneyReadRepositoryServiceImpl implements JourneyReadRepositoryService {

    private final HazelcastInstance hazelcastInstance;
    private final ApiToken hazelcastApiToken;

    @Autowired
    public JourneyReadRepositoryServiceImpl(@Qualifier(HAZELCAST_INSTANCE) HazelcastInstance hazelcastInstance, ApiToken hazelcastApiToken) {
        this.hazelcastInstance = hazelcastInstance;
        this.hazelcastApiToken = hazelcastApiToken;
    }

    @Override
    public Journey getJourneyById(UUID id) {
        IMap<UUID, Journey> hazelcastJourneys = hazelcastInstance.getMap(JOURNEY_MAP);
        return hazelcastJourneys.get(id);
    }

    @Override
    public Stream<Journey> getJourneysSortedByDepartureDateWith(ApiToken apiToken) {
        IMap<UUID, Journey> hazelcastJourneys = hazelcastInstance.getMap(JOURNEY_MAP);
        return hazelcastJourneys.values(Predicates.and(buildBaseJourneyQueryWith(apiToken), new DepartureTimePredicate(apiToken.getDateTime(), hazelcastApiToken.getJourneySearchWindowInMinutes())))
                .stream()
                .sorted(Comparator.comparing(journey -> journey.getLegs().getFirst().getDeparture().getDepartureTime()));
    }

    @Override
    public Stream<Journey> getJourneysSortedByArrivalDateWith(ApiToken apiToken) {
        IMap<UUID, Journey> hazelcastJourneys = hazelcastInstance.getMap(JOURNEY_MAP);
        return hazelcastJourneys.values(Predicates.and(buildBaseJourneyQueryWith(apiToken), new ArrivalTimePredicate(apiToken.getDateTime(), hazelcastApiToken.getJourneySearchWindowInMinutes())))
                .stream()
                .sorted(Comparator.comparing(journey -> journey.getLegs().getLast().getArrival().getArrivalTime(), Comparator.reverseOrder()));
    }

    private Predicate<String, Journey> buildBaseJourneyQueryWith(ApiToken apiToken) {
        Predicate<UUID, Journey> departurePointCondition = new DeparturePointPredicate(apiToken.getDepartureCoordinate());
        Predicate<UUID, Journey> arrivalPointCondition = new ArrivalPointPredicate(apiToken.getArrivalCoordinate());
        Predicate<UUID, Journey> languageCondition = new LanguagePredicate(apiToken.getLanguage());

        return Predicates.and(departurePointCondition, arrivalPointCondition, languageCondition);
    }
}
