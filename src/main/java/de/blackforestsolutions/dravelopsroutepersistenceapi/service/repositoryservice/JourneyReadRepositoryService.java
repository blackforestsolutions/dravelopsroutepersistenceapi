package de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;

import java.util.stream.Stream;

public interface JourneyReadRepositoryService {
    Stream<Journey> getJourneysSortedByDepartureDateWith(ApiToken apiToken);

    Stream<Journey> getJourneysSortedByArrivalDateWith(ApiToken apiToken);
}
