package de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RequestTokenHandlerServiceImpl implements RequestTokenHandlerService {

    public ApiToken mergeJourneyApiTokensWith(ApiToken userRequest, ApiToken configuredRequestData) {
        Objects.requireNonNull(userRequest.getArrivalCoordinate(), "arrivalCoordinate is not allowed to be null");
        Objects.requireNonNull(userRequest.getDepartureCoordinate(), "departureCoordinate is not allowed to be null");
        Objects.requireNonNull(userRequest.getDateTime(), "dateTime is not allowed to be null");
        Objects.requireNonNull(userRequest.getOptimize(), "optimize is not allowed to be null");
        Objects.requireNonNull(userRequest.getLanguage(), "departureCoordinate is not allowed to be null");

        return new ApiToken.ApiTokenBuilder(configuredRequestData)
                .setArrivalCoordinate(userRequest.getArrivalCoordinate())
                .setDepartureCoordinate(userRequest.getDepartureCoordinate())
                .setDateTime(userRequest.getDateTime())
                .setOptimize(userRequest.getOptimize())
                .setLanguage(userRequest.getLanguage())
                .build();
    }
}
