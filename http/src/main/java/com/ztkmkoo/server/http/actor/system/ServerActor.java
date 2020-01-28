package com.ztkmkoo.server.http.actor.system;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.IncomingConnection;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.settings.ServerSettings;
import akka.japi.function.Function;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import com.ztkmkoo.server.http.actor.system.message.SystemHttpRequestMessage;
import com.ztkmkoo.server.http.actor.system.message.SystemHttpResponseMessage;
import com.ztkmkoo.server.http.actor.system.message.SystemRunHttpServerMessage;

import java.util.*;
import java.util.concurrent.*;

public class ServerActor extends AbstractActor {

    private final LoggingAdapter logger = Logging.getLogger(this);
    private final ActorSystem actorSystem = getContext().getSystem();
    private final ForkJoinPool forkJoinPool = new ForkJoinPool(200);
    private final List<ActorRef> filterList = new ArrayList<>();

    private final Map<Long, SystemHttpResponseMessage> responseMap = new ConcurrentHashMap<>();
    private final Queue<Long> timeoutQueue = new ConcurrentLinkedQueue<>();

    public static Props props() {
        return Props.create(ServerActor.class);
    }

    private ServerActor() {}

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SystemRunHttpServerMessage.class, this::handlingSystemRunHttpServerMessage)
                .build();
    }

    /*Message Handling*/

    /**
     * Run akka http server.
     * @param msg: start message with default config values
     */
    private void handlingSystemRunHttpServerMessage(final SystemRunHttpServerMessage msg) {

        logger.info("Try to run http server.");

        final String host = msg.getHost();
        final int port = msg.getPort();
        final long requestTimeoutMillis = msg.getRequestTimeoutMillis();


        final Materializer materializer = ActorMaterializer.create(actorSystem);
        final ServerSettings serverSettings = ServerSettings.create(actorSystem);

        final Source<IncomingConnection, CompletionStage<ServerBinding>> serverSource =
                Http
                        .get(actorSystem)
                        .bind(ConnectHttp.toHost(host, port), serverSettings);

        final CompletionStage<ServerBinding> serverBindingCompletionStage = serverSource.to(Sink.foreach(connection -> {
            System.out.println("Accepted new connection from " + connection.remoteAddress());

            connection.handleWithAsyncHandler(asyncRequestHandler(requestTimeoutMillis), materializer);
            // this is equivalent to
            // connection.handleWith(Flow.of(HttpRequest.class).map(requestHandler), materializer);

        })).run(materializer);

        logger.info("Akka Http Server is started up: {}:{}", host, port);
    }

    /*private methods*/
    private Function<HttpRequest, CompletionStage<HttpResponse>> asyncRequestHandler(final long timeoutMillis) {
        return request -> {

            logger.info("HttpRequest: {}", request);

            if (Objects.isNull(request)) {
                return completableFutureResponse(HttpResponse.create());
            }

            return CompletableFuture.supplyAsync(() -> {

                final long seq = sendHttpRequest(request);
                return syncWaitHttpResponse(seq, timeoutMillis);
            }, forkJoinPool);
        };
    }

    private long sendHttpRequest(final HttpRequest request) {

        final SystemHttpRequestMessage msg = SystemHttpRequestMessage.builder().httpRequest(request).build();
        self().tell(msg, self());
        return msg.getId();
    }

    private HttpResponse syncWaitHttpResponse(final long seq, final long timeoutMillis) {

        final long start = System.currentTimeMillis();

        while (true) {

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                logger.error(e, "Sync wait http response thread sleep error!");
                return HttpResponse
                        .create()
                        .withStatus(StatusCodes.INTERNAL_SERVER_ERROR);
            }

            if (isTimeout(start, timeoutMillis)) {
                break;
            }

            if (responseMap.containsKey(seq)) {
                final SystemHttpResponseMessage responseMessage = responseMap.get(seq);
                responseMap.remove(seq);

                if (Objects.isNull(responseMessage)) {
                    return HttpResponse
                            .create()
                            .withStatus(StatusCodes.INTERNAL_SERVER_ERROR);
                }

                return responseMessage.getHttpResponse();
            }
        }

        logger.info("It is timeout: {}", seq);

        return HttpResponse
                .create()
                .withStatus(StatusCodes.REQUEST_TIMEOUT);
    }

    /*private static methods*/
    private static CompletionStage<HttpResponse> completableFutureResponse(final HttpResponse response) {
        return CompletableFuture.supplyAsync(() -> response);
    }

    private static boolean isTimeout(final long start, final long timeoutMillis) {

        final long cost = System.currentTimeMillis() - start;
        return cost >= timeoutMillis;
    }
}
