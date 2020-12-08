package io.github.ztkmkoo.dss.core.network.rest;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-10-22 07:12
 */
public class DssDefaultRestChannelInitializer extends ChannelInitializer<SocketChannel> implements DssRestChannelInitializer {

    @Getter
    @Setter
    private ActorRef<DssResolverCommand> resolverActor;

    public DssDefaultRestChannelInitializer(ActorRef<DssResolverCommand> resolverActor) {
        this.resolverActor = Objects.requireNonNull(resolverActor);
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        final ChannelPipeline p = ch.pipeline();
        initRestChannel(p);
    }

    protected void initRestChannel(ChannelPipeline p) {
        p.addLast(new HttpRequestDecoder());
        p.addLast(new HttpResponseEncoder());
        p.addLast(new DssDefaultRestChannelHandler(resolverActor));
    }
}
