package de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.data.geo.Point;

import java.time.ZonedDateTime;
import java.util.Locale;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getApiTokenBuilderWithNoEmptyFields;

public class ApiTokenObjectMother {

    public static ApiToken getApiTokenWithNoEmptyFieldsBy(Point arrivalCoordinate, Point departureCoordinate, ZonedDateTime dateTime, Locale language) {
        return getApiTokenBuilderWithNoEmptyFields()
                .setArrivalCoordinate(arrivalCoordinate)
                .setDepartureCoordinate(departureCoordinate)
                .setDateTime(dateTime)
                .setLanguage(language)
                .build();
    }
}
