package com.ztkmkoo.dss.server.network.core.creator;

import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.server.message.ServerMessages;
import io.netty.channel.ChannelHandler;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 *
 * For creating netty handler
 * It was for lambda expression, Do not make additional method
 */
@Deprecated
public interface DssServerHandlerCreator<T extends ServerMessages.Req> {

    ChannelHandler createChannelHandler(ActorRef<T> masterActor);
}
