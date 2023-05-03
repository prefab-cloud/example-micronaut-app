package com.example.auth;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class ExampleAuthenticationProvider implements AuthenticationProvider {

    public static final String USER_ATTR = "user-attr";

    public static final List<User> ALL_USERS = List.of(new User(1, "Tony", "UK", "tony@example.com"),
            new User(2, "Joan", "FR", "joan@example.com"),
            new User(3, "Jeff", "US", "jeff.dwyer@prefab.cloud"));


    public static Optional<User> findUserByName(String name) {
        return ALL_USERS.stream().filter(user -> user.name().equals(name)).findFirst();
    }


    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Flux.create(emitter -> {
            Optional<User> userMaybe = findUserByName((String) authenticationRequest.getIdentity());
            if (userMaybe.isPresent()) {
                emitter.next(AuthenticationResponse.success((String) authenticationRequest.getIdentity(), Map.of(USER_ATTR, userMaybe.get())));
                emitter.complete();
            }
            else {
                emitter.error(AuthenticationResponse.exception());
            }
        }, FluxSink.OverflowStrategy.ERROR);
    }
}
