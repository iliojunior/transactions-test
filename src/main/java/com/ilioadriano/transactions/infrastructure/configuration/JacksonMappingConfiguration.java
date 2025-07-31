package com.ilioadriano.transactions.infrastructure.configuration;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE;


/**
 * Make configurations about Jackson
 */
@Configuration
public class JacksonMappingConfiguration {

    /**
     * Needed to make {@link PropertyNamingStrategies.SnakeCaseStrategy} the default property naming strategy to jackson
     * @return Jackson2ObjectMapperBuilderCustomizer with SNAKE_CASE customization set
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.propertyNamingStrategy(SNAKE_CASE);
    }

}
