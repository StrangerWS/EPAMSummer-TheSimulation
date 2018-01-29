package boot.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by DobryninAM on 22.09.2017.
 */
@Configuration
public class Config {
    @Bean
    public RestTemplate getRestTemplate(RestTemplateBuilder bulider){
        return bulider.build();
    }
}