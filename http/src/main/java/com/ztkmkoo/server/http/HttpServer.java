package com.ztkmkoo.server.http;

import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.IncomingConnection;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.settings.ServerSettings;
import akka.japi.function.Function;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import com.ztkmkoo.server.http.config.HttpConfig;
import com.ztkmkoo.server.http.core.HttpRequestHandlerFactory;

import java.util.Objects;
import java.util.concurrent.CompletionStage;

public class HttpServer {

    private final LoggingAdapter logger;
    private final ActorSystem actorSystem;
    private final HttpConfig httpConfig;
    private final Function<HttpRequest, CompletionStage<HttpResponse>> requestHandler;

    private CompletionStage<ServerBinding> serverBindingCompletionStage;

    public HttpServer(
            final ActorSystem actorSystem,
            final HttpConfig httpConfig,
            final HttpRequestHandlerFactory requestHandlerFactory
    ) {
        Objects.requireNonNull(actorSystem);
        Objects.requireNonNull(httpConfig);
        Objects.requireNonNull(requestHandlerFactory);

        this.actorSystem = actorSystem;
        this.httpConfig = httpConfig;
        this.requestHandler = requestHandlerFactory.httpAsyncHandler();
        this.logger = Logging.getLogger(actorSystem, this);

        Objects.requireNonNull(this.requestHandler);

        actorSystem.getWhenTerminated().thenRun(() -> {
            logger.info("Actor system terminated..");
            this.stop();
        });
    }

    public HttpServer(
            final HttpConfig httpConfig,
            final HttpRequestHandlerFactory requestHandlerFactory
    ) {
        this(ActorSystem.create(), httpConfig, requestHandlerFactory);
    }

    /**
     * Run http server till server terminated
     */
    public final void run() {

        logger.info("Try to run http server.");

        final Materializer materializer = ActorMaterializer.create(actorSystem);
        final ServerSettings serverSettings = ServerSettings.create(actorSystem);

        final Source<IncomingConnection, CompletionStage<ServerBinding>> serverSource =
                Http
                        .get(actorSystem)
                        .bind(ConnectHttp.toHost(httpConfig.getHost(), httpConfig.getPort()), serverSettings);

        serverBindingCompletionStage = serverSource.to(Sink.foreach(connection -> {
            System.out.println("Accepted new connection from " + connection.remoteAddress());

            connection.handleWithAsyncHandler(requestHandler, materializer);
            // this is equivalent to
            // connection.handleWith(Flow.of(HttpRequest.class).map(requestHandler), materializer);

        })).run(materializer);

        logger.info("Akka Http Server is started up: {}:{}", httpConfig.getHost(), httpConfig.getPort());
    }

    public final void stop() {

        logger.info("Try to stop http server.");

        if (Objects.nonNull(serverBindingCompletionStage)) {
            serverBindingCompletionStage
                    .thenCompose(ServerBinding::unbind)
                    .thenAccept(done -> {
                        logger.info("Akka Http server is terminated.");
                        actorSystem.terminate();
                    });
        }
    }
}
