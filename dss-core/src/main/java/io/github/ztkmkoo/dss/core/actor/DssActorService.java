package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.javadsl.ActorContext;
import io.github.ztkmkoo.dss.core.actor.blocking.DssBlockingServiceResolver;
import io.github.ztkmkoo.dss.core.message.DssCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-01 21:53
 */
public interface DssActorService<M extends DssCommand> {

    String getName();

    void finish(M res);

    void initializeFromActor(ActorContext<M> context);

    void setBlockingServiceResolver(DssBlockingServiceResolver resolver);
}
