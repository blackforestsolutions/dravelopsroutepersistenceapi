package de.blackforestsolutions.dravelopsroutepersistenceapi.controller;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice.JourneyHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("journeys")
public class JourneyController {

    private final JourneyHandlerService journeyHandlerService;

    public JourneyController(JourneyHandlerService journeyHandlerService) {
        this.journeyHandlerService = journeyHandlerService;
    }

    @RequestMapping(value = "/otp", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Journey> getJourneysBy(@RequestBody ApiToken request) {
        return journeyHandlerService.retrieveJourneysFromApiOrRepositoryService(request);
    }
}
