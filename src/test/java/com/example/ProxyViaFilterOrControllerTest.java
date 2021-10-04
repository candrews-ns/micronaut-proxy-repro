package com.example;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
class ProxyViaFilterOrControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void test_direct() {
        String s = client.toBlocking().retrieve("/api/hello/foo");
        Assertions.assertEquals("Hello foo!", s);
    }

    @Test
    void test_via_controller() {
        String s = client.toBlocking().retrieve("/controller/publisher");
        Assertions.assertEquals("Hello foo!", s);
    }

    @Test
    void test_via_filter() {
        String s = client.toBlocking().retrieve("/hello/foo");
        Assertions.assertEquals("Hello foo!", s);
    }
}
