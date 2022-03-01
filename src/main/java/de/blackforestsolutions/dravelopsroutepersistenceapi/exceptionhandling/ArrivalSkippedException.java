package de.blackforestsolutions.dravelopsroutepersistenceapi.exceptionhandling;

public class ArrivalSkippedException extends RuntimeException {

    public ArrivalSkippedException() {
        super("Arrival from leg is skipped!");
    }
}
