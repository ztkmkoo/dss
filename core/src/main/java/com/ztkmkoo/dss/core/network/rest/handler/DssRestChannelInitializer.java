package com.ztkmkoo.dss.core.network.rest.handler;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import com.ztkmkoo.dss.core.actor.DssUserActor;
import com.ztkmkoo.dss.core.actor.exception.DssUserActorDuplicateBehaviorCreateException;
import com.ztkmkoo.dss.core.message.rest.DssRestChannelInitializerCommand;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 2. 오전 12:46
 */
public class DssRestChannelInitializer extends ChannelInitializer<SocketChannel> implements DssUserActor<DssRestChannelInitializerCommand> {

    private final AtomicBoolean initializeBehavior = new AtomicBoolean(false);

    @Override
    public Behavior<DssRestChannelInitializerCommand> create() {

        if (initializeBehavior.get()) {
            throw new DssUserActorDuplicateBehaviorCreateException("Cannot setup twice for one object");
        }

        initializeBehavior.set(true);
        return Behaviors.setup(this::dssRestChannelInitializer);
    }

    private Behavior<DssRestChannelInitializerCommand> dssRestChannelInitializer(ActorContext<DssRestChannelInitializerCommand> context) {

        return Behaviors
                .receive(DssRestChannelInitializerCommand.class)
                .build();
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        final ChannelPipeline p = ch.pipeline();
        p.addLast(new HttpRequestDecoder());
        p.addLast(new HttpResponseEncoder());
    }
}
