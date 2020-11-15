package io.github.ztkmkoo.dss.core.network;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssNetworkCommand;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import org.slf4j.Logger;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-10-26 08:15
 */
public interface DssNetworkChannel {

    void bind(DssNetworkCommand.Bind msg, ActorRef<DssNetworkCommand> networkActor, ActorRef<DssResolverCommand> resolverActor);

    void close();

    Logger getLog();

    boolean getActive();

    void setActive(boolean active);
}
