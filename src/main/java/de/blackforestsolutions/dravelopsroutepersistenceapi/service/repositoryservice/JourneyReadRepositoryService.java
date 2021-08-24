package de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;

import java.util.UUID;
import java.util.stream.Stream;

public interface JourneyReadRepositoryService {
    Journey getJourneyById(UUID id);

    Stream<Journey> getJourneysSortedByDepartureDateWith(ApiToken apiToken);

    Stream<Journey> getJourneysSortedByArrivalDateWith(ApiToken apiToken);
}
