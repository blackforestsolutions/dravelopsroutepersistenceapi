package de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice;

import java.time.ZonedDateTime;

public interface TimeService {
    ZonedDateTime convertEpochSecondsToDate(long epochSeconds);

    ZonedDateTime convertStartDateAndStartTimeToDate(String startDate, String startTime);
}
