package de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers;

import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeEvent;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate.ScheduleRelationship;

public class StopTimeUpdateObjectMother {

    public static StopTimeUpdate getBaselMainStationStopTimeUpdate() {
        return StopTimeUpdate.newBuilder()
                .setStopSequence(1)
                .setStopId("8500090")
                .setDeparture(StopTimeEvent.newBuilder().setDelay(0).build())
                .setScheduleRelationship(ScheduleRelationship.SCHEDULED)
                .build();
    }

    public static StopTimeUpdate getBaselSbbStopTimeUpdate() {
        return StopTimeUpdate.newBuilder()
                .setStopSequence(2)
                .setStopId("8500010:0:4")
                .setArrival(StopTimeEvent.newBuilder().setDelay(0).build())
                .setDeparture(StopTimeEvent.newBuilder().setDelay(0).build())
                .setScheduleRelationship(ScheduleRelationship.SCHEDULED)
                .build();
    }

    public static StopTimeUpdate getZurichMainStationStopTimeUpdate() {
        return StopTimeUpdate.newBuilder()
                .setStopSequence(4)
                .setStopId("8503000:0:10")
                .setArrival(StopTimeEvent.newBuilder().setDelay(0).build())
                .setDeparture(StopTimeEvent.newBuilder().setDelay(60).build())
                .setScheduleRelationship(ScheduleRelationship.SCHEDULED)
                .build();
    }

    public static StopTimeUpdate getSargansStopTimeUpdate() {
        return StopTimeUpdate.newBuilder()
                .setStopSequence(6)
                .setStopId("8509411:0:3")
                .setArrival(StopTimeEvent.newBuilder().setDelay(-60).build())
                .setDeparture(StopTimeEvent.newBuilder().setDelay(60).build())
                .setScheduleRelationship(ScheduleRelationship.SCHEDULED)
                .build();
    }

    public static StopTimeUpdate getLargantStopTimeUpdate() {
        return StopTimeUpdate.newBuilder()
                .setStopSequence(7)
                .setStopId("8509002:0:2")
                .setArrival(StopTimeEvent.newBuilder().setDelay(0).build())
                .setDeparture(StopTimeEvent.newBuilder().setDelay(60).build())
                .setScheduleRelationship(ScheduleRelationship.SCHEDULED)
                .build();
    }

    public static StopTimeUpdate getChurStopTimeUpdate() {
        return StopTimeUpdate.newBuilder()
                .setStopSequence(8)
                .setStopId("8509000:0:8")
                .setArrival(StopTimeEvent.newBuilder().setDelay(-60).build())
                .setScheduleRelationship(ScheduleRelationship.SCHEDULED)
                .build();
    }

    public static StopTimeUpdate getWentorfBullenhorstStopTimeUpdate() {
        return StopTimeUpdate.newBuilder()
                .setStopSequence(1)
                .setStopId("de:01053:85302::857541")
                .setArrival(StopTimeEvent.newBuilder().setDelay(120).setTime(1646205780L).build())
                .setDeparture(StopTimeEvent.newBuilder().setDelay(120).setTime(1646205780L).build())
                .setScheduleRelationship(ScheduleRelationship.SCHEDULED)
                .build();
    }

    public static StopTimeUpdate getSchönbergFireDepartmentStopTimeUpdate() {
        return StopTimeUpdate.newBuilder()
                .setStopSequence(2)
                .setStopId("de:01053:85295::857696")
                .setArrival(StopTimeEvent.newBuilder().setDelay(60).setTime(1646205900L).build())
                .setDeparture(StopTimeEvent.newBuilder().setDelay(60).setTime(1646205900L).build())
                .setScheduleRelationship(ScheduleRelationship.SCHEDULED)
                .build();
    }

    public static StopTimeUpdate getSchönbergPohlenStopTimeUpdate() {
        return StopTimeUpdate.newBuilder()
                .setStopSequence(3)
                .setStopId("de:01053:85299::857686")
                .setArrival(StopTimeEvent.newBuilder().setDelay(0).setTime(1646205960L).build())
                .setDeparture(StopTimeEvent.newBuilder().setDelay(0).setTime(1646205960L).build())
                .setScheduleRelationship(ScheduleRelationship.SCHEDULED)
                .build();
    }

    public static StopTimeUpdate getMollhagenPohlenStopTimeUpdate() {
        return StopTimeUpdate.newBuilder()
                .setStopSequence(5)
                .setStopId("de:01062:80184::801846")
                .setArrival(StopTimeEvent.newBuilder().setDelay(60).setTime(1646206260L).build())
                .setDeparture(StopTimeEvent.newBuilder().setDelay(60).setTime(1646206260L).build())
                .setScheduleRelationship(ScheduleRelationship.SCHEDULED)
                .build();
    }

    public static StopTimeUpdate getHammoorStopTimeUpdate() {
        return StopTimeUpdate.newBuilder()
                .setStopSequence(10)
                .setStopId("de:01062:80402::809571")
                .setArrival(StopTimeEvent.newBuilder().setDelay(0).setTime(1646206860L).build())
                .setDeparture(StopTimeEvent.newBuilder().setDelay(0).setTime(1646206860L).build())
                .setScheduleRelationship(ScheduleRelationship.SCHEDULED)
                .build();
    }

    public static StopTimeUpdate getAhrensburgKurtFischerStreetStopTimeUpdate() {
        return StopTimeUpdate.newBuilder()
                .setStopSequence(12)
                .setStopId("de:01062:99512::809581")
                .setArrival(StopTimeEvent.newBuilder().setDelay(420).setTime(1646207640L).build())
                .setDeparture(StopTimeEvent.newBuilder().setDelay(420).setTime(1646207640L).build())
                .setScheduleRelationship(ScheduleRelationship.SCHEDULED)
                .build();
    }
}
