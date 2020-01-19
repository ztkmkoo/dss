package com.ztkmkoo.server.http.core;

import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.japi.function.Function;

import java.util.concurrent.CompletionStage;

public class HttpRequestHandlerFactory {

    public Function<HttpRequest, CompletionStage<HttpResponse>> httpAsyncHandler() {
        return null;
    }
}
