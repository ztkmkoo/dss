package io.github.ztkmkoo.dss.core.actor.rest;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActor;
import io.github.ztkmkoo.dss.core.actor.DssNetworkActor;
import io.github.ztkmkoo.dss.core.common.security.SslContextUtils;
import io.github.ztkmkoo.dss.core.message.DssMasterCommand;
import io.github.ztkmkoo.dss.core.message.DssNetworkCommand;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.network.rest.DssRestChannelInitializer;
import io.github.ztkmkoo.dss.core.network.rest.property.DssRestChannelInitializerProperty;
import io.github.ztkmkoo.dss.core.network.rest.property.DssRestChannelProperty;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-10-17 05:54
 */
public class DssRestNetworkActor extends AbstractDssActor<DssNetworkCommand> implements DssNetworkActor<DssRestChannelProperty, SocketChannel> {

    public static Behavior<DssNetworkCommand> create() {
        return Behaviors.setup(DssRestNetworkActor::new);
    }

    @Getter @Setter
    private EventLoopGroup bossGroup;

    @Getter @Setter
    private EventLoopGroup workerGroup;

    @Setter
    private boolean active;

    @Getter @Setter
    private ActorRef<DssMasterCommand> masterActor;

    @Getter @Setter
    private ActorRef<DssResolverCommand> resolverActor;

    private DssRestChannelInitializerProperty channelInitializerProperty = new DssRestChannelInitializerProperty();

    private DssRestNetworkActor(ActorContext<DssNetworkCommand> context) {
        super(context);
    }

    @Override
    public Receive<DssNetworkCommand> createReceive() {
        return networkReceiveBuilder(this)
                .build();
    }

    @Override
    public DssRestChannelProperty convertToChannelProperty(DssNetworkCommand.Bind msg) {
        return DssRestChannelProperty
                .builder()
                .host(msg.getHost())
                .port(msg.getPort())
                .dssLogLevel(msg.getLogLevel())
                .build();
    }

    @Override
    public ChannelInitializer<SocketChannel> getChannelInitializer() throws InterruptedException {
        return DssRestChannelInitializer.createChannelInitializer(resolverActor, channelInitializerProperty);
    }

    @Override
    public boolean getActive() {
        return active;
    }

    @Override
    public void configExtraHandlingBind(DssNetworkCommand.Bind msg) {
        final boolean ssl = msg.isSsl();
        final SslContext sslContext = loadSslContext(ssl, msg.getPrivateKeyPath(), msg.getCertificatePath(), msg.getPassword());
        this.channelInitializerProperty = new DssRestChannelInitializerProperty(ssl, sslContext);
    }

    private SslContext loadSslContext(boolean ssl, String privateKeyPath, String certificatePath, String password) {
        if (!ssl) {
            return null;
        }

        return SslContextUtils.sslContextForServer(privateKeyPath, certificatePath, password);
    }
}
