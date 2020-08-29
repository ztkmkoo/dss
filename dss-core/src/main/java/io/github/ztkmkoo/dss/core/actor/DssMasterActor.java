package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
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

    Behavior<DssResolverCommand> createResolverActorBehavior();

    Behavior<DssNetworkCommand> createNetworkActorBehavior();

    default ActorRef<DssResolverCommand> spawnResolverActor(AbstractDssActor<DssMasterCommand> actor) {
        final ActorRef<DssResolverCommand> resolverActor = actor.getContext().spawn(createResolverActorBehavior(), "resolver");
        return Objects.requireNonNull(resolverActor);
    }

    default ActorRef<DssNetworkCommand> spawnNetworkActor(AbstractDssActor<DssMasterCommand> actor, ActorRef<DssResolverCommand> resolverActor) {
        final ActorRef<DssNetworkCommand> networkActor = actor.getContext().spawn(createNetworkActorBehavior(), "network");
        return Objects.requireNonNull(networkActor);
    }
}
