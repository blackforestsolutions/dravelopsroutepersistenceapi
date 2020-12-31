package de.blackforestsolutions.dravelopsroutpersistenceapi.service.repositoryservice.predicates;

import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.repositoryservice.predicates.LanguagePredicate;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Map;

import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.JourneyObjectMother.getJourneyWithNoEmptyFieldsByLanguage;
import static de.blackforestsolutions.dravelopsroutpersistenceapi.objectmothers.ShaIdObjectMother.SHA_ID_1;
import static org.assertj.core.api.Assertions.assertThat;

class LanguagePredicateTest {

    private final Locale languageToCompare = new Locale("de");

    private final LanguagePredicate classUnderTest = new LanguagePredicate(languageToCompare);

    @Test
    void test_apply_journeyEntry_returns_true_when_languageToCompare_and_testLanguage_are_equal() {
        Locale testLanguage = new Locale("de");
        Journey testData = getJourneyWithNoEmptyFieldsByLanguage(testLanguage);
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isTrue();
    }

    @Test
    void test_apply_journeyEntry_returns_false_when_languageToCompare_and_testLanguage_are_not_equal() {
        Locale testLanguage = new Locale("en");
        Journey testData = getJourneyWithNoEmptyFieldsByLanguage(testLanguage);
        Map.Entry<String, Journey> testEntry = Map.entry(SHA_ID_1, testData);

        boolean result = classUnderTest.apply(testEntry);

        assertThat(result).isFalse();
    }

}
