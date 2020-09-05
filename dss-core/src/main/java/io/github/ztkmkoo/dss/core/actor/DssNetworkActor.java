package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssMasterCommand;
import io.github.ztkmkoo.dss.core.message.DssNetworkCommand;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.network.DssChannel;
import io.github.ztkmkoo.dss.core.network.DssChannelProperty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-24 01:46
 */
public interface DssNetworkActor<P extends DssChannelProperty, C extends DssChannel<P, ChannelInitializer<S>>, S extends SocketChannel> extends DssActor<DssNetworkCommand> {

    /**
     * Get actor ref of master actor(parent)
     */
    ActorRef<DssMasterCommand> getMasterActor();

    /**
     * Get actor ref of resolver actor
     */
    ActorRef<DssResolverCommand> getResolverActor();

    /**
     * Get DssChannel for binding
     */
    C getChannel();

    /**
     * Get ChannelInitializer for binding netty channel
     */
    ChannelInitializer<S> getChannelInitializer();

    /**
     * Get boss event loop group to make netty server bootstrap
     */
    EventLoopGroup getBossGroup();

    /**
     * Get worker event loop group to make netty server bootstrap
     */
    EventLoopGroup getWorkerGroup();

    /**
     * Future of when netty channel is closed
     */
    ChannelFutureListener closeFuture();

    static void shutdownEventLoopGroup(Logger logger, EventLoopGroup group, String name) {
        if (Objects.nonNull(group)) {
            if (Objects.nonNull(logger)) {
                logger.info("Try to shutdown EventLoopGroup: {}", name);
            }
            group.shutdownGracefully();
            if (Objects.nonNull(logger)) {
                logger.info("Success to shutdown EventLoopGroup: {}", name);
            }
        }
    }

    default ServerBootstrap serverBootstrap() {
        return new ServerBootstrap()
                .group(getBossGroup(), getWorkerGroup());
    }

    default Channel bind(P property) throws InterruptedException {
        final Channel nettyChannel =  getChannel().bind(serverBootstrap(), property, getChannelInitializer());
        nettyChannel.closeFuture().addListener(closeFuture());

        return nettyChannel;
    }

}
