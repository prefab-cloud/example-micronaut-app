# Example Prefab Micronaut Application

This repo shows how to add [Prefab] to your Rails app to get access to features like

- Dynamic log levels
- Feature flags
- Live config

Follow along on YouTube

## Micronaut Launch

The [first commit](https://github.com/prefab-cloud/example-micronaut-app/commit/d915d4d83b8c9e6de9105d3c66dde85b391aa413) was built using [Micronaut Launch], an online wrapper of the micronaut CLI  

## Initial Content

[Full diff](https://github.com/prefab-cloud/example-micronaut-app/compare/new-repo...initial-content)

Before adding Prefab to the project, We'll add a very basic one page site that will allow logging in as various users. Nothing Prefab specific here, so just a quick overview.

* [ExampleAuthenticationProvider](https://github.com/prefab-cloud/example-micronaut-app/blob/initial-content/src/main/java/com/example/auth/ExampleAuthenticationProvider.java) Implements username only login process and a rudimentary database of fake users and one real user, our CEO, Jeff
* [DefaultLoginFilter](https://github.com/prefab-cloud/example-micronaut-app/blob/initial-content/src/main/java/com/example/auth/DefaultLoginFilter.java) Runs after the authentication process to ensure that Jeff is logged if no one else is. Has some logging we'll use to demonstrate dynamic log levels later.
* [HomeController](https://github.com/prefab-cloud/example-micronaut-app/blob/initial-content/src/main/java/com/example/HomeController.java) to build out a template that will be rendered by our handlebars view
* [Home View](https://github.com/prefab-cloud/example-micronaut-app/blob/initial-content/src/main/resources/views/home.hbs) to render our home page with its library of users to log in as.
* Adds stock images for our fake users.

Now you can start the app with `MICRONAUT_ENVIRONMENTS=development ./mvnw mn:run`

## Install Prefab

[Full diff](https://github.com/prefab-cloud/example-micronaut-app/compare/initial-content...install-prefab)
### Dependencies

First we need to add maven dependencies for Prefab by adding a property to control the version

```xml
<prefab.version>0.3.8pre4</prefab.version>
```
Then our dependencies
```xml
<dependency>
    <groupId>cloud.prefab</groupId>
    <artifactId>client</artifactId>
    <version>${prefab.version}</version>
</dependency>
<dependency>
    <groupId>cloud.prefab</groupId>
    <artifactId>logback-listener</artifactId>
    <version>${prefab.version}</version>
</dependency>
<dependency>
    <groupId>cloud.prefab</groupId>
    <artifactId>micronaut</artifactId>
    <version>${prefab.version}</version>
 </dependency>
```

### Dependency Injection

We'll next write a factory class to handle initializing instances of the prefab clients and making them available for injection as-needed.

[PrefabFactory](https://github.com/prefab-cloud/example-micronaut-app/blob/install-prefab/src/main/java/com/example/config/PrefabFactory.java) looks like this

```java
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
    PrefabMDCTurboFilter.install(configClient);
    return configClient;
  }
}
```

The `prefabCloudClient` method creates a Prefab Options object and instantiates a PrefabCloudClient with those options

The `@Context` annotation eagerly instantiates the `ConfigClient`, which requires instantiating `PrefabCloudClient`. The `configClient` method also wires up the `PrefabMDCTurboFilter` which will manage log output for us, as we'll see later.

### Get a Prefab API Key
The last part of adding Prefab to your app is to get an API key from https://app.prefab.cloud and set it as the environment variable PREFAB_API_KEY. We'll restart our app to make sure it uses that env var.

That's all it takes to add Prefab to your app. Now let's take some of the features for a test drive.

## Dynamic log levels

No work needed here, the HomeController already has some log lines in it - as a reminder those are

```java
LOG.debug("🔍 Hello debug logger");
LOG.info("ℹ️ Hello info logger");
LOG.warn("⚠️ Hello warn logger");
LOG.error("🚨 Hello error logger");
```

The default log level in the [logback.xml](https://github.com/prefab-cloud/example-micronaut-app/blob/install-prefab/src/main/resources/logback.xml) file is INFO. When we visit http://localhost:8080 we should see the ERROR, WARN and INFO output but not the DEBUG.

In the Prefab UI, let's set our "Root Log Level" to "INFO". Now reloading http://localhost:8080 shows the error, warn, and info output. Note how the output changes.

Prefab lets you change log levels on the fly. We can even set log levels for specific for specific packages and classes by fully qualified class name.

## Configure Prefab Contexts


[Full diff](https://github.com/prefab-cloud/example-micronaut-app/compare/install-prefab...configure-prefab-context)


## All the Configs

[Full diff](https://github.com/prefab-cloud/example-micronaut-app/compare/configure-prefab-context...show-values-table)




[Prefab]: https://prefab.cloud
[Sign up]: https://app.prefab.cloud/users/sign_up
[Micronaut Launch]: https://micronaut.io/launch/
[dynamic log levels]: https://docs.prefab.cloud/docs/ruby-sdk/dynamic-log-levels
