package de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers;

import de.blackforestsolutions.dravelopsdatamodel.Point;
import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;

import java.time.Duration;
import java.time.ZonedDateTime;

public class TravelPointObjectMother {

    public static TravelPoint getBaselMainStationTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("sbb:8500090")
                .setStopSequence(1L)
                .setName("Basel Bad Bf")
                .setPoint(new Point.PointBuilder(7.60691586067732d, 47.5673090682292d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T08:40:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T08:40:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getBaselSbbTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("sbb:8500010:0:5")
                .setStopSequence(2L)
                .setName("Basel SBB")
                .setPoint(new Point.PointBuilder(7.58956040938571d, 47.5474126115932d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T08:47:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T09:06:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getZurichMainStationTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("sbb:8503000:0:10")
                .setStopSequence(4L)
                .setName("Zürich HB")
                .setPoint(new Point.PointBuilder(8.54021154209037d, 47.3781762039461d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T10:00:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T10:07:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getSargansTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("sbb:8509411:0:3")
                .setStopSequence(6L)
                .setName("Sargans")
                .setPoint(new Point.PointBuilder(9.44539893825941d, 47.0453592437944d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T11:03:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T11:03:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getLargantTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("sbb:8509002:0:2")
                .setStopSequence(7L)
                .setName("Landquart")
                .setPoint(new Point.PointBuilder(9.55404118873594d, 46.9674395751018d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T11:12:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T11:13:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getChurTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("sbb:8509000:0:8")
                .setStopSequence(8L)
                .setName("Chur")
                .setPoint(new Point.PointBuilder(9.52893327654131d, 46.8530869214607d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T11:23:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T11:23:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getSandesnebenSchoolTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("hvv:de:01053:33088::857311")
                .setStopSequence(0L)
                .setName("Sandesneben, Schule")
                .setPoint(new Point.PointBuilder(10.483916972089d, 53.688773536977d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T08:19:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T08:19:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getWentorfBullenhorstTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("hvv:de:01053:85302::857541")
                .setStopSequence(1L)
                .setName("Wentorf A/S, Bullenhorst")
                .setPoint(new Point.PointBuilder(10.465430289783d, 53.682594485671d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T08:21:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T08:21:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getSchönbergFireDepartmentTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("hvv:de:01053:85295::857696")
                .setStopSequence(2L)
                .setName("Schönberg, Feuerwehr")
                .setPoint(new Point.PointBuilder(10.432429938699d, 53.678474165052d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T08:24:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T08:24:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getSchönbergPohlenTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("hvv:de:01053:85299::857686")
                .setStopSequence(3L)
                .setName("Schönberg, Pöhlen")
                .setPoint(new Point.PointBuilder(10.427871202699d, 53.682850726808d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T08:26:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T08:26:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getSprengeRaumredderTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("hvv:de:01062:37805::800136")
                .setStopSequence(4L)
                .setName("Sprenge, Raumredder")
                .setPoint(new Point.PointBuilder(10.391586138946d, 53.693746269408d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T08:28:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T08:28:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getMollhagenTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("hvv:de:01062:80184::801846")
                .setStopSequence(5L)
                .setName("Mollhagen, Sprenger Weg")
                .setPoint(new Point.PointBuilder(10.383599702067d, 53.703201795724d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T08:30:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T08:30:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getTodendorfAltenfelderStreetTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("hvv:de:01062:80295::802956")
                .setStopSequence(6L)
                .setName("Todendorf, Altenfelder Straße 7")
                .setPoint(new Point.PointBuilder(10.359751162174d, 53.699045153893d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T08:32:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T08:32:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getTodendorfMainStreetTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("hvv:de:01062:80814::808146")
                .setStopSequence(7L)
                .setName("Todendorf, Hauptstraße")
                .setPoint(new Point.PointBuilder(10.350058376929d, 53.696144697335d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T08:33:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T08:33:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getTodendorfMoorwayTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("hvv:de:01062:80482::809521")
                .setStopSequence(8L)
                .setName("Todendorf, Moorweg")
                .setPoint(new Point.PointBuilder(10.342479324016d, 53.703005005632d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T08:36:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T08:36:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getTodendorfHöltkenklinkTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("hvv:de:01062:80481::809531")
                .setStopSequence(9L)
                .setName("Todendorf, Höltenklink")
                .setPoint(new Point.PointBuilder(10.342151609480d, 53.713638529709d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T08:38:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T08:38:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getHammoorMainTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("hvv:de:01062:80402::809571")
                .setStopSequence(10L)
                .setName("Hammoor, Hauptstraße")
                .setPoint(new Point.PointBuilder(10.321914786872d, 53.712659597685d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T08:41:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T08:41:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getHammoorHoppenbrookTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("hvv:900000804531")
                .setStopSequence(11L)
                .setName("Hammoor, Hoppenbrook")
                .setPoint(new Point.PointBuilder(10.317792147358d, 53.708212303947d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T08:42:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T08:42:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getAhrensburgKurtFischerStreetTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("hvv:de:01062:99512::809581")
                .setStopSequence(12L)
                .setName("Ahrensburg, Kurt-Fischer-Straße (Süd)")
                .setPoint(new Point.PointBuilder(10.262180856965d, 53.681441813077d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T08:47:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T08:47:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getAhrensburgBeimoorWayTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("hvv:de:01062:35007::350012")
                .setStopSequence(13L)
                .setName("Ahrensburg, Beimoorweg")
                .setPoint(new Point.PointBuilder(10.252781673112d, 53.680417375962d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T08:49:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T08:49:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getAhrensburgHamburgStreetTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("hvv:de:01062:35042::350109")
                .setStopSequence(14L)
                .setName("Ahrensburg, Hamburger Straße")
                .setPoint(new Point.PointBuilder(10.236230351779d, 53.671334464337d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T08:53:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T08:53:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }

    public static TravelPoint getAhrensburgTrainStationTravelPoint(Duration arrivalDelayInSeconds, Duration departureDelayInSeconds) {
        return new TravelPoint.TravelPointBuilder()
                .setStopId("hvv:de:01062:34001::99")
                .setStopSequence(15L)
                .setName("Bf. Ahrensburg")
                .setPoint(new Point.PointBuilder(10.235969641356d, 53.669665864186d).build())
                .setArrivalTime(ZonedDateTime.parse("2022-03-02T08:55:00+01:00[Europe/Berlin]"))
                .setArrivalDelayInSeconds(arrivalDelayInSeconds)
                .setDepartureTime(ZonedDateTime.parse("2022-03-02T08:55:00+01:00[Europe/Berlin]"))
                .setDepartureDelayInSeconds(departureDelayInSeconds)
                .build();
    }
}
