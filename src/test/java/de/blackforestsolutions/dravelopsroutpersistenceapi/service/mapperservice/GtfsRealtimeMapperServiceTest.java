package de.blackforestsolutions.dravelopsroutpersistenceapi.service.mapperservice;

import com.google.transit.realtime.GtfsRealtime.TripUpdate;
import de.blackforestsolutions.dravelopsdatamodel.Leg;
import de.blackforestsolutions.dravelopsroutepersistenceapi.exceptionhandling.LegCanceledException;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.mapperservice.GtfsRealtimeMapperService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.mapperservice.GtfsRealtimeMapperServiceImpl;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.TimeService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.TimeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.LegObjectMother.*;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.TripUpdateObjectMother.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GtfsRealtimeMapperServiceTest {

    private final TimeService timeService = new TimeServiceImpl();

    private final GtfsRealtimeMapperService classUnderTest = new GtfsRealtimeMapperServiceImpl(timeService);

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(timeService, "timeZone", "Europe/Berlin");
    }

    @Test
    void test_updateLegWithTripUpdate_should_return_updated_leg_when_all_travel_point_have_related_trip_updat() {
        Leg testLeg = getBaselMainStationToChurLegBeforeTripUpdate();
        TripUpdate testTripUpdate = getBaselMainStationToChurTripUpdate();

        Leg result = classUnderTest.updateLegWithTripUpdate(testLeg, testTripUpdate);

        assertThat(result).isEqualToComparingFieldByFieldRecursively(getBaselMainStationToChurLegAfterTripUpdate());
    }

    @Test
    void test_updateLegWithTripUpdate_should_return_updated_leg_when_start_intermediate_and_end_travel_point_have_no_related_trip_update() {
        Leg testLeg = getSandesnebenToAhrensburgLegBeforeTripUpdate();
        TripUpdate testTripUpdate = getSandesnebenToAhrensburgTripUpdate();

        Leg result = classUnderTest.updateLegWithTripUpdate(testLeg, testTripUpdate);

        assertThat(result).isEqualToComparingFieldByFieldRecursively(getSandesnebenToAhrensburgLegBeforeAfterUpdate());
    }

    @Test
    void test_updateLegWithTripUpdate_should_throw_leg_canceled_exception_when_scheduled_relationship_in_trip_update_is_canceled() {
        Leg testLeg = getBaselMainStationToChurLegBeforeTripUpdate();
        TripUpdate testTripUpdate = getCanceledTripUpdate();

        assertThrows(LegCanceledException.class, () -> classUnderTest.updateLegWithTripUpdate(testLeg, testTripUpdate));
    }

    @Test
    void test_updateLegWithTripUpdate_should_return_same_leg_when_there_are_zero_stop_time_updates_available() {
        Leg testLeg = getBaselMainStationToChurLegBeforeTripUpdate();
        TripUpdate testTripUpdate = getTripUpdateWithZeroStopTimeUpdates();

        Leg result = classUnderTest.updateLegWithTripUpdate(testLeg, testTripUpdate);

        assertThat(result).isEqualToComparingFieldByFieldRecursively(testLeg);
    }
}
