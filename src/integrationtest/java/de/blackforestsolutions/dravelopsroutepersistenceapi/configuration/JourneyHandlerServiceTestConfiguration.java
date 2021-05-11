package de.blackforestsolutions.dravelopsroutepersistenceapi.configuration;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.ZonedDateTime;
import java.util.Locale;

@TestConfiguration
public class JourneyHandlerServiceTestConfiguration {

    @Value("${graphql.playground.tabs.JOURNEY_QUERY.variables.departureLatitude}")
    private Double departureLatitude;
    @Value("${graphql.playground.tabs.JOURNEY_QUERY.variables.departureLongitude}")
    private Double departureLongitude;
    @Value("${graphql.playground.tabs.JOURNEY_QUERY.variables.arrivalLatitude}")
    private Double arrivalLatitude;
    @Value("${graphql.playground.tabs.JOURNEY_QUERY.variables.arrivalLongitude}")
    private Double arrivalLongitude;
    @Value("${graphql.playground.tabs.JOURNEY_QUERY.variables.dateTime}")
    private String dateTime;
    @Value("${graphql.playground.tabs.JOURNEY_QUERY.variables.isArrivalDateTime}")
    private Boolean isArrivalDateTime;
    @Value("${graphql.playground.tabs.JOURNEY_QUERY.variables.language}")
    private Locale language;


    @Bean
    public ApiToken routePersistenceApiToken() {
        ApiToken apiToken = new ApiToken();

        apiToken.setDepartureCoordinate(new Point.PointBuilder(departureLongitude, departureLatitude).build());
        apiToken.setArrivalCoordinate(new Point.PointBuilder(arrivalLongitude, arrivalLatitude).build());
        apiToken.setDateTime(ZonedDateTime.parse(dateTime));
        apiToken.setIsArrivalDateTime(isArrivalDateTime);
        apiToken.setLanguage(language);

        return apiToken;
    }
}
