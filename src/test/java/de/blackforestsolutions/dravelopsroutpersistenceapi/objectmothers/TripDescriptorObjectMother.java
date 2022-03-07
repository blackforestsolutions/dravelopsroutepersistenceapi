package de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers;

import com.google.transit.realtime.GtfsRealtime.TripDescriptor;
import com.google.transit.realtime.GtfsRealtime.TripDescriptor.ScheduleRelationship;

public class TripDescriptorObjectMother {

    public static TripDescriptor getBaselMainStationToChurTripDescriptor(ScheduleRelationship scheduleRelationship) {
        return TripDescriptor.newBuilder()
                .setTripId("385.TA.91-Q-Y-j22-1.115.R")
                .setRouteId("91-Q-Y-j22-1")
                .setStartTime("08:40:00")
                .setStartDate("20220302")
                .setScheduleRelationship(scheduleRelationship)
                .build();
    }

    public static TripDescriptor getSandesnebenToAhrensburgTripDescriptpr() {
        return TripDescriptor.newBuilder()
                .setTripId("30214919")
                .setStartTime("08:19:00")
                .setStartDate("20220302")
                .setRouteId("8650_3")
                .build();
    }
}
