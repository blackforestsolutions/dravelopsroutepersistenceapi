package de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers;

import com.google.transit.realtime.GtfsRealtime;
import com.google.transit.realtime.GtfsRealtime.TripDescriptor.ScheduleRelationship;
import com.google.transit.realtime.GtfsRealtime.TripUpdate;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;

import java.util.List;

import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.StopTimeUpdateObjectMother.*;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.TripDescriptorObjectMother.getBaselMainStationToChurTripDescriptor;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.TripDescriptorObjectMother.getSandesnebenToAhrensburgTripDescriptpr;

public class TripUpdateObjectMother {

    public static TripUpdate getBaselMainStationToChurTripUpdate() {
        return TripUpdate.newBuilder()
                .setTrip(getBaselMainStationToChurTripDescriptor(ScheduleRelationship.ADDED))
                .addAllStopTimeUpdate(getBaselMainStationToChurStopTimeUpdates())
                .build();
    }

    public static TripUpdate getSandesnebenToAhrensburgTripUpdate() {
        return TripUpdate.newBuilder()
                .setTrip(getSandesnebenToAhrensburgTripDescriptpr())
                .addAllStopTimeUpdate(getSandesnebenToAhrensburgStopTimeUpdates())
                .setVehicle(GtfsRealtime.VehicleDescriptor.newBuilder().setId("999999").setLabel("Langenfelder Damm").build())
                .build();
    }

    public static TripUpdate getCanceledTripUpdate() {
        return TripUpdate.newBuilder()
                .setTrip(getBaselMainStationToChurTripDescriptor(ScheduleRelationship.CANCELED))
                .build();
    }

    public static TripUpdate getTripUpdateWithZeroStopTimeUpdates() {
        return TripUpdate.newBuilder()
                .setTrip(getBaselMainStationToChurTripDescriptor(ScheduleRelationship.SCHEDULED))
                .build();
    }

    private static List<StopTimeUpdate> getBaselMainStationToChurStopTimeUpdates() {
        return List.of(
                getBaselMainStationStopTimeUpdate(),
                getBaselSbbStopTimeUpdate(),
                getZurichMainStationStopTimeUpdate(),
                getSargansStopTimeUpdate(),
                getLargantStopTimeUpdate(),
                getChurStopTimeUpdate()
        );
    }

    private static List<StopTimeUpdate> getSandesnebenToAhrensburgStopTimeUpdates() {
        return List.of(
                getWentorfBullenhorstStopTimeUpdate(),
                getSchönbergFireDepartmentStopTimeUpdate(),
                getSchönbergPohlenStopTimeUpdate(),
                getMollhagenPohlenStopTimeUpdate(),
                getHammoorStopTimeUpdate(),
                getAhrensburgKurtFischerStreetStopTimeUpdate()
        );
    }
}
