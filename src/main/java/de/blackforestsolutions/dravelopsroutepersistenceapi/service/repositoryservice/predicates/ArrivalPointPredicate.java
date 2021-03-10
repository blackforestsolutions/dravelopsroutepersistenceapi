package de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates;

import com.hazelcast.query.Predicate;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Point;

import java.util.Map;
import java.util.UUID;

public class ArrivalPointPredicate implements Predicate<UUID, Journey> {

    private static final long serialVersionUID = -1434743634655172925L;
    /**
     * tolerance value to avoid double overflows
     */
    private static final double MAX_EPSILON = 0.00001d;

    private final Point arrivalPointToCompare;

    public ArrivalPointPredicate(Point arrivalPointToCompare) {
        this.arrivalPointToCompare = arrivalPointToCompare;
    }

    @Override
    public boolean apply(Map.Entry<UUID, Journey> entry) {
        Point arrivalPoint = entry.getValue().getLegs().getLast().getArrival().getPoint();

        // save way for arrivalPoint.getX() == arrivalPointToCompare.getX() && arrivalPoint.getY() == arrivalPointToCompare.getY();
        // avoids a double overflow
        return Math.abs(arrivalPoint.getX() - arrivalPointToCompare.getX()) < MAX_EPSILON
                &&
                Math.abs(arrivalPoint.getY() - arrivalPointToCompare.getY()) < MAX_EPSILON;
    }
}
