package de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers;

import de.blackforestsolutions.dravelopsdatamodel.TravelProvider;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;

@Slf4j
public class TravelProviderObjectMother {

    public static TravelProvider getSbbTravelProvider() {
        return new TravelProvider.TravelProviderBuilder()
                .setId("sbb")
                .setName("Schweizerische Bundesbahnen SBB")
                .setUrl(getSbbUrl())
                .build();
    }

    public static TravelProvider getHvvTravelProvider() {
        return new TravelProvider.TravelProviderBuilder()
                .setId("hvv")
                .setName("Hamburger Verkehrsverbund (hvv)")
                .setUrl(getHvvUrl())
                .build();
    }

    public static TravelProvider getVrsTravelProvider() {
        return new TravelProvider.TravelProviderBuilder()
                .setId("vrs")
                .setName("NX National Express Rail GmbH")
                .setUrl(getVrsUrl())
                .build();
    }

    private static URL getSbbUrl() {
        try {
            return new URL("http://www.sbb.ch/");
        } catch (Exception e) {
            log.error("URL from TravelProvider was not possible to parse due to: ", e);
            return null;
        }
    }

    private static URL getHvvUrl() {
        try {
            return new URL("http://www.hvv.de");
        } catch (Exception e) {
            log.error("URL from TravelProvider was not possible to parse due to: ", e);
            return null;
        }
    }

    private static URL getVrsUrl() {
        try {
            return new URL("https://www.nationalexpress.de");
        } catch (Exception e) {
            log.error("URL from TravelProvider was not possible to parse due to: ", e);
            return null;
        }
    }
}
