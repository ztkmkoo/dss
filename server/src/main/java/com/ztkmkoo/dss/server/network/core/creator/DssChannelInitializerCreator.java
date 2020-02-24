package com.ztkmkoo.dss.server.network.core.creator;

import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.server.message.ServerMessages;
import com.ztkmkoo.dss.server.network.core.handler.DssChannelInitializer;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 25. 오전 12:14
 */
public interface DssChannelInitializerCreator {

    DssChannelInitializer newDssChannelInitializer(ActorRef<ServerMessages.Req> masterActor);
}
