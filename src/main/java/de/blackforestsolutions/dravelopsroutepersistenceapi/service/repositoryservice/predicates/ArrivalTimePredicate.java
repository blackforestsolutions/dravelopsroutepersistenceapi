package de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates;

import com.hazelcast.query.Predicate;
import de.blackforestsolutions.dravelopsdatamodel.Journey;

import java.time.ZonedDateTime;
import java.util.Map;

public class ArrivalTimePredicate implements Predicate<String, Journey> {

    private final ZonedDateTime maxArrivalTimeToCompare;
    private final ZonedDateTime minArrivalTimeToCompare;

    public ArrivalTimePredicate(ZonedDateTime maxArrivalTimeToCompare, int minArrivalTimeInMinutes) {
        this.maxArrivalTimeToCompare = maxArrivalTimeToCompare;
        this.minArrivalTimeToCompare = maxArrivalTimeToCompare.minusMinutes(minArrivalTimeInMinutes);
    }

    @Override
    public boolean apply(Map.Entry<String, Journey> entry) {
        ZonedDateTime arrivalTime = entry.getValue().getLegs().getLast().getArrival().getArrivalTime();

        return arrivalTime.isBefore(maxArrivalTimeToCompare) && arrivalTime.isAfter(minArrivalTimeToCompare);
    }
}
