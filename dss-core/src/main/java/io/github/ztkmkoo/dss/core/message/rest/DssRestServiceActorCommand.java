package io.github.ztkmkoo.dss.core.message.rest;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssCommand;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 14. 오후 9:40
 */
public interface DssRestServiceActorCommand extends DssCommand {

    ActorRef<DssRestChannelHandlerCommand> getSender();
}
