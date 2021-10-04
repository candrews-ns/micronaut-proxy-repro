package com.example;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.ProxyHttpClient;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;

import java.net.URI;

// This is /controller/... implementing various ways of proxying from a controller.

@Controller("/controller")
public class HelloController {

    @Inject
    ProxyHttpClient proxyHttpClient;

    @Inject
    HttpClient httpClient;

    // Directly return a Publisher<...> -- does not work.
    @Get(uri = "/publisher")
    public Publisher<MutableHttpResponse<?>> publisher() {
        MutableHttpRequest<?> req = HttpRequest.GET(getUri());
        return proxyHttpClient.proxy(req);
    }

    private URI getUri() {
        return UriBuilder.of("/api/hello/foo")
                .scheme("http")
                .host("localhost")
                .port(8080)
                .build();
    }
}
