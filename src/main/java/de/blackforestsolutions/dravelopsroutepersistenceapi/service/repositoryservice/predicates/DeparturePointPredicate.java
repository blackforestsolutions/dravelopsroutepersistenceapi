package de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates;

import com.hazelcast.query.Predicate;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Point;

import java.util.Map;

public class DeparturePointPredicate implements Predicate<String, Journey> {

    private static final long serialVersionUID = -1760994329544417605L;
    /**
     * tolerance value to avoid double overflows
     */
    private static final double MAX_EPSILON = 0.00001d;

    private final Point departurePointToCompare;

    public DeparturePointPredicate(Point departurePointToCompare) {
        this.departurePointToCompare = departurePointToCompare;
    }

    @Override
    public boolean apply(Map.Entry<String, Journey> entry) {
        Point departurePoint = entry.getValue().getLegs().getFirst().getDeparture().getPoint();

        // save way for departurePoint.getX() == departurePointToCompare.getX() && departurePoint.getY() == departurePointToCompare.getY();
        // avoids a double overflow
        return Math.abs(departurePoint.getX() - departurePointToCompare.getX()) < MAX_EPSILON
                &&
                Math.abs(departurePoint.getY() - departurePointToCompare.getY()) < MAX_EPSILON;
    }
}
