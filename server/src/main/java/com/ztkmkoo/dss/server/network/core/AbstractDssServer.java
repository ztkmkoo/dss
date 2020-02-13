package com.ztkmkoo.dss.server.network.core;

import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.server.message.ServerMessages;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 *
 * Network abstract server module.
 */
public abstract class AbstractDssServer implements DssServer {

    protected final Logger logger;

    protected AbstractDssServer() {

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    protected abstract ServerBootstrap configServerBootstrap(DssServerProperty p, ServerBootstrap b, ActorRef<ServerMessages.Req> masterActor) throws InterruptedException;

    @Override
    public Channel bind(ServerBootstrap b, DssServerProperty property, ActorRef<ServerMessages.Req> masterActor) throws InterruptedException {

        Objects.requireNonNull(b, "ServerBootstrap is null");
        Objects.requireNonNull(property, "DssServerProperty is null");

        return configServerBootstrap(property, b, masterActor)
                .bind(property.getHost(), property.getPort())
                .sync()
                .channel();
    }
}
