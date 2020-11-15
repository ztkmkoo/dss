package io.github.ztkmkoo.dss.core.network;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.enumeration.DssNetworkCloseStatus;
import io.github.ztkmkoo.dss.core.exception.network.DssNetworkChannelException;
import io.github.ztkmkoo.dss.core.message.DssNetworkCommand;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.logging.LoggingHandler;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-11-01 02:16
 */
public abstract class AbstractDssNettyNetworkChannel<S extends Channel> implements DssNetworkChannel {

    public abstract ChannelInitializer<S> newChannelInitializer(ActorRef<DssResolverCommand> resolverActor) throws InterruptedException;

    public abstract DssChannelProperty convertToChannelProperty(DssNetworkCommand.Bind msg);

    @Getter @Setter
    private EventLoopGroup bossGroup;
    @Getter @Setter
    private EventLoopGroup workerGroup;
    @Setter
    private boolean active = false;

    private Channel channel;

    public AbstractDssNettyNetworkChannel(int bossThread, int workerThread) {
        this.bossGroup = new NioEventLoopGroup(bossThread);
        this.workerGroup = new NioEventLoopGroup(workerThread);
    }

    @Override
    public void bind(DssNetworkCommand.Bind msg, ActorRef<DssNetworkCommand> networkActor, ActorRef<DssResolverCommand> resolverActor) {
        if (Objects.nonNull(channel)) {
            close();
            throw new DssNetworkChannelException("Channel is not null");
        }

        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(getBossGroup(), getWorkerGroup());
        final DssChannelProperty property = convertToChannelProperty(msg);
        configExtraHandlingBind(msg);

        try {
            channel = bind(bootstrap, property, newChannelInitializer(resolverActor));
            channel.closeFuture().addListeners(closeFuture(networkActor));
        } catch (Exception e) {
            getLog().error("Network actor bind error, ", e);
        }
    }

    protected Channel bind(ServerBootstrap serverBootstrap, DssChannelProperty property, ChannelInitializer<S> channelInitializer) throws InterruptedException {
        Objects.requireNonNull(serverBootstrap);
        Objects.requireNonNull(property);
        Objects.requireNonNull(property.getServerSocketClass());
        Objects.requireNonNull(channelInitializer);

        return serverBootstrap
                .channel(property.getServerSocketClass())
                .handler(new LoggingHandler(property.getDssLogLevel().getNettyLogLevel()))
                .childHandler(channelInitializer)
                .bind(property.getHost(), property.getPort())
                .sync()
                .channel();
    }

    /**
     * Future of when netty channel is closed
     */
    protected ChannelFutureListener closeFuture(ActorRef<DssNetworkCommand> networkActor) {
        return future -> {
            if(Objects.nonNull(future)) {
                DssNetworkCloseStatus status = DssNetworkCloseStatus.from(future.isDone(), future.isSuccess(), future.isCancelled(), future.cause());
                switch (status) {
                    case CANCELLED:
                        // close cancelled
                        return;
                    case SUCCESSFULLY:
                    case FAILED:
                    case UNCOMPLETED:
                    default:
                        networkActor.tell(DssNetworkCommand.Close.INST);
                }
            }
        };
    }

    protected void configExtraHandlingBind(DssNetworkCommand.Bind msg) {
        // do nothing
    }

    @SuppressWarnings("java:S2142")
    @Override
    public void close() {
        if (Objects.nonNull(channel)) {
            try {
                channel.close().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
                channel = null;
            }
        }
    }

    @Override
    public boolean getActive() {
        return active;
    }
}
