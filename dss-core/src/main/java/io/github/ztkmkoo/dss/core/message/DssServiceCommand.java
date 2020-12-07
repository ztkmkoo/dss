package io.github.ztkmkoo.dss.core.message;

import akka.actor.typed.ActorRef;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 03:12
 */
public interface DssServiceCommand extends DssCommand {

    interface DssServiceRequestCommand extends DssServiceCommand {

        String getChannelId();

        ActorRef<DssResolverCommand> getSender();
    }
}
