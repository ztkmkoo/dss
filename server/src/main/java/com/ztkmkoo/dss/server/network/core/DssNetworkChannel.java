package com.ztkmkoo.dss.server.network.core;

import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.server.message.ServerMessages;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 17. 오후 10:32
 */
public interface DssNetworkChannel {

    Channel bind(ServerBootstrap b, DssNetworkChannelProperty property, ActorRef<ServerMessages.Req> requestActorRef) throws InterruptedException;
}
