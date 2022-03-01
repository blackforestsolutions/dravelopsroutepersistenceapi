package de.blackforestsolutions.dravelopsroutepersistenceapi.exceptionhandling;

public class DepartureSkippedException extends RuntimeException {

    public DepartureSkippedException() {
        super("Departure from leg is skipped!");
    }
}
