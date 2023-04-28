package com.example.config;

import cloud.prefab.client.*;
import cloud.prefab.client.config.logging.PrefabContextTurboFilter;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.env.Environment;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Factory
public class PrefabFactory {
  private static final Logger LOG = LoggerFactory.getLogger(PrefabFactory.class);

  @Singleton
  public PrefabCloudClient prefabCloudClient(Environment environment) {
    final Options options = new Options();
    LOG.info("Prefab Envs {}", environment.getActiveNames().stream().toList());
    options.setPrefabEnvs(environment.getActiveNames().stream().toList());
    return new PrefabCloudClient(options);
  }

  @Singleton
  public FeatureFlagClient featureFlagClient(PrefabCloudClient prefabCloudClient) {
    return prefabCloudClient.featureFlagClient();
  }

  @Context
  public ConfigClient configClient(
    PrefabCloudClient prefabCloudClient
  ) {
    ConfigClient configClient = prefabCloudClient.configClient();
    // install the logging filter at the same time
    PrefabContextTurboFilter.install(configClient);
    return configClient;
  }
}
