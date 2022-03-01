package de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice;

import com.google.transit.realtime.GtfsRealtime;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface GtfsRealtimeApiService {
    Mono<Map<String, GtfsRealtime.FeedMessage>> getGtfsRealtimeFeeds();

    Mono<Journey> updateJourneyWithRealtimeFeeds(Journey journey, Map<String, GtfsRealtime.FeedMessage> realtimeFeeds);
}
