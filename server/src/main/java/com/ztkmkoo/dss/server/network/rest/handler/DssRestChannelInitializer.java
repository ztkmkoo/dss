package com.ztkmkoo.dss.server.network.rest.handler;

import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.server.message.ServerMessages;
import com.ztkmkoo.dss.server.network.core.handler.DssChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 19. 오전 12:43
 */
public class DssRestChannelInitializer extends DssChannelInitializer<SocketChannel> {

    private static final long DEFAULT_TIMEOUT = 3000;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MILLISECONDS;

    private final long timeout;
    private final TimeUnit timeUnit;
    private final ActorRef<ServerMessages.Req> masterActorRef;

    public DssRestChannelInitializer(long timeout, TimeUnit timeUnit, ActorRef<ServerMessages.Req> masterActorRef) {
        Objects.requireNonNull(timeUnit);
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.masterActorRef = masterActorRef;
    }

    public DssRestChannelInitializer(ActorRef<ServerMessages.Req> masterActorRef) {
        this(DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT, masterActorRef);
    }

    @Override
    public void initChannelPipeline(ChannelPipeline p) {

        p.addLast(new HttpRequestDecoder());
        p.addLast(new HttpResponseEncoder());
        p.addLast(new ReadTimeoutHandler(timeout, timeUnit));
        p.addLast(new DssRestHandler(masterActorRef));
    }

    @Override
    public void initSslChannelPipeline(ChannelPipeline p) {
        // do nothing now.
    }
}
