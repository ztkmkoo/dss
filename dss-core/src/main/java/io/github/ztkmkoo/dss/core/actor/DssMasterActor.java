package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.rest.DssNetworkActorRestImpl;
import io.github.ztkmkoo.dss.core.actor.rest.DssResolverActorRestImpl;
import io.github.ztkmkoo.dss.core.message.DssMasterCommand;
import io.github.ztkmkoo.dss.core.message.DssNetworkCommand;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-24 01:59
 */
public interface DssMasterActor {

    ActorRef<DssResolverCommand> getResolverActor();

    ActorRef<DssNetworkCommand> getNetworkActor();

    DssMasterActorProperty getProperty();

    default ActorRef<DssResolverCommand> spawnResolverActor(AbstractDssActor<DssMasterCommand> actor) {
        final ActorRef<DssResolverCommand> resolverActor = actor.getContext().spawn(DssResolverActorRestImpl.create(actor.getSelf()), "resolver");
        return Objects.requireNonNull(resolverActor);
    }

    default ActorRef<DssNetworkCommand> spawnNetworkActor(AbstractDssActor<DssMasterCommand> actor, ActorRef<DssResolverCommand> resolverActor) {
        final ActorRef<DssNetworkCommand> networkActor = actor.getContext().spawn(DssNetworkActorRestImpl.create(actor.getSelf(), resolverActor), "network");
        return Objects.requireNonNull(networkActor);
    }
}
