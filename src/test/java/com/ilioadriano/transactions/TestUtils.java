package com.ilioadriano.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.path.json.mapper.factory.Jackson2ObjectMapperFactory;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

@UtilityClass
public class TestUtils {

    public static String generateRandomDocumentNumber() {
        int min = 100_000_000; // Smallest 9-digit number
        int max = 999_999_999; // Largest 9-digit number

        Integer number = new Random().nextInt(max - min + 1) + min;
        return String.valueOf(number);
    }

    public static String readStringFromResource(String resourcePath) throws URISyntaxException, IOException {
        URL resourceURL = TestUtils.class.getClassLoader().getResource(resourcePath);

        if (resourceURL != null) {
            return Files.readString(Path.of(resourceURL.toURI()));
        }

        throw new IOException("Resource does not exists. " + resourcePath);
    }

    public static RestAssuredConfig buildRestAssuredConfig(ObjectMapper objectMapper) {
        Jackson2ObjectMapperFactory jackson2ObjectMapperFactory = (type, s) -> objectMapper;
        Jackson2Mapper jackson2Mapper = new Jackson2Mapper(jackson2ObjectMapperFactory);
        ObjectMapperConfig objectMapperConfig = new ObjectMapperConfig().defaultObjectMapper(jackson2Mapper);
        return RestAssured.config().objectMapperConfig(objectMapperConfig);
    }
}
