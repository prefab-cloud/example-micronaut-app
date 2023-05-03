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


Before adding Prefab to the project, We'll add a very basic one page site...



## Install Prefab

[Full diff](https://github.com/prefab-cloud/example-micronaut-app/compare/initial-content...install-prefab)

Now we can target loglevels etc

## Dynamic log levels

No work needed here, the HomeController already has some logging in it.


## Configure Prefab Contexts


[Full diff](https://github.com/prefab-cloud/example-micronaut-app/compare/install-prefab...configure-prefab-context)


## All the Configs

[Full diff](https://github.com/prefab-cloud/example-micronaut-app/compare/configure-prefab-context...show-values-table)




[Prefab]: https://prefab.cloud
[Sign up]: https://app.prefab.cloud/users/sign_up
[Micronaut Launch]: https://micronaut.io/launch/
[dynamic log levels]: https://docs.prefab.cloud/docs/ruby-sdk/dynamic-log-levels
