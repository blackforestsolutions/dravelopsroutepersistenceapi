package de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;

import java.io.IOException;
import java.util.stream.Stream;

public interface JourneyRepositoryService {
    Stream<Journey> getJourneysSortedByDepartureDateWith(ApiToken apiToken);

    Stream<Journey> getJourneysSortedByArrivalDateWith(ApiToken apiToken);

    Journey writeJourneyToMapWith(ApiToken apiToken, Journey journey) throws IOException;
}
