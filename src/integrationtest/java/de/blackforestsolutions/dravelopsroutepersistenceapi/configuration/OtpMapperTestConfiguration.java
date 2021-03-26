package de.blackforestsolutions.dravelopsroutepersistenceapi.configuration;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(JourneyHandlerServiceTestConfiguration.class)
@TestConfiguration
public class OtpMapperTestConfiguration {

    @Value("${otpmapper.get.journey.path}")
    private String path;
    @Value("${graphql.playground.tabs[0].maxResults}")
    private Integer maxResults;


    @Bean(name = "journeyOtpMapperApiTokenIT")
    @ConfigurationProperties(prefix = "otpmapper")
    public ApiToken.ApiTokenBuilder apiToken(@Autowired ApiToken routePersistenceApiToken) {
        return new ApiToken.ApiTokenBuilder(routePersistenceApiToken)
                .setPath(path)
                .setMaxResults(maxResults);
    }
}
