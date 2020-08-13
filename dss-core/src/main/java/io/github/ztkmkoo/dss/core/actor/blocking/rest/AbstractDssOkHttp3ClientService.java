package io.github.ztkmkoo.dss.core.actor.blocking.rest;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingRestCommand;
import io.github.ztkmkoo.dss.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-13 22:03
 */
@Slf4j
public abstract class AbstractDssOkHttp3ClientService<R extends DssBlockingRestCommand.HttpRequest> implements DssHttpClientService<R> {

    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int OK = 200;

    private final int connectTimeoutMillis;
    private final int readTimeoutMillis;

    public AbstractDssOkHttp3ClientService(int connectTimeoutMillis, int readTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
    }

    protected abstract Request buildRequest(R request);
    protected abstract Serializable convertBody(String body);

    @Override
    public void httpRequest(R request, ActorRef<DssBlockingRestCommand> restActor) {
        final Request req = buildRequest(request);
        execute(req, request.getSeq(), request.getSender(), restActor);
    }

    private void execute(Request request, long seq, ActorRef<DssBlockingRestCommand> sender, ActorRef<DssBlockingRestCommand> restActor) {
        final OkHttpClient client = new OkHttpClient
                .Builder()
                .connectTimeout(connectTimeoutMillis, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeoutMillis, TimeUnit.MILLISECONDS)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                log.error("Http request error: ", e);
                final DssBlockingRestCommand.HttpResponse<Serializable> resCommand = DssBlockingRestCommand.HttpResponse
                        .builder(seq, restActor, INTERNAL_SERVER_ERROR)
                        .build();

                sender.tell(resCommand);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final int code = response.code();
                final Serializable body = getBody(code, response.body());

                final DssBlockingRestCommand.HttpResponse<Serializable> resCommand = DssBlockingRestCommand.HttpResponse
                        .builder(seq, restActor, INTERNAL_SERVER_ERROR)
                        .body(body)
                        .build();

                sender.tell(resCommand);
            }

            private Serializable getBody(int code, ResponseBody body) throws IOException {
                if (OK != code || Objects.isNull(body)) {
                    return null;
                }

                final String bodyString = body.string();
                if (StringUtils.isEmpty(bodyString)) {
                    return null;
                }

                return convertBody(bodyString);
            }
        });
    }
}
