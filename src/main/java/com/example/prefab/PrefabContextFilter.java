package com.example.prefab;

import cloud.prefab.client.ConfigClient;
import cloud.prefab.context.PrefabContext;
import com.example.auth.ExampleAuthenticationProvider;
import com.example.auth.User;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.FilterChain;
import io.micronaut.http.filter.HttpFilter;
import io.micronaut.http.filter.ServerFilterPhase;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;

@Filter(Filter.MATCH_ALL_PATTERN)
public class PrefabContextFilter implements HttpFilter {

    private final ConfigClient configClient;

    @Inject
    PrefabContextFilter(ConfigClient configClient) {
        this.configClient = configClient;
    }

    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(HttpRequest<?> request, FilterChain chain) {

        request.getUserPrincipal(Authentication.class).ifPresent(authentication ->
                {
                    User user = (User) authentication.getAttributes().get(ExampleAuthenticationProvider.USER_ATTR);
                    configClient.getContextStore()
                            .addContext(PrefabContext.newBuilder("user")
                                .put("country", user.country())
                                .put("email", user.email())
                                .build()
                            );
                }
        );


        return chain.proceed(request);
    }

    @Override
    public int getOrder() {
        return ServerFilterPhase.SECURITY.after() + 1;
        // run after the DefaultLoginFilter
    }
}
