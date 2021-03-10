package de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates;

import com.hazelcast.query.Predicate;
import de.blackforestsolutions.dravelopsdatamodel.Journey;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class LanguagePredicate implements Predicate<UUID, Journey> {

    private static final long serialVersionUID = -3709201656536195728L;
    private final Locale languageToCompare;

    public LanguagePredicate(Locale languageToCompare) {
        this.languageToCompare = languageToCompare;
    }

    @Override
    public boolean apply(Map.Entry<UUID, Journey> entry) {
        Locale language = entry.getValue().getLanguage();

        return language.getLanguage().equals(languageToCompare.getLanguage());
    }
}
