package de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates;

import com.hazelcast.query.Predicate;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import org.springframework.data.geo.Point;

import java.util.Map;

public class ArrivalPointPredicate implements Predicate<String, Journey> {

    private static final long serialVersionUID = -1434743634655172925L;
    private final Point arrivalPointToCompare;

    public ArrivalPointPredicate(Point arrivalPointToCompare) {
        this.arrivalPointToCompare = arrivalPointToCompare;
    }

    @Override
    public boolean apply(Map.Entry<String, Journey> entry) {
        Point arrivalPoint = entry.getValue().getLegs().getLast().getArrival().getPoint();

        return arrivalPoint.getX() == arrivalPointToCompare.getX() && arrivalPoint.getY() == arrivalPointToCompare.getX();
    }
}
