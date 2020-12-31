package de.blackforestsolutions.dravelopsroutpersistenceapi.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.ShaIdService;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.supportservice.ShaIdServiceImpl;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.internal.Strings;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.regex.Pattern;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getFurtwangenToWaldkirchJourney;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getJourneyWithNoEmptyFields;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ShaIdServiceTest {

    private final ShaIdService classUnderTest = new ShaIdServiceImpl();

    @Test
    void test_generateShaIdWith_journey_returs_valid_sha1() throws IOException {
        Journey testData = getJourneyWithNoEmptyFields();
        Pattern expectedHexPattern = Pattern.compile("[0-9a-f]{40}");

        String result = classUnderTest.generateShaIdWith(testData);

        assertThat(result.length()).isEqualTo(40);
        assertThat(result).isEqualTo("c3963cc0b108768cd309e6603b71c7262f73b542");
        Strings.instance().assertContainsPattern(new WritableAssertionInfo(), result, expectedHexPattern);
    }

    @Test
    void test_generateShaIdWith_journey_returns_different_valid_sha1() throws IOException {
        Journey testData = getFurtwangenToWaldkirchJourney();
        Pattern expectedHexPattern = Pattern.compile("[0-9a-f]{40}");

        String result = classUnderTest.generateShaIdWith(testData);

        assertThat(result).isNotEqualTo("c3963cc0b108768cd309e6603b71c7262f73b542");
        Strings.instance().assertContainsPattern(new WritableAssertionInfo(), result, expectedHexPattern);
    }

    @Test
    void test_generateShaIdWith_null_value_throws_exception() {

        assertThrows(NullPointerException.class, () -> classUnderTest.generateShaIdWith(null));
    }

}
