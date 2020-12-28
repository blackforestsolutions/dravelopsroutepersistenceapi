package de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import reactor.core.publisher.Flux;

public interface JourneyApiService {
    Flux<Journey> retrieveJourneysFromApiOrRepositoryService(ApiToken userRequestToken);
}
