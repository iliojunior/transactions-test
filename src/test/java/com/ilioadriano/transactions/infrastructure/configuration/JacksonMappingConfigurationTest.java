package com.ilioadriano.transactions.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static org.assertj.core.api.Assertions.assertThat;

class JacksonMappingConfigurationTest {

    @Test
    void shouldConfigureJackson2ObjectMapperBuilderCustomizerToSnakeCase() {
        JacksonMappingConfiguration jacksonMappingConfiguration = new JacksonMappingConfiguration();
        Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer = jacksonMappingConfiguration.jackson2ObjectMapperBuilderCustomizer();
        Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = new Jackson2ObjectMapperBuilder();

        jackson2ObjectMapperBuilderCustomizer.customize(jackson2ObjectMapperBuilder);

        ObjectMapper objectMapper = jackson2ObjectMapperBuilder.build();
        assertThat(objectMapper.getPropertyNamingStrategy()).isInstanceOf(PropertyNamingStrategies.SnakeCaseStrategy.class);
    }

}