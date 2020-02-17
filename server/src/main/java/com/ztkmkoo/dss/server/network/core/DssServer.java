package com.ztkmkoo.dss.server.network.core;

import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.server.message.ServerMessages;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 *
 * Network server module interface.
 */
public interface DssServer {

    /**
     * Server binding method.
     * @param b: Netty ServerBootstrap
     * @param property: DssServerProperty pass the specific property
     * @param masterActor: Master actor handling request. (Netty handler send request to this actor)
     * @return Netty Nio Channel
     * @throws InterruptedException: from configServerBootstrap or channel future sync
     */
    Channel bind(ServerBootstrap b, DssServerProperty property, ActorRef<ServerMessages.Req> masterActor) throws InterruptedException;
}
