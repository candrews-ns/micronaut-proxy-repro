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

import io.micronaut.http.*;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.ProxyHttpClient;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.rxjava2.http.client.proxy.RxProxyHttpClient;
import io.micronaut.validation.Validated;
import io.reactivex.Flowable;
import io.reactivex.Single;

import jakarta.inject.Inject;
import javax.validation.constraints.NotBlank;
import java.net.URI;

@Controller("/")
@Validated
public class HelloController {

    @Inject
    RxProxyHttpClient rxHttpClient;

    @Inject
    ProxyHttpClient httpClient;

    @Get(uri = "/blocking-first")
    public HttpResponse<?> blockingFirst() {
        URI uri = UriBuilder.of("http://localhost:8080/hello/foo").build();
        MutableHttpRequest<?> req = HttpRequest.GET(uri);
        return rxHttpClient.proxy(req).blockingFirst();
    }

    @Get(uri = "/single-or-error")
    public Single<MutableHttpResponse<?>> singleOrError() {
        URI uri = UriBuilder.of("http://localhost:8080/hello/foo").build();
        MutableHttpRequest<?> req = HttpRequest.GET(uri);
        return rxHttpClient.proxy(req).singleOrError();
    }
    
    @Get(uri = "/flowable")
    public Flowable<MutableHttpResponse<?>> flowable() {
        URI uri = UriBuilder.of("http://localhost:8080/hello/foo").build();
        MutableHttpRequest<?> req = HttpRequest.GET(uri);
        return rxHttpClient.proxy(req);
    }

    @Get(uri = "/from-publisher")
    public Flowable<MutableHttpResponse<?>> fromPublisher() {
        URI uri = UriBuilder.of("http://localhost:8080/hello/foo").build();
        MutableHttpRequest<?> req = HttpRequest.GET(uri);
        return Flowable.fromPublisher(httpClient.proxy(req));
    }

    @Get(uri = "/from-publisher-single-or-error")
    public Single<MutableHttpResponse<?>> fromPublisherSingleOrError() {
        URI uri = UriBuilder.of("http://localhost:8080/hello/foo").build();
        MutableHttpRequest<?> req = HttpRequest.GET(uri);
        return Flowable.fromPublisher(httpClient.proxy(req)).singleOrError();
    }

    @Get(uri = "/hello/{name}", produces = MediaType.TEXT_PLAIN)
    public Single<String> hello(@NotBlank String name) {
        return Single.just("Hello " + name + "!");
    }
}
