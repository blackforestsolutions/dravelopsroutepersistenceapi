package de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates;

import com.hazelcast.query.Predicate;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import org.springframework.data.geo.Point;

import java.util.Map;

public class DeparturePointPredicate implements Predicate<String, Journey> {

    private final Point departurePointToCompare;

    public DeparturePointPredicate(Point departurePointToCompare) {
        this.departurePointToCompare = departurePointToCompare;
    }

    @Override
    public boolean apply(Map.Entry<String, Journey> entry) {
        Point departurePoint = entry.getValue().getLegs().getFirst().getDeparture().getPoint();

        return departurePoint.getX() == departurePointToCompare.getX() && departurePoint.getY() == departurePointToCompare.getY();
    }
}
