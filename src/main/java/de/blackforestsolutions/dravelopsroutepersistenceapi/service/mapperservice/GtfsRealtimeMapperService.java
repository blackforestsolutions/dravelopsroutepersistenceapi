package de.blackforestsolutions.dravelopsroutepersistenceapi.service.mapperservice;

import com.google.transit.realtime.GtfsRealtime;
import de.blackforestsolutions.dravelopsdatamodel.Leg;

public interface GtfsRealtimeMapperService {
    Leg updateLegWithTripUpdate(Leg leg, GtfsRealtime.TripUpdate tripUpdate);
}
