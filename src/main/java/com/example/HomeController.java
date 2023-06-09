package com.example;

import cloud.prefab.client.ConfigClient;
import cloud.prefab.client.FeatureFlagClient;
import cloud.prefab.context.PrefabContextHelper;
import cloud.prefab.context.PrefabContextSetReadable;
import com.example.auth.ExampleAuthenticationProvider;
import com.example.auth.User;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller
public class HomeController {
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    private final FeatureFlagClient featureFlagClient;
    private final ConfigClient configClient;

    @Inject
    HomeController(FeatureFlagClient featureFlagClient, ConfigClient configClient) {
        this.featureFlagClient = featureFlagClient;
        this.configClient = configClient;
    }

    @Get
    @View("home")
    Map<String, Object> index(@Nullable Authentication auth) {
        LOG.debug("🔍 Hello debug logger");
        LOG.info("ℹ️ Hello info logger");
        LOG.warn("⚠️ Hello warn logger");
        LOG.error("🚨 Hello error logger");
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("loggedIn", auth != null);
        templateData.put("userNames", ExampleAuthenticationProvider.ALL_USERS.stream().map(User::name).collect(Collectors.toList()));
        templateData.put("allUsers", ExampleAuthenticationProvider.ALL_USERS);
        if (auth != null) {
            templateData.put("currentUser", auth.getAttributes().get(ExampleAuthenticationProvider.USER_ATTR));
        }
        templateData.put("showGdprBanner", featureFlagClient.featureIsOn("gdpr.banner"));
        // this block evaluates the configs with no pre-existing context so its the default values, unaffected by logged in user context
        try (PrefabContextHelper.PrefabContextScope ignored = new PrefabContextHelper(configClient).performWorkWithAutoClosingContext(PrefabContextSetReadable.EMPTY)) {
            templateData.put("allConfigs", configClient.getAll(PrefabContextSetReadable.EMPTY));
        }
        return templateData;
    }
}
