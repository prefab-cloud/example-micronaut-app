package com.example.config;

import cloud.prefab.client.ConfigClient;
import cloud.prefab.client.config.ConfigValueUtils;
import cloud.prefab.domain.Prefab;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Replaces;
import jakarta.inject.Singleton;

import java.io.IOException;

@Factory
public class HandlebarsFactory {

    @Singleton
    @Replaces(Handlebars.class)
    public Handlebars provideHandlebars(ConfigClient configClient) {
        Handlebars handlebars = new Handlebars();
        handlebars.registerHelpers(HelperSource.class);
        handlebars.registerHelper("prefabEvaluateAndCoerceToString",
                (Helper<String>) (key, options) -> new Handlebars.SafeString(configClient.get(key).flatMap(ConfigValueUtils::coerceToString).orElse(""))
        );
        handlebars.registerHelper("prefabCoerceToString", (Helper<Prefab.ConfigValue>) (configValue, options) -> ConfigValueUtils.coerceToString(configValue).orElse(""));
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
