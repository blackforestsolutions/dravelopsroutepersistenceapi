package de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyCreateRepositoryService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyReadRepositoryService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.RequestTokenHandlerService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.ShaIdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class JourneyHandlerServiceImpl implements JourneyHandlerService {

    private final RequestTokenHandlerService requestTokenHandlerService;
    private final JourneyCreateRepositoryService journeyCreateRepositoryService;
    private final JourneyReadRepositoryService journeyReadRepositoryService;
    private final BackendApiService backendApiService;
    private final ShaIdService shaIdService;
    private final ExceptionHandlerService exceptionHandlerService;
    private final ApiToken otpmapperApiToken;

    @Autowired
    public JourneyHandlerServiceImpl(RequestTokenHandlerService requestTokenHandlerService, JourneyCreateRepositoryService journeyCreateRepositoryService, JourneyReadRepositoryService journeyReadRepositoryService, BackendApiService backendApiService, ShaIdService shaIdService, ExceptionHandlerService exceptionHandlerService, ApiToken otpmapperApiToken) {
        this.requestTokenHandlerService = requestTokenHandlerService;
        this.journeyCreateRepositoryService = journeyCreateRepositoryService;
        this.journeyReadRepositoryService = journeyReadRepositoryService;
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
                )
                .distinct(this::distinctByShaIdWith);
    }

    private Flux<Journey> retrieveJourneysFromApiServiceWith(ApiToken userRequestToken) {
        return Mono.just(userRequestToken)
                .flatMapMany(userToken -> backendApiService.getManyBy(userToken, otpmapperApiToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class))
                .doOnNext(journey -> {
                    try {
                        journeyCreateRepositoryService.writeJourneyToMapWith(journey);
                        log.info("Writing Journey to Map was successfull");
                    } catch (Exception e) {
                        exceptionHandlerService.handleExceptions(e);
                    }
                })
                .onErrorResume(exceptionHandlerService::handleExceptions);
    }

    private Flux<Journey> retrieveJourneysFromRepositoryServiceWith(ApiToken userRequestToken) {
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
                    .flatMapMany(userToken -> Flux.fromStream(journeyReadRepositoryService.getJourneysSortedByArrivalDateWith(userToken)));
        }
        return Mono.just(userRequestToken)
                .flatMapMany(userToken -> Flux.fromStream(journeyReadRepositoryService.getJourneysSortedByDepartureDateWith(userToken)));
    }

    private String distinctByShaIdWith(Journey journey) {
        try {
            return shaIdService.generateShaIdWith(journey);
        } catch (Exception e) {
            exceptionHandlerService.handleExceptions(e);
            return "";
        }
    }

}
