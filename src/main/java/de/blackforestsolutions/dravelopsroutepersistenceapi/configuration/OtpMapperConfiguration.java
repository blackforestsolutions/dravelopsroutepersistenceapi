package de.blackforestsolutions.dravelopsroutepersistenceapi.configuration;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

@RefreshScope
@SpringBootConfiguration
public class OtpMapperConfiguration {

    @Value("${otpmapper.protocol}")
    private String otpMapperProtocol;
    @Value("${otpmapper.host}")
    private String otpMapperHost;
    @Value("${otpmapper.port}")
    private int otpMapperPort;
    @Value("${otpmapper.get.journey.path}")
    private String otpMapperJourneyPath;
    @Value("${graphql.playground.tabs[0].maxResults}")
    private Integer maxResults;

    @RefreshScope
    @Bean
    public ApiToken otpMapperApiToken() {
        ApiToken apiToken = new ApiToken();

        apiToken.setProtocol(otpMapperProtocol);
        apiToken.setHost(otpMapperHost);
        apiToken.setPort(otpMapperPort);
        apiToken.setPath(otpMapperJourneyPath);
        apiToken.setMaxResults(maxResults);

        return apiToken;
    }
}
