package de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;

@RefreshScope
@Service
public class TimeServiceImpl implements TimeService {

    private static final int LONG_START_TIME_LENGTH = 8;
    private static final int START_TIME_HOURS_START = 0;
    private static final int SHORT_START_TIME_HOURS_END = 1;
    private static final int LONG_START_TIME_HOURS_END = 2;

    private static final int TWENTY_FOUR_HOURS = 24;
    private static final long ONE_DAY = 1L;
    private static final String START_DATE_PATTERN = "yyyyMMdd";
    private static final String START_TIME_PATTERN = "H:mm:ss";

    @Value("${otp.timeZone}")
    private String timeZone;

    @Override
    public ZonedDateTime convertEpochSecondsToDate(long epochSeconds) {
        Instant instant = Instant.ofEpochSecond(epochSeconds);
        return ZonedDateTime.ofInstant(instant, ZoneId.of(timeZone));
    }

    @Override
    public ZonedDateTime convertStartDateAndStartTimeToDate(String startDate, String startTime) {
        LocalDate departureDate = convertToLocalDate(startDate, startTime);
        LocalTime departureTime = convertToLocalTime(startTime);

        return LocalDateTime.of(departureDate, departureTime).atZone(ZoneId.of(timeZone));
    }

    private LocalDate convertToLocalDate(String tripUpdateStartDate, String tripUpdateStartTime) {
        if (getHoursFromStartTime(tripUpdateStartTime) >= TWENTY_FOUR_HOURS) {
            return LocalDate.parse(tripUpdateStartDate, DateTimeFormatter.ofPattern(START_DATE_PATTERN))
                    .plusDays(ONE_DAY);
        }
        return LocalDate.parse(tripUpdateStartDate, DateTimeFormatter.ofPattern(START_DATE_PATTERN));
    }

    private LocalTime convertToLocalTime(String tripUpdateStartTime) {
        if (getHoursFromStartTime(tripUpdateStartTime) >= TWENTY_FOUR_HOURS) {
            tripUpdateStartTime = getStartTimeMinus24Hours(tripUpdateStartTime);
        }
        if (tripUpdateStartTime.length() == LONG_START_TIME_LENGTH) {
            return LocalTime.parse(tripUpdateStartTime, DateTimeFormatter.ISO_LOCAL_TIME);
        }
        return LocalTime.parse(tripUpdateStartTime, DateTimeFormatter.ofPattern(START_TIME_PATTERN));
    }

    private int getHoursFromStartTime(String startTime) {
        if (startTime.length() == LONG_START_TIME_LENGTH) {
            return getLongHoursFromStartTime(startTime);
        }
        return getShortHoursFromStartTime(startTime);
    }

    private String getStartTimeMinus24Hours(String startTime) {
        int minus24Hours = getLongHoursFromStartTime(startTime) - TWENTY_FOUR_HOURS;
        return ""
                .concat(String.valueOf(minus24Hours))
                .concat(StringUtils.substring(startTime, LONG_START_TIME_HOURS_END));
    }

    private int getLongHoursFromStartTime(String startTime) {
        return Integer.parseInt(StringUtils.substring(
                startTime,
                START_TIME_HOURS_START,
                LONG_START_TIME_HOURS_END
        ));
    }

    private int getShortHoursFromStartTime(String startTime) {
        return Integer.parseInt(StringUtils.substring(
                startTime,
                START_TIME_HOURS_START,
                SHORT_START_TIME_HOURS_END
        ));
    }
}
