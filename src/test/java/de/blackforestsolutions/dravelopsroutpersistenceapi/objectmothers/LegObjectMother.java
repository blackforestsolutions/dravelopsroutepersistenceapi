package de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Leg;
import org.springframework.data.geo.Point;

import java.time.ZonedDateTime;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.LegObjectMother.getLegBuilderWithNoEmptyFields;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.UUIDObjectMother.TEST_UUID_2;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.TravelPointObjectMother.*;

public class LegObjectMother {

    static Leg getLegWithNoEmptyFieldsByArrivalPoint(Point arrivalPoint) {
        return getLegBuilderWithNoEmptyFields(TEST_UUID_2)
                .setArrival(getTravelPointWithNoEmptyFieldsByArrivalTime(arrivalPoint))
                .build();
    }

    static Leg getLegWithNoEmptyFieldsByDeparturePoint(Point departurePoint) {
        return getLegBuilderWithNoEmptyFields(TEST_UUID_2)
                .setDeparture(getTravelPointWithNoEmptyFieldsByArrivalTime(departurePoint))
                .build();
    }

    static Leg getLegWithNoEmptyFieldsByArrivalTime(ZonedDateTime arrivalTime) {
        return getLegBuilderWithNoEmptyFields(TEST_UUID_2)
                .setArrival(getTravelPointWithNoEmptyFieldsByArrivalTime(arrivalTime))
                .build();
    }

    static Leg getLegWithNoEmptyFieldsByDepartureTime(ZonedDateTime departureTime) {
        return getLegBuilderWithNoEmptyFields(TEST_UUID_2)
                .setDeparture(getTravelPointWithNoEmptyFieldsByDepartureTime(departureTime))
                .build();
    }

    static Leg getLegWithNoEmptyFieldsBy(ApiToken apiToken) {
        return getLegBuilderWithNoEmptyFields(TEST_UUID_2)
                .setDeparture(getDepartureTravelPointWithNoEmptyFieldsBy(apiToken))
                .setArrival(getArrivalTravelPointWithNoEmptyFieldsBy(apiToken))
                .build();
    }

}
