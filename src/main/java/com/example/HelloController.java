/*
 * Copyright 2017 original authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.example;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.ProxyHttpClient;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;

import javax.validation.constraints.NotBlank;
import java.net.URI;

@Controller("/api")
@Validated
public class HelloController {

    @Inject
    ProxyHttpClient httpClient;

    @Get(uri = "/publisher")
    public Publisher<MutableHttpResponse<?>> publisher() {
        URI uri = UriBuilder.of("http://localhost:8080/hello/foo").build();
        MutableHttpRequest<?> req = HttpRequest.GET(uri);
        return httpClient.proxy(req);
    }

    @Get(uri = "/hello/{name}", produces = MediaType.TEXT_PLAIN)
    public String hello(@NotBlank String name) {
        return "Hello " + name + "!";
    }
}
