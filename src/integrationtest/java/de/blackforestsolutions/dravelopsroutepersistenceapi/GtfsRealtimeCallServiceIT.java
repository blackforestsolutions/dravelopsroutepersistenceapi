package de.blackforestsolutions.dravelopsroutepersistenceapi;

import com.google.transit.realtime.GtfsRealtime;
import de.blackforestsolutions.dravelopsroutepersistenceapi.configuration.GtfsRealtimeConfiguration;
import de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice.restcalls.CallService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class GtfsRealtimeCallServiceIT {

    @Autowired
    private CallService classUnderTest;

    @Autowired
    private GtfsRealtimeConfiguration gtfsRealtimeConfiguration;

    @Test
    void test_getGtfsRealtime_returns_for_every_apiToken_a_realtime_feed() throws IOException {
        for (GtfsRealtimeConfiguration.ApiToken apiToken : gtfsRealtimeConfiguration.getApitokens().values()) {
            URL testUrl = new URL(apiToken.getGtfsRealtimeUrl());
            Map<String, String> testHttpHeaders = Optional.ofNullable(apiToken.getHeaders()).orElse(Collections.emptyMap());

            GtfsRealtime.FeedMessage result = classUnderTest.getGtfsRealtime(testUrl, testHttpHeaders);

            assertThat(result).isNotNull();
            assertThat(result.getEntityList().size()).isGreaterThanOrEqualTo(1);
        }
    }
}
