package com.ztkmkoo.dss.server.network.rest;

import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.server.message.ServerMessages;
import com.ztkmkoo.dss.server.network.core.DssNetworkChannel;
import com.ztkmkoo.dss.server.network.core.DssNetworkChannelProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 17. 오후 10:36
 */
public class DssRestChannel implements DssNetworkChannel {

    @Override
    public Channel bind(ServerBootstrap b, DssNetworkChannelProperty p, ActorRef<ServerMessages.Req> requestActorRef) throws InterruptedException {

        Objects.requireNonNull(b);
        Objects.requireNonNull(p);

        if (p instanceof DssRestChannelProperty) {

            final DssRestChannelProperty property = (DssRestChannelProperty) p;

            return b
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(property.getLogLevel().getNettyLogLevel()))
                    .childHandler(property.getDssChannelInitializerCreator().newDssChannelInitializer(requestActorRef))
                    .bind(property.getHost(), property.getPort())
                    .sync()
                    .channel();

        } else {
            throw new InterruptedException("Invalid DssNetworkChannelProperty: " + p.getClass().getName());
        }
    }
}
