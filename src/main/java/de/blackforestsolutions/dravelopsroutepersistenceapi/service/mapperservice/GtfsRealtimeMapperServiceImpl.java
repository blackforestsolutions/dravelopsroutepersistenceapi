package de.blackforestsolutions.dravelopsroutepersistenceapi.service.mapperservice;

import com.google.transit.realtime.GtfsRealtime.TripDescriptor.ScheduleRelationship;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeEvent;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;
import de.blackforestsolutions.dravelopsdatamodel.Leg;
import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import de.blackforestsolutions.dravelopsdatamodel.TravelProvider;
import de.blackforestsolutions.dravelopsroutepersistenceapi.exceptionhandling.LegCanceledException;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.google.transit.realtime.GtfsRealtime.*;

@Slf4j
@Service
public class GtfsRealtimeMapperServiceImpl implements GtfsRealtimeMapperService {

    private static final int ZERO_STOP_TIME_UPDATES = 0;

    private final TimeService timeService;

    @Autowired
    public GtfsRealtimeMapperServiceImpl(TimeService timeService) {
        this.timeService = timeService;
    }

    @Override
    public Leg updateLegWithTripUpdate(Leg leg, TripUpdate tripUpdate) {
        if (tripUpdate.getTrip().getScheduleRelationship().equals(ScheduleRelationship.CANCELED)) {
            throw new LegCanceledException();
        }
        if (tripUpdate.getStopTimeUpdateCount() == ZERO_STOP_TIME_UPDATES) {
            log.warn("TripUpdate has zero StopTimeUpdates!");
            return leg;
        }

        LinkedList<TravelPoint> allTravelPoints = updateAllTravelPointsWith(leg, tripUpdate);
        return new Leg.LegBuilder(leg)
                .setVehicleName(extractVehicleNameFrom(tripUpdate).orElse(leg.getVehicleName()))
                .setDeparture(allTravelPoints.getFirst())
                .setArrival(allTravelPoints.getLast())
                .setIntermediateStops(extractNewIntermediateStopsFrom(allTravelPoints))
                .build();
    }

    private LinkedList<TravelPoint> updateAllTravelPointsWith(Leg leg, TripUpdate tripUpdate) {
        AtomicInteger stopTimeUpdateCounter = new AtomicInteger();
        AtomicReference<Duration> departureDelayInSeconds = new AtomicReference<>(Duration.ZERO);
        AtomicReference<Duration> arrivalDelayInSeconds = new AtomicReference<>(Duration.ZERO);

        return extractAllTravelPointsFrom(leg)
                .stream()
                .map(travelPoint -> {
                    /*
                     * The following lines of codes work with six params. These six params are too many to put in one
                     * function. Unfortunately splitting the parameters into multiple functions with less params would
                     * make the algorithm more complicated to update each TravelPoint in a trip. I guess this is the
                     * best solution.
                     */
                    if (stopTimeUpdateCounter.get() < tripUpdate.getStopTimeUpdateCount()) {
                        StopTimeUpdate stopUpdate = tripUpdate.getStopTimeUpdate(stopTimeUpdateCounter.get());
                        if (hasEqualStopSequenceOrStopId(stopUpdate, travelPoint, leg.getTravelProvider())) {
                            departureDelayInSeconds.set(extractDepartureDelayInSecondsFrom(travelPoint, stopUpdate));
                            arrivalDelayInSeconds.set(extractArrivalDelayInSecondsFrom(travelPoint, stopUpdate));
                            stopTimeUpdateCounter.incrementAndGet();
                        }
                    }
                    return buildNewTravelPointWith(travelPoint, departureDelayInSeconds.get(), arrivalDelayInSeconds.get());
                })
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private int initStopTimeUpdateCounterWith(Leg leg, TripUpdate tripUpdate) {
        int stopTimeUpdateCounter = 0;
        for (StopTimeUpdate stopTimeUpdate : tripUpdate.getStopTimeUpdateList()) {
            for (TravelPoint travelPoint : extractAllTravelPointsFrom(leg)) {
                if (hasEqualStopSequenceOrStopId(stopTimeUpdate, travelPoint, leg.getTravelProvider())) {
                    return stopTimeUpdateCounter;
                }
            }
            stopTimeUpdateCounter++;
        }
        return stopTimeUpdateCounter;
    }

    private LinkedList<TravelPoint> extractAllTravelPointsFrom(Leg leg) {
        LinkedList<TravelPoint> oldTravelPoints = new LinkedList<>();

        oldTravelPoints.add(leg.getDeparture());
        oldTravelPoints.addAll(leg.getIntermediateStops());
        oldTravelPoints.add(leg.getArrival());

        return oldTravelPoints;
    }

    private TravelPoint buildNewTravelPointWith(TravelPoint travelPoint, Duration departureDelayInSeconds, Duration arrivalDelayInSeconds) {
        if (departureDelayInSeconds.isZero() && arrivalDelayInSeconds.isZero()) {
            return travelPoint;
        }
        return new TravelPoint.TravelPointBuilder(travelPoint)
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .build();
    }

    private Optional<String> extractVehicleNameFrom(TripUpdate tripUpdate) {
        if (tripUpdate.hasVehicle() && tripUpdate.getVehicle().hasLabel()) {
            return Optional.of(tripUpdate.getVehicle().getLabel());
        }
        return Optional.empty();
    }

    private LinkedList<TravelPoint> extractNewIntermediateStopsFrom(LinkedList<TravelPoint> allStops) {
        allStops.removeFirst();
        allStops.removeLast();
        return allStops;
    }

    private boolean hasEqualStopSequenceOrStopId(StopTimeUpdate stopUpdate, TravelPoint travelPoint, TravelProvider travelProvider) {
        if (stopUpdate.hasStopSequence() && stopUpdate.hasStopId()) {
            return hasEqualStopSequence(stopUpdate, travelPoint);
        }
        return hasEqualStopSequence(stopUpdate, travelPoint) || hasEqualStopId(stopUpdate, travelPoint, travelProvider);
    }

    private boolean hasEqualStopSequence(StopTimeUpdate stopUpdate, TravelPoint travelPoint) {
        if (!stopUpdate.hasStopSequence()) {
            return false;
        }
        return stopUpdate.getStopSequence() == travelPoint.getStopSequence();
    }

    private boolean hasEqualStopId(StopTimeUpdate stopUpdate, TravelPoint travelPoint, TravelProvider travelProvider) {
        if (!stopUpdate.hasStopId()) {
            return false;
        }
        return buildUniqueStopIdBy(travelProvider.getId(), stopUpdate.getStopId()).equals(travelPoint.getStopId());
    }

    private String buildUniqueStopIdBy(String travelProviderId, String stopId) {
        return ""
                .concat(travelProviderId)
                .concat(":")
                .concat(stopId);
    }

    private Duration extractDepartureDelayInSecondsFrom(TravelPoint travelPoint, StopTimeUpdate stopUpdate) {
        Optional<ZonedDateTime> travelPointDepartureTime = Optional.ofNullable(travelPoint.getDepartureTime());
        Optional<StopTimeEvent> tripUpdateDepartureTime = Optional.ofNullable(stopUpdate.getDeparture());

        if (travelPointDepartureTime.isPresent() && tripUpdateDepartureTime.isPresent()) {
            return extractDelayInSecondsFrom(
                    travelPointDepartureTime.get(),
                    tripUpdateDepartureTime.get()
            );
        }
        return Duration.ZERO;
    }

    private Duration extractArrivalDelayInSecondsFrom(TravelPoint travelPoint, StopTimeUpdate stopUpdate) {
        Optional<ZonedDateTime> travelPointArrivalTime = Optional.ofNullable(travelPoint.getArrivalTime());
        Optional<StopTimeEvent> tripUpdateArrivalTime = Optional.ofNullable(stopUpdate.getArrival());

        if (travelPointArrivalTime.isPresent() && tripUpdateArrivalTime.isPresent()) {
            return extractDelayInSecondsFrom(
                    travelPointArrivalTime.get(),
                    tripUpdateArrivalTime.get()
            );
        }
        return Duration.ZERO;
    }

    private Duration extractDelayInSecondsFrom(ZonedDateTime travelPointTime, StopTimeEvent stopTimeEvent) {
        if (stopTimeEvent.hasTime()) {
            return extractDelayInSecondsFrom(travelPointTime, stopTimeEvent.getTime());
        }
        if (stopTimeEvent.hasDelay()) {
            return Duration.ofSeconds(stopTimeEvent.getDelay());
        }
        return Duration.ZERO;
    }

    private Duration extractDelayInSecondsFrom(ZonedDateTime travelPointTime, long stopUpdateEpochSeconds) {
        ZonedDateTime stopUpdateTime = timeService.convertEpochSecondsToDate(stopUpdateEpochSeconds);
        return Duration.between(travelPointTime, stopUpdateTime);
    }
}
