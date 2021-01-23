package de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice;

import de.blackforestsolutions.dravelopsdatamodel.Journey;

import java.io.IOException;

public interface JourneyCreateRepositoryService {
    Journey writeJourneyToMapWith(Journey journey) throws IOException;
}
