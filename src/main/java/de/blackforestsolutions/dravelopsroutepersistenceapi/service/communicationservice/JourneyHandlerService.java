package de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface JourneyHandlerService {
    Mono<Journey> getJourneyById(UUID id);

    Flux<Journey> retrieveJourneysFromApiOrRepositoryService(ApiToken userRequestToken);
}
