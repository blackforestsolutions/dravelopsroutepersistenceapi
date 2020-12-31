package de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates;

import com.hazelcast.query.Predicate;
import de.blackforestsolutions.dravelopsdatamodel.Journey;

import java.time.ZonedDateTime;
import java.util.Map;

public class DepartureTimePredicate implements Predicate<String, Journey> {

    private static final long serialVersionUID = -6260183345401394137L;
    private final ZonedDateTime minDepartureTimeToCompare;
    private final ZonedDateTime maxDepartureTimeToCompare;

    public DepartureTimePredicate(ZonedDateTime minDepartureTimeToCompare, int maxDepartureTimeInMinutes) {
        this.minDepartureTimeToCompare = minDepartureTimeToCompare;
        this.maxDepartureTimeToCompare = minDepartureTimeToCompare.plusMinutes(maxDepartureTimeInMinutes);
    }

    @Override
    public boolean apply(Map.Entry<String, Journey> entry) {
        ZonedDateTime departureTime = entry.getValue().getLegs().getFirst().getDeparture().getDepartureTime();

        if (departureTime.equals(minDepartureTimeToCompare) || departureTime.equals(maxDepartureTimeToCompare)) {
            return true;
        }
        return departureTime.isAfter(minDepartureTimeToCompare) && departureTime.isBefore(maxDepartureTimeToCompare);
    }
}
