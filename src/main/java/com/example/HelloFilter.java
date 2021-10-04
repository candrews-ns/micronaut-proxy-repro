package com.example;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.client.ProxyHttpClient;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.micronaut.runtime.server.EmbeddedServer;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;

// This is a filter at the root, implementing a (working) way of proxying from a filter.
// It's at the root, because that's how we'd need to use it in the CWS, so we're making
// sure we can do that.

@Filter("/**")
public class HelloFilter implements HttpServerFilter {
    @Inject
    ProxyHttpClient client;

    @Inject
    EmbeddedServer server;

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        if (request.getPath().startsWith("/api") || request.getPath().startsWith("/controller"))
            return chain.proceed(request);

        MutableHttpRequest<?> mutableHttpRequest = request.mutate()
                .uri(uri -> uri.scheme(server.getScheme())
                        .host(server.getHost())
                        .port(server.getPort())
                        .replacePath("/api" + request.getPath())
                );
        return client.proxy(mutableHttpRequest);
    }
}