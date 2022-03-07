package de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers;

import de.blackforestsolutions.dravelopsdatamodel.Leg;
import de.blackforestsolutions.dravelopsdatamodel.Point;
import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import de.blackforestsolutions.dravelopsdatamodel.VehicleType;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;

import java.time.Duration;
import java.util.LinkedList;
import java.util.function.Supplier;

import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.TravelPointObjectMother.*;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.TravelProviderObjectMother.*;

public class LegObjectMother {

    private static final Metrics DEFAULT_TEST_DISTANCE_METRIC = Metrics.KILOMETERS;

    public static Leg getBaselMainStationToChurLegBeforeTripUpdate() {
        TravelPoint departure = getBaselMainStationTravelPoint(Duration.ZERO, Duration.ZERO);
        TravelPoint arrival = getChurTravelPoint(Duration.ZERO, Duration.ZERO);
        Supplier<LinkedList<TravelPoint>> intermediateStops = LegObjectMother::getBaselMainStationToChurIntermediateStopsBeforeTripUpdate;

        return getBaselMainStationToChurLeg(
                departure,
                arrival,
                intermediateStops
        );
    }

    public static Leg getBaselMainStationToChurLegAfterTripUpdate() {
        TravelPoint departure = getBaselMainStationTravelPoint(Duration.ZERO, Duration.ZERO);
        TravelPoint arrival = getChurTravelPoint(Duration.ofSeconds(-60L), Duration.ZERO);
        Supplier<LinkedList<TravelPoint>> intermediateStops = LegObjectMother::getBaselMainStationToChurIntermediateStopsAfterTripUpdate;

        return getBaselMainStationToChurLeg(
                departure,
                arrival,
                intermediateStops
        );
    }

    public static Leg getSandesnebenToAhrensburgLegBeforeTripUpdate() {
        String vehicleName = "Bf. Ratzeburg > Nusse > Sandesneben > Bf. Ahrensburg";
        TravelPoint departure = getSandesnebenSchoolTravelPoint(Duration.ZERO, Duration.ZERO);
        TravelPoint arrival = getAhrensburgTrainStationTravelPoint(Duration.ZERO, Duration.ZERO);
        Supplier<LinkedList<TravelPoint>> intermediateStops = LegObjectMother::getSandesnebenToAhrensburgIntermediateStopsBeforeTripUpdate;

        return getSandesnebenToAhrensburgLeg(
                vehicleName,
                departure,
                arrival,
                intermediateStops
        );
    }

    public static Leg getSandesnebenToAhrensburgLegBeforeAfterUpdate() {
        String vehicleName = "Langenfelder Damm";
        TravelPoint departure = getSandesnebenSchoolTravelPoint(Duration.ZERO, Duration.ZERO);
        TravelPoint arrival = getAhrensburgTrainStationTravelPoint(Duration.ofSeconds(420), Duration.ofSeconds(420));
        Supplier<LinkedList<TravelPoint>> intermediateStops = LegObjectMother::getSandesnebenToAhrensburgIntermediateStopsAfterTripUpdate;

        return getSandesnebenToAhrensburgLeg(
                vehicleName,
                departure,
                arrival,
                intermediateStops
        );
    }

    private static Leg getBaselMainStationToChurLeg(TravelPoint departure, TravelPoint arrival, Supplier<LinkedList<TravelPoint>> intermediateStops) {
        return new Leg.LegBuilder()
                .setTripId("sbb:385.TA.91-Q-Y-j22-1.115.R")
                .setDistanceInKilometers(new Distance(196.1d, DEFAULT_TEST_DISTANCE_METRIC))
                .setVehicleType(VehicleType.RAIL)
                .setTravelProvider(getSbbTravelProvider())
                .setDeparture(departure)
                .setArrival(arrival)
                .setPolyline("mtodHyhpo@")
                .setWaypoints(getTrack())
                .setVehicleNumber("271")
                .setVehicleName("ICE")
                .setIntermediateStops(intermediateStops.get())
                .build();
    }

    private static Leg getSandesnebenToAhrensburgLeg(String vehicleName, TravelPoint departure, TravelPoint arrival, Supplier<LinkedList<TravelPoint>> intermediateStops) {
        return new Leg.LegBuilder()
                .setTripId("hvv:30214919")
                .setDistanceInKilometers(new Distance(22.6d, DEFAULT_TEST_DISTANCE_METRIC))
                .setVehicleType(VehicleType.BUS)
                .setTravelProvider(getHvvTravelProvider())
                .setDeparture(departure)
                .setArrival(arrival)
                .setPolyline("mtodHyhpo@")
                .setWaypoints(getTrack())
                .setVehicleNumber("8730")
                .setVehicleName(vehicleName)
                .setIntermediateStops(intermediateStops.get())
                .build();
    }

    private static LinkedList<TravelPoint> getBaselMainStationToChurIntermediateStopsBeforeTripUpdate() {
        LinkedList<TravelPoint> intermediateStops = new LinkedList<>();

        intermediateStops.add(getBaselSbbTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getZurichMainStationTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getSargansTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getLargantTravelPoint(Duration.ZERO, Duration.ZERO));

        return intermediateStops;
    }

    private static LinkedList<TravelPoint> getBaselMainStationToChurIntermediateStopsAfterTripUpdate() {
        LinkedList<TravelPoint> intermediateStops = new LinkedList<>();

        intermediateStops.add(getBaselSbbTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getZurichMainStationTravelPoint(Duration.ZERO, Duration.ofSeconds(60L)));
        intermediateStops.add(getSargansTravelPoint(Duration.ofSeconds(-60L), Duration.ofSeconds(60L)));
        intermediateStops.add(getLargantTravelPoint(Duration.ZERO, Duration.ofSeconds(60L)));

        return intermediateStops;
    }

    private static LinkedList<TravelPoint> getSandesnebenToAhrensburgIntermediateStopsBeforeTripUpdate() {
        LinkedList<TravelPoint> intermediateStops = new LinkedList<>();

        intermediateStops.add(getWentorfBullenhorstTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getSchönbergFireDepartmentTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getSchönbergPohlenTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getSprengeRaumredderTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getMollhagenTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getTodendorfAltenfelderStreetTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getTodendorfMainStreetTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getTodendorfMoorwayTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getTodendorfHöltkenklinkTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getHammoorMainTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getHammoorHoppenbrookTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getAhrensburgKurtFischerStreetTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getAhrensburgBeimoorWayTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getAhrensburgHamburgStreetTravelPoint(Duration.ZERO, Duration.ZERO));

        return intermediateStops;
    }

    private static LinkedList<TravelPoint> getSandesnebenToAhrensburgIntermediateStopsAfterTripUpdate() {
        LinkedList<TravelPoint> intermediateStops = new LinkedList<>();

        intermediateStops.add(getWentorfBullenhorstTravelPoint(Duration.ofSeconds(120L), Duration.ofSeconds(120L)));
        intermediateStops.add(getSchönbergFireDepartmentTravelPoint(Duration.ofSeconds(60L), Duration.ofSeconds(60L)));
        intermediateStops.add(getSchönbergPohlenTravelPoint(Duration.ofSeconds(0L), Duration.ofSeconds(0L)));
        intermediateStops.add(getSprengeRaumredderTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getMollhagenTravelPoint(Duration.ofSeconds(60L), Duration.ofSeconds(60L)));
        intermediateStops.add(getTodendorfAltenfelderStreetTravelPoint(Duration.ofSeconds(60L), Duration.ofSeconds(60L)));
        intermediateStops.add(getTodendorfMainStreetTravelPoint(Duration.ofSeconds(60L), Duration.ofSeconds(60L)));
        intermediateStops.add(getTodendorfMoorwayTravelPoint(Duration.ofSeconds(60L), Duration.ofSeconds(60L)));
        intermediateStops.add(getTodendorfHöltkenklinkTravelPoint(Duration.ofSeconds(60L), Duration.ofSeconds(60L)));
        intermediateStops.add(getHammoorMainTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getHammoorHoppenbrookTravelPoint(Duration.ZERO, Duration.ZERO));
        intermediateStops.add(getAhrensburgKurtFischerStreetTravelPoint(Duration.ofSeconds(420L), Duration.ofSeconds(420L)));
        intermediateStops.add(getAhrensburgBeimoorWayTravelPoint(Duration.ofSeconds(420L), Duration.ofSeconds(420L)));
        intermediateStops.add(getAhrensburgHamburgStreetTravelPoint(Duration.ofSeconds(420L), Duration.ofSeconds(420L)));

        return intermediateStops;
    }

    private static LinkedList<Point> getTrack() {
        LinkedList<Point> points = new LinkedList<>();
        points.add(new Point.PointBuilder(0.0d, 0.0d).build());
        return points;
    }
}
