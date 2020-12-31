package de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Leg;
import org.springframework.data.geo.Point;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.Locale;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getJourneyBuilderWithNoEmptyFields;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.LegObjectMother.*;

public class JourneyObjectMother {

    public static Journey getJourneyWithNoEmptyFieldsByArrivalPoint(Point arrivalPoint) {
        return getJourneyBuilderWithNoEmptyFields()
                .setLegs(getLegsByArrivalPoint(arrivalPoint))
                .build();
    }

    public static Journey getJourneyWithNoEmptyFieldsByDeparturePoint(Point departurePoint) {
        return getJourneyBuilderWithNoEmptyFields()
                .setLegs(getLegsByDeparturePoint(departurePoint))
                .build();
    }

    public static Journey getJourneyWithNoEmptyFieldsByArrivalTime(ZonedDateTime arrivalTime) {
        return getJourneyBuilderWithNoEmptyFields()
                .setLegs(getLegsByArrivalTime(arrivalTime))
                .build();
    }

    public static Journey getJourneyWithNoEmptyFieldsByDepartureTime(ZonedDateTime departureTime) {
        return getJourneyBuilderWithNoEmptyFields()
                .setLegs(getLegsByDepartureTime(departureTime))
                .build();
    }

    public static Journey getJourneyWithNoEmptyFieldsByLanguage(Locale language) {
        return getJourneyBuilderWithNoEmptyFields()
                .setLanguage(language)
                .build();
    }

    public static Journey getJourneyWithNoEmptyFieldsBy(ApiToken apiToken) {
        return getJourneyBuilderWithNoEmptyFields()
                .setLanguage(apiToken.getLanguage())
                .setLegs(getLegsBy(apiToken))
                .build();
    }

    private static LinkedList<Leg> getLegsByArrivalPoint(Point arrivalPoint) {
        LinkedList<Leg> legs = new LinkedList<>();
        legs.add(getLegWithNoEmptyFieldsByArrivalPoint(arrivalPoint));
        return legs;
    }

    private static LinkedList<Leg> getLegsByDeparturePoint(Point departurePoint) {
        LinkedList<Leg> legs = new LinkedList<>();
        legs.add(getLegWithNoEmptyFieldsByDeparturePoint(departurePoint));
        return legs;
    }

    private static LinkedList<Leg> getLegsByArrivalTime(ZonedDateTime arrivalTime) {
        LinkedList<Leg> legs = new LinkedList<>();
        legs.add(getLegWithNoEmptyFieldsByArrivalTime(arrivalTime));
        return legs;
    }

    private static LinkedList<Leg> getLegsByDepartureTime(ZonedDateTime departureTime) {
        LinkedList<Leg> legs = new LinkedList<>();
        legs.add(getLegWithNoEmptyFieldsByDepartureTime(departureTime));
        return legs;
    }

    private static LinkedList<Leg> getLegsBy(ApiToken apiToken) {
        LinkedList<Leg> legs = new LinkedList<>();
        legs.add(getLegWithNoEmptyFieldsBy(apiToken));
        return legs;
    }
}
