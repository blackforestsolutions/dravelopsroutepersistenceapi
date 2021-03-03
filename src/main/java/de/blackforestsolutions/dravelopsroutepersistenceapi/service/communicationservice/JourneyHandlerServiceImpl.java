package de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyCreateRepositoryService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.JourneyReadRepositoryService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.RequestTokenHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
public class JourneyHandlerServiceImpl implements JourneyHandlerService {

    private final RequestTokenHandlerService requestTokenHandlerService;
    private final JourneyCreateRepositoryService journeyCreateRepositoryService;
    private final JourneyReadRepositoryService journeyReadRepositoryService;
    private final BackendApiService backendApiService;
    private final ExceptionHandlerService exceptionHandlerService;
    private final ApiToken otpMapperApiToken;

    @Autowired
    public JourneyHandlerServiceImpl(RequestTokenHandlerService requestTokenHandlerService, JourneyCreateRepositoryService journeyCreateRepositoryService, JourneyReadRepositoryService journeyReadRepositoryService, BackendApiService backendApiService, ExceptionHandlerService exceptionHandlerService, ApiToken otpMapperApiToken) {
        this.requestTokenHandlerService = requestTokenHandlerService;
        this.journeyCreateRepositoryService = journeyCreateRepositoryService;
        this.journeyReadRepositoryService = journeyReadRepositoryService;
        this.backendApiService = backendApiService;
        this.exceptionHandlerService = exceptionHandlerService;
        this.otpMapperApiToken = otpMapperApiToken;
    }

    @Override
    public Flux<Journey> retrieveJourneysFromApiOrRepositoryService(ApiToken userRequestToken) {
        return Flux.fromIterable(journeyHandlerFunctions())
                .flatMap(journeyHandlerFunction -> Mono.just(journeyHandlerFunction)
                        .flatMapMany(backendService -> backendService.apply(userRequestToken))
                        .subscribeOn(Schedulers.parallel())
                ).distinct(Journey::getId);
    }

    private List<Function<ApiToken, Flux<Journey>>> journeyHandlerFunctions() {
        return List.of(
                this::retrieveJourneysFromRepositoryServiceWith,
                this::retrieveJourneysFromApiServiceWith
        );
    }

    private Flux<Journey> retrieveJourneysFromApiServiceWith(ApiToken userRequestToken) {
        return Mono.just(userRequestToken)
                .flatMapMany(userToken -> backendApiService.getManyBy(userToken, otpMapperApiToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class))
                .doOnNext(journey -> {
                    try {
                        journeyCreateRepositoryService.writeJourneyToMapWith(journey);
                    } catch (Exception e) {
                        exceptionHandlerService.handleExceptions(e);
                    }
                })
                .take(otpMapperApiToken.getMaxResults())
                .onErrorResume(exceptionHandlerService::handleExceptions);
    }

    private Flux<Journey> retrieveJourneysFromRepositoryServiceWith(ApiToken userRequestToken) {
        try {
            return executeJourneyRepositoryServiceWith(userRequestToken)
                    .take(otpMapperApiToken.getMaxResults())
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

}
