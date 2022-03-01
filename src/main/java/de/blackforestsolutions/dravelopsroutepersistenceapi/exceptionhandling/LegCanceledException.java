package de.blackforestsolutions.dravelopsroutepersistenceapi.exceptionhandling;

public class LegCanceledException extends RuntimeException {

    public LegCanceledException() {
        super("Leg is canceled by TravelProvider!");
    }
}
