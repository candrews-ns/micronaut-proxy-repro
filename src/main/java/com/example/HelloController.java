package com.example;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.ProxyHttpClient;
import io.micronaut.http.client.RxProxyHttpClient;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.runtime.server.EmbeddedServer;
import javax.inject.Inject;

import io.reactivex.Single;
import org.reactivestreams.Publisher;

import java.net.URI;

// This is /controller/... implementing various ways of proxying from a controller.

@Controller("/controller")
public class HelloController {

    @Inject
    ProxyHttpClient proxyHttpClient;

    @Inject
    EmbeddedServer server;

    // Directly return a Publisher<...> -- does not work, returns an empty body.
    @Get(uri = "/publisher")
    public Publisher<MutableHttpResponse<?>> publisher() {
        MutableHttpRequest<?> req = HttpRequest.GET(getUri());
        return proxyHttpClient.proxy(req);
    }

    // Return Single<...> -- works in MN 2.5.13
    @Get(uri = "/single")
    public Single<MutableHttpResponse<?>> single() {
        MutableHttpRequest<?> req = HttpRequest.GET(getUri());
        return ((RxProxyHttpClient) proxyHttpClient).proxy(req).singleOrError();
    }

    private URI getUri() {
        return UriBuilder.of("/api/hello/foo")
                .scheme("http")
                .host(server.getHost())
                .port(server.getPort())
                .build();
    }
}
