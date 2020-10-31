package io.github.ztkmkoo.dss.core.network.rest;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssNetworkCommand;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.network.AbstractDssNettyNetworkChannel;
import io.github.ztkmkoo.dss.core.network.DssChannelProperty;
import io.github.ztkmkoo.dss.core.network.rest.property.DssRestChannelProperty;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-10-26 22:48
 */
public class DssRestNetworkChannel extends AbstractDssNettyNetworkChannel<SocketChannel> {

    private final Logger logger = LoggerFactory.getLogger(DssRestNetworkChannel.class);

    public DssRestNetworkChannel(int bossThread, int workerThread) {
        super(bossThread, workerThread);
    }

    public DssRestNetworkChannel() {
        this(0, 0);
    }

    @Override
    public ChannelInitializer<SocketChannel> newChannelInitializer(ActorRef<DssResolverCommand> resolverActor) throws InterruptedException {
        return new DssDefaultRestChannelInitializer(resolverActor);
    }

    @Override
    public DssChannelProperty convertToChannelProperty(DssNetworkCommand.Bind msg) {
        return DssRestChannelProperty
                .builder()
                .host(msg.getHost())
                .port(msg.getPort())
                .dssLogLevel(msg.getLogLevel())
                .build();
    }

    @Override
    public Logger getLog() {
        return logger;
    }
}
