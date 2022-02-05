package de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice.restcalls;

import com.google.transit.realtime.GtfsRealtime;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public interface CallService {
    <T> Flux<T> postMany(String url, ApiToken body, HttpHeaders httpHeaders, Class<T> returnType);

    GtfsRealtime.FeedMessage getGtfsRealtime(URL url, Map<String, String> httpHeaders) throws IOException;
}
