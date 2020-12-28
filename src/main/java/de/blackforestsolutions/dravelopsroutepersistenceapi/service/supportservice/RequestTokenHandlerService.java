package de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;

public interface RequestTokenHandlerService {

    ApiToken mergeJourneyApiTokensWith(ApiToken userRequest, ApiToken configuredRequestData);
}
