package de.blackforestsolutions.dravelopsroutepersistenceapi.service.communicationservice.restcalls;

import com.google.transit.realtime.GtfsRealtime;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Service
public class CallServiceImpl implements CallService {

    private final WebClient webClient;

    @Autowired
    public CallServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public <T> Flux<T> postMany(String url, ApiToken body, HttpHeaders httpHeaders, Class<T> returnType) {
        return webClient
                .post()
                .uri(url)
                .body(Mono.just(body), ApiToken.class)
                .headers(headers -> httpHeaders.forEach(headers::addAll))
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(returnType);

    }

    @Override
    public GtfsRealtime.FeedMessage getGtfsRealtime(URL url, Map<String, String> httpHeaders) throws IOException {
        HttpURLConnection httpRequest = (HttpURLConnection) url.openConnection();
        for (Map.Entry<String, String> header : httpHeaders.entrySet()) {
            httpRequest.addRequestProperty(header.getKey(), header.getValue());
        }
        return GtfsRealtime.FeedMessage.parseFrom(httpRequest.getInputStream());
    }
}
