package de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import org.springframework.data.geo.Point;

import java.time.ZonedDateTime;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.TravelPointObjectMother.getTravelPointBuilderWithNoEmptyFields;

public class TravelPointObjectMother {

    static TravelPoint getTravelPointWithNoEmptyFieldsByArrivalTime(Point point) {
        return getTravelPointBuilderWithNoEmptyFields()
                .setPoint(point)
                .build();
    }

    static TravelPoint getTravelPointWithNoEmptyFieldsByArrivalTime(ZonedDateTime arrivalTime) {
        return getTravelPointBuilderWithNoEmptyFields()
                .setArrivalTime(arrivalTime)
                .build();
    }

    static TravelPoint getTravelPointWithNoEmptyFieldsByDepartureTime(ZonedDateTime departureTime) {
        return getTravelPointBuilderWithNoEmptyFields()
                .setDepartureTime(departureTime)
                .build();
    }

    static TravelPoint getDepartureTravelPointWithNoEmptyFieldsBy(ApiToken apiToken) {
        return getTravelPointBuilderWithNoEmptyFields()
                .setPoint(apiToken.getDepartureCoordinate())
                .setDepartureTime(apiToken.getDateTime())
                .build();
    }

    static TravelPoint getArrivalTravelPointWithNoEmptyFieldsBy(ApiToken apiToken) {
        return getTravelPointBuilderWithNoEmptyFields()
                .setPoint(apiToken.getArrivalCoordinate())
                .setArrivalTime(apiToken.getDateTime())
                .build();
    }
}
