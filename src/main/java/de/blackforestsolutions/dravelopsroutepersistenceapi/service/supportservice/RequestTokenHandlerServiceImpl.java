package de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RequestTokenHandlerServiceImpl implements RequestTokenHandlerService {

    @Override
    public ApiToken mergeJourneyApiTokensWith(ApiToken userRequest, ApiToken configuredRequestData) {
        Objects.requireNonNull(userRequest.getArrivalCoordinate(), "arrivalCoordinate is not allowed to be null");
        Objects.requireNonNull(userRequest.getDepartureCoordinate(), "departureCoordinate is not allowed to be null");
        Objects.requireNonNull(userRequest.getDateTime(), "dateTime is not allowed to be null");
        Objects.requireNonNull(userRequest.getIsArrivalDateTime(), "isArrivalDateTime is not allowed to be null");
        Objects.requireNonNull(userRequest.getLanguage(), "departureCoordinate is not allowed to be null");

        ApiToken journeyOtpMapperApiToken = new ApiToken(configuredRequestData);

        journeyOtpMapperApiToken.setArrivalCoordinate(userRequest.getArrivalCoordinate());
        journeyOtpMapperApiToken.setDepartureCoordinate(userRequest.getDepartureCoordinate());
        journeyOtpMapperApiToken.setDateTime(userRequest.getDateTime());
        journeyOtpMapperApiToken.setIsArrivalDateTime(userRequest.getIsArrivalDateTime());
        journeyOtpMapperApiToken.setLanguage(userRequest.getLanguage());

        return journeyOtpMapperApiToken;
    }
}
