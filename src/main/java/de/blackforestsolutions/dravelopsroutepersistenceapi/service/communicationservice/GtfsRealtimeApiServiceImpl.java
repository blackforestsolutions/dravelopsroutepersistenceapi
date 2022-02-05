package de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice;

import com.google.transit.realtime.GtfsRealtime;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.GtfsRealtimeConfiguration;
import de.blackforestsolutions.dravelopsroutepersistenceapi.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice.restcalls.CallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class GtfsRealtimeApiServiceImpl implements GtfsRealtimeApiService {

    private final CallService callService;
    private final GtfsRealtimeConfiguration gtfsRealtimeConfiguration;
    private final ExceptionHandlerService exceptionHandlerService;

    @Autowired
    public GtfsRealtimeApiServiceImpl(CallService callService, GtfsRealtimeConfiguration gtfsRealtimeConfiguration, ExceptionHandlerService exceptionHandlerService) {
        this.callService = callService;
        this.gtfsRealtimeConfiguration = gtfsRealtimeConfiguration;
        this.exceptionHandlerService = exceptionHandlerService;
    }

    @Override
    public Mono<GtfsRealtime.FeedMessage> getGtfsRealtimeFeed() {
        if (gtfsRealtimeConfiguration.hasGtfsRealtime()) {
            return Optional.ofNullable(gtfsRealtimeConfiguration.getGtfsRealtimeTokens())
                    .map(this::executeRealtimeCalls)
                    .map()
                    .orElseGet(this::logMissingGtfsRealtimeTokens);
        }
        return Mono.empty();
    }

    private Mono<List<GtfsRealtime.FeedMessage>> executeRealtimeCalls(Map<String, GtfsRealtimeConfiguration.ApiToken> gtfsRealtimeTokens) {
        return Flux.fromIterable(gtfsRealtimeTokens.entrySet())
                .doOnNext(realtimeTokenEntry -> log.info("Executing gtfs realtime call for ".concat(realtimeTokenEntry.getKey()).concat(".")))
                .map(Map.Entry::getValue)
                .flatMap(realtimeToken -> Mono.just(realtimeToken)
                        .flatMap(this::executeRealtimeCallWith)
                        .subscribeOn(Schedulers.parallel())
                )
                .collectList();

    }

    private Mono<GtfsRealtime.FeedMessage> executeRealtimeCallWith(GtfsRealtimeConfiguration.ApiToken apiToken) {
        try {
            URL url = new URL(apiToken.getUrl());
            return Mono.just(callService.getGtfsRealtime(url, apiToken.getHeaders()));
        } catch (IOException e) {
            return exceptionHandlerService.handleException(e);
        }
    }

    private GtfsRealtime.FeedMessage mergeRealtimeTokens(List<GtfsRealtime.FeedMessage> gtfsRealtime) {
        return null;

//        for (Map.Entry<String, GtfsRealtimeConfiguration.ApiToken> realtimeToken : realtimeTokens.entrySet()) {
//
//        }
    }

    private Mono<GtfsRealtime.FeedMessage> logMissingGtfsRealtimeTokens() {
        log.warn("HasGtfsRealtime is true, but there is no url provided for call!");
        return Mono.empty();
    }


    @Override
    public Journey updateJourneyWithRealtimeFeed(Journey journey, GtfsRealtime.FeedMessage realtimeFeed) {
        return null;
    }
}
