package com.example.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Handlebars;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Replaces;
import jakarta.inject.Singleton;

@Factory
public class HandlebarsFactory {

    @Singleton
    @Replaces(Handlebars.class)
    public Handlebars provideHandlebars() {
        Handlebars handlebars = new Handlebars();
        handlebars.registerHelpers(HelperSource.class);
        return handlebars;
    }

    static public class HelperSource {

        static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        public static String toLowerCase(String str) {
            return str.toLowerCase();
        }

        public static String toJson(Object obj) {
            try {
                return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
