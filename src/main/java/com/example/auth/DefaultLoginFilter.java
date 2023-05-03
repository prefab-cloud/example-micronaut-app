package com.example.auth;

import io.micronaut.http.HttpAttributes;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.FilterChain;
import io.micronaut.http.filter.HttpFilter;
import io.micronaut.http.filter.ServerFilterPhase;
import io.micronaut.security.authentication.Authentication;
import org.reactivestreams.Publisher;

import java.util.Map;

@Filter(Filter.MATCH_ALL_PATTERN)
public class DefaultLoginFilter implements HttpFilter {
    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(HttpRequest<?> request, FilterChain chain) {

        if (request.getUserPrincipal().isEmpty()) {
            request.setAttribute(HttpAttributes.PRINCIPAL, new Authentication() {
                @Override
                public Map<String, Object> getAttributes() {
                    return Map.of(ExampleAuthenticationProvider.USER_ATTR, ExampleAuthenticationProvider.findUserByName("Jeff").get());
                }

                @Override
                public String getName() {
                    return "Jeff";
                }
            });
        }
        return chain.proceed(request);

    }

    @Override
    public int getOrder() {
        return ServerFilterPhase.SECURITY.after();
    }
}
