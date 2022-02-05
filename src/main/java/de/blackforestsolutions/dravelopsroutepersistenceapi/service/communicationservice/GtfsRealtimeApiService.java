package de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice;

import com.google.transit.realtime.GtfsRealtime;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import reactor.core.publisher.Mono;

public interface GtfsRealtimeApiService {
    Mono<GtfsRealtime.FeedMessage> getGtfsRealtimeFeed();

    Journey updateJourneyWithRealtimeFeed(Journey journey, GtfsRealtime.FeedMessage realtimeFeed);
}
