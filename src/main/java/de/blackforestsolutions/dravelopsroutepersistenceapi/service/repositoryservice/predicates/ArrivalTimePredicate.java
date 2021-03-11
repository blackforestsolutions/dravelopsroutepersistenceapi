package de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates;

import com.hazelcast.query.Predicate;
import de.blackforestsolutions.dravelopsdatamodel.Journey;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

public class ArrivalTimePredicate implements Predicate<UUID, Journey> {

    private static final long serialVersionUID = -4729480972321148445L;
    private final ZonedDateTime maxArrivalTimeToCompare;
    private final ZonedDateTime minArrivalTimeToCompare;

    public ArrivalTimePredicate(ZonedDateTime maxArrivalTimeToCompare, int minArrivalTimeInMinutes) {
        this.maxArrivalTimeToCompare = maxArrivalTimeToCompare;
        this.minArrivalTimeToCompare = maxArrivalTimeToCompare.minusMinutes(minArrivalTimeInMinutes);
    }

    @Override
    public boolean apply(Map.Entry<UUID, Journey> entry) {
        ZonedDateTime arrivalTime = entry.getValue().getLegs().getLast().getArrival().getArrivalTime();

        if (arrivalTime.equals(maxArrivalTimeToCompare) || arrivalTime.equals(minArrivalTimeToCompare)) {
            return true;
        }
        return arrivalTime.isBefore(maxArrivalTimeToCompare) && arrivalTime.isAfter(minArrivalTimeToCompare);
    }
}
