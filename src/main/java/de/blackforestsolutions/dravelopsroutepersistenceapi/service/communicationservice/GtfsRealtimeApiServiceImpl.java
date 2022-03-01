package de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice;

import com.google.transit.realtime.GtfsRealtime;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Leg;
import de.blackforestsolutions.dravelopsdatamodel.TravelProvider;
import de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.GtfsRealtimeConfiguration;
import de.blackforestsolutions.dravelopsroutepersistenceapi.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.exceptionhandling.LegCanceledException;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.mapperservice.GtfsRealtimeMapperService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GtfsRealtimeApiServiceImpl implements GtfsRealtimeApiService {

    private static final int ZERO_REALTIME_FEEDS = 0;

    private final CallService callService;
    private final GtfsRealtimeMapperService gtfsRealtimeMapperService;
    private final GtfsRealtimeConfiguration gtfsRealtimeConfiguration;
    private final TimeService timeService;
    private final ExceptionHandlerService exceptionHandlerService;

    @Autowired
    public GtfsRealtimeApiServiceImpl(CallService callService, GtfsRealtimeMapperService gtfsRealtimeMapperService, GtfsRealtimeConfiguration gtfsRealtimeConfiguration, TimeService timeService, ExceptionHandlerService exceptionHandlerService) {
        this.callService = callService;
        this.gtfsRealtimeMapperService = gtfsRealtimeMapperService;
        this.gtfsRealtimeConfiguration = gtfsRealtimeConfiguration;
        this.timeService = timeService;
        this.exceptionHandlerService = exceptionHandlerService;
    }

    @Override
    public Mono<Map<String, GtfsRealtime.FeedMessage>> getGtfsRealtimeFeeds() {
        return Optional.ofNullable(gtfsRealtimeConfiguration.getApitokens())
                .map(this::executeRealtimeCalls)
                .orElse(Mono.just(Collections.emptyMap()));
    }

    private Mono<Map<String, GtfsRealtime.FeedMessage>> executeRealtimeCalls(Map<String, GtfsRealtimeConfiguration.ApiToken> gtfsRealtimeTokens) {
        return Flux.fromIterable(gtfsRealtimeTokens.entrySet())
                .doOnNext(realtimeToken -> log.warn("Executing gtfs realtime call for ".concat(realtimeToken.getKey()).concat("...")))
                .flatMap(realtimeToken -> Mono.just(realtimeToken)
                        .flatMap(this::executeRealtimeCallWith)
                        .subscribeOn(Schedulers.parallel())
                )
                .collectMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    private Mono<Map.Entry<String, GtfsRealtime.FeedMessage>> executeRealtimeCallWith(Map.Entry<String, GtfsRealtimeConfiguration.ApiToken> apiToken) {
        try {
            URL url = new URL(apiToken.getValue().getGtfsRealtimeUrl());
            Map<String, String> httpHeaders = buildHttpHeadersWith(apiToken.getValue());
            return Mono.just(Map.entry(apiToken.getKey(), callService.getGtfsRealtime(url, httpHeaders)));
        } catch (Exception e) {
            return exceptionHandlerService.handleException(e);
        }
    }

    private Map<String, String> buildHttpHeadersWith(GtfsRealtimeConfiguration.ApiToken gtfsRealtimeConfiguration) {
        return Optional.ofNullable(gtfsRealtimeConfiguration.getHeaders())
                .orElse(Collections.emptyMap());
    }

    @Override
    public Mono<Journey> updateJourneyWithRealtimeFeeds(Journey journey, Map<String, GtfsRealtime.FeedMessage> realtimeFeeds) {
        if (realtimeFeeds.size() == ZERO_REALTIME_FEEDS) {
            return Mono.just(journey);
        }
        try {
            return Mono.just(updateJourneyIfAvailable(journey, updateLegsIfAvailable(journey.getLegs(), realtimeFeeds)));
        } catch (LegCanceledException ignored) {
            return Mono.empty();
        }
    }

    private Journey updateJourneyIfAvailable(Journey journey, LinkedList<Leg> newLegs) {
        if (newLegs.equals(journey.getLegs())) {
            return journey;
        }
        return new Journey.JourneyBuilder(journey)
                .setLegs(newLegs)
                .build();
    }

    private LinkedList<Leg> updateLegsIfAvailable(LinkedList<Leg> legs, Map<String, GtfsRealtime.FeedMessage> realtimeFeeds) {
        return legs.stream()
                .map(leg -> updateLegIfRealtimeFeedExists(leg, realtimeFeeds))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private Leg updateLegIfRealtimeFeedExists(Leg leg, Map<String, GtfsRealtime.FeedMessage> realtimeFeeds) {
        return Optional.ofNullable(leg.getTravelProvider())
                .map(TravelProvider::getId)
                .flatMap(travelProviderId -> Optional.ofNullable(realtimeFeeds.get(travelProviderId)))
                .flatMap(realtimeFeed -> Optional.ofNullable(realtimeFeed.getEntityList()))
                .map(updateEntities -> updateLegIfAnyUpdateEntityExist(leg, updateEntities))
                .orElse(leg);
    }

    private Leg updateLegIfAnyUpdateEntityExist(Leg leg, List<GtfsRealtime.FeedEntity> updateEntities) {
        for (GtfsRealtime.FeedEntity updateEntity : updateEntities) {
            leg = updateLegIfUpdateEntityExist(leg, updateEntity);
        }
        return leg;
    }

    private Leg updateLegIfUpdateEntityExist(Leg leg, GtfsRealtime.FeedEntity updateEntity) {
        if (updateEntity.hasTripUpdate() && containsTripUpdateForLeg(updateEntity.getTripUpdate(), leg)) {
            return gtfsRealtimeMapperService.updateLegWithTripUpdate(leg, updateEntity.getTripUpdate());
        }
        if (updateEntity.hasAlert()) {
            gtfsRealtimeMapperService.updateLegWithAlert(leg, updateEntity.getAlert());
            return leg;
        }
        if (updateEntity.hasVehicle()) {
            gtfsRealtimeMapperService.updateLegWithVehiclePosition(leg, updateEntity.getVehicle());
            return leg;
        }
        return leg;
    }

    private boolean containsTripUpdateForLeg(GtfsRealtime.TripUpdate tripUpdate, Leg leg) {
        if (hasInvalidFieldsForTripUpdate(tripUpdate)) {
            return false;
        }
        if (hasTravelProviderNonFrequencyBasedTrips(leg)) {
            return hasTripUpdateOnNonFrequencyBasedTrips(tripUpdate, leg);
        }
        if (hasInvalidFieldsForTripUpdateOnFrequencyBasedTrips(tripUpdate)) {
            return false;
        }
        return hasTripUpdateOnFrequencyBasedTrips(tripUpdate, leg);
    }

    private boolean hasTravelProviderNonFrequencyBasedTrips(Leg leg) {
        String travelProviderId = leg.getTravelProvider().getId();
        return !gtfsRealtimeConfiguration.getApitokens().get(travelProviderId).getHasFrequencyEntries();
    }

    private boolean hasTripUpdateOnNonFrequencyBasedTrips(GtfsRealtime.TripUpdate tripUpdate, Leg leg) {
        String legTravelProviderId = leg.getTravelProvider().getId();
        String tripUpdateTripId = tripUpdate.getTrip().getTripId();

        return buildUniqueTripIdBy(legTravelProviderId, tripUpdateTripId).equals(leg.getTripId());
    }

    private boolean hasTripUpdateOnFrequencyBasedTrips(GtfsRealtime.TripUpdate tripUpdate, Leg leg) {
        String tripUpdateStartDate = tripUpdate.getTrip().getStartDate();
        String tripUpdateStartTime = tripUpdate.getTrip().getStartTime();

        ZonedDateTime tripUpdateDepartureTime = extractDepartureTimeFrom(tripUpdateStartDate, tripUpdateStartTime);
        ZonedDateTime legDepartureTime = leg.getDeparture().getDepartureTime();

        boolean equalTripId = hasTripUpdateOnNonFrequencyBasedTrips(tripUpdate, leg);
        boolean equalDepartureTime = tripUpdateDepartureTime.isEqual(legDepartureTime);

        return equalTripId && equalDepartureTime;
    }

    private boolean hasInvalidFieldsForTripUpdate(GtfsRealtime.TripUpdate tripUpdate) {
        if (!tripUpdate.hasTrip()) {
            log.warn("TripUpdate has no Trip instance to identify related leg by tripId/startTime/startDate!");
            return true;
        }
        if (!tripUpdate.getTrip().hasTripId()) {
            log.warn("TripUpdate has no TripId to identify related leg by tripId! One reason could the tripUpdate contains an experimental field direction_id, which is not implemented yet!");
            return true;
        }
        return false;
    }

    private boolean hasInvalidFieldsForTripUpdateOnFrequencyBasedTrips(GtfsRealtime.TripUpdate tripUpdate) {
        if (!tripUpdate.getTrip().hasStartDate()) {
            log.warn("TripUpdate has no startDate to identify related leg on a frequency based trip!");
            return true;
        }
        if (!tripUpdate.getTrip().hasStartTime()) {
            log.warn("TripUpdate has no startTime to identify related leg on a frequency based trip!");
            return true;
        }
        return false;
    }

    private String buildUniqueTripIdBy(String travelProviderId, String tripId) {
        return ""
                .concat(travelProviderId)
                .concat(":")
                .concat(tripId);
    }

    private ZonedDateTime extractDepartureTimeFrom(String startDate, String startTime) {
        return timeService.convertStartDateAndStartTimeToDate(
                startDate,
                startTime
        );
    }
}