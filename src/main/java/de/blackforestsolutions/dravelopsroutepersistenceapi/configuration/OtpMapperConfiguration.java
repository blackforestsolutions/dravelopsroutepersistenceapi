package de.blackforestsolutions.dravelopsroutepersistenceapi.configuration;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

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

    @Bean(name = "otpMapperApiToken")
    public ApiToken apiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setProtocol(otpMapperProtocol)
                .setHost(otpMapperHost)
                .setPort(otpMapperPort)
                .setPath(otpMapperJourneyPath)
                .setMaxResults(maxResults)
                .build();
    }
}
