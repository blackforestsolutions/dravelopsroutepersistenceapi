package de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyRepositoryService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.RequestTokenHandlerService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.ShaIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class JourneyApiServiceImpl implements JourneyApiService {

    private final RequestTokenHandlerService requestTokenHandlerService;
    private final JourneyRepositoryService journeyRepositoryService;
    private final BackendApiService backendApiService;
    private final ShaIdService shaIdService;
    private final ExceptionHandlerService exceptionHandlerService;
    private final ApiToken otpmapperApiToken;

    @Autowired
    public JourneyApiServiceImpl(RequestTokenHandlerService requestTokenHandlerService, JourneyRepositoryService journeyRepositoryService, BackendApiService backendApiService, ShaIdService shaIdService, ExceptionHandlerService exceptionHandlerService, ApiToken otpmapperApiToken) {
        this.requestTokenHandlerService = requestTokenHandlerService;
        this.journeyRepositoryService = journeyRepositoryService;
        this.backendApiService = backendApiService;
        this.shaIdService = shaIdService;
        this.exceptionHandlerService = exceptionHandlerService;
        this.otpmapperApiToken = otpmapperApiToken;
    }

    @Override
    public Flux<Journey> retrieveJourneysFromApiOrRepositoryService(ApiToken userRequestToken) {
        return Flux.merge(
                retrieveJourneysFromRepositoryServiceWith(userRequestToken),
                retrieveJourneysFromApiServiceWith(userRequestToken)
        ).distinct(this::distinctByShaIdWith);
    }

    private Flux<Journey> retrieveJourneysFromApiServiceWith(ApiToken userRequestToken) {
        return Mono.just(userRequestToken)
                .flatMapMany(userToken -> backendApiService.getManyBy(userToken, otpmapperApiToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class))
                .doOnNext(journey -> {
                    try {
                        journeyRepositoryService.writeJourneyToMapWith(userRequestToken, journey);
                    } catch (Exception e) {
                        exceptionHandlerService.handleExceptions(e);
                    }
                })
                .onErrorResume(exceptionHandlerService::handleExceptions);
    }

    private Flux<Journey> retrieveJourneysFromRepositoryServiceWith(ApiToken userRequestToken) {
        Objects.requireNonNull(userRequestToken.getIsArrivalDateTime(), "isArrivalDateTime is not allowed to be null");
        try {
            return executeJourneyRepositoryServiceWith(userRequestToken)
                    .onErrorResume(exceptionHandlerService::handleExceptions);
        } catch (Exception e) {
            return exceptionHandlerService.handleExceptions(e);
        }
    }

    private Flux<Journey> executeJourneyRepositoryServiceWith(ApiToken userRequestToken) {
        if (userRequestToken.getIsArrivalDateTime()) {
            return Mono.just(userRequestToken)
                    .flatMapMany(userToken -> Flux.fromStream(journeyRepositoryService.getJourneysSortedByArrivalDateWith(userToken)));
        }
        return Mono.just(userRequestToken)
                .flatMapMany(userToken -> Flux.fromStream(journeyRepositoryService.getJourneysSortedByDepartureDateWith(userToken)));
    }

    private String distinctByShaIdWith(Journey journey) {
        try {
            return shaIdService.generateShaIdWith(journey);
        } catch (Exception e) {
            exceptionHandlerService.handleExceptions(e);
            return journey.toString();
        }
    }

}
