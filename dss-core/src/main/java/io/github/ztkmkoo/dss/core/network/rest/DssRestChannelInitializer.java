package io.github.ztkmkoo.dss.core.network.rest;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.network.DssChannelInitializer;
import io.github.ztkmkoo.dss.core.network.rest.property.DssRestChannelInitializerProperty;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-10-17 06:14
 */
@SuppressWarnings("java:S3740")
public interface DssRestChannelInitializer extends DssChannelInitializer {

    static ChannelInitializer<SocketChannel> createChannelInitializer(ActorRef<DssResolverCommand> resolverActor, DssRestChannelInitializerProperty property) throws InterruptedException {
        Objects.requireNonNull(resolverActor, "resolverActor cannot be null");
        return property.isSsl() ?
                new DssDefaultRestChannelInitializer(resolverActor) :
                new DssDefaultRestSslChannelInitializer(resolverActor, property);
    }
}
