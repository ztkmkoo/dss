package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import io.github.ztkmkoo.dss.core.actor.enumeration.DssMasterActorStatus;
import io.github.ztkmkoo.dss.core.message.DssCommand;
import io.github.ztkmkoo.dss.core.message.DssMasterCommand;
import io.github.ztkmkoo.dss.core.message.DssNetworkCommand;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;

import java.util.Objects;

/**
 * Master actor control all of its child actor(network, resolver, etc..)
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-24 01:59
 */
public interface DssMasterActor extends DssActor<DssMasterCommand> {

    /**
     * Get actor ref of resolver actor
     */
    ActorRef<DssResolverCommand> getResolverActor();

    /**
     * Get actor ref of network actor
     */
    ActorRef<DssNetworkCommand> getNetworkActor();

    /**
     * Get master actor properties
     */
    DssMasterActorProperty getProperty();

    /**
     * Create resolver actor method
     */
    Behavior<DssResolverCommand> createResolverActorBehavior();

    /**
     * Create network actor method
     */
    Behavior<DssNetworkCommand> createNetworkActorBehavior();

    /**
     * Get current master actor status
     */
    DssMasterActorStatus getMasterActorStatus();

    void setMasterActorStatus(DssMasterActorStatus status);

    default ActorRef<DssResolverCommand> spawnResolverActor(AbstractDssActor<DssMasterCommand> actor) {
        final Behavior<DssResolverCommand> resolverBehavior = createResolverActorBehavior();
        Objects.requireNonNull(resolverBehavior);

        final ActorRef<DssResolverCommand> resolverActor = actor.getContext().spawn(resolverBehavior, "resolver");
        return Objects.requireNonNull(resolverActor);
    }

    default ActorRef<DssNetworkCommand> spawnNetworkActor(AbstractDssActor<DssMasterCommand> actor) {
        final Behavior<DssNetworkCommand> networkBehavior = createNetworkActorBehavior();
        Objects.requireNonNull(networkBehavior);

        final ActorRef<DssNetworkCommand> networkActor = actor.getContext().spawn(networkBehavior, "network");
        return Objects.requireNonNull(networkActor);
    }

    default Behavior<DssMasterCommand> handlingStatusRequest(DssMasterCommand.StatusRequest msg) {
        Objects.requireNonNull(msg);
        Objects.requireNonNull(getMasterActorStatus());

        getLog().debug("Receive: {}", msg);

        final ActorRef<DssCommand> sender = msg.getSender();
        if (Objects.nonNull(sender)) {
            sender.tell(DssMasterCommand.StatusResponse
                    .builder()
                    .status(getMasterActorStatus())
                    .build());
        } else {
            getLog().warn("Sender is null. Skip sending response: {}", msg);
        }

        return getBehavior();
    }

    default Behavior<DssMasterCommand> handlingStatusUpdate(DssMasterCommand.StatusUpdate msg) {
        Objects.requireNonNull(msg);
        Objects.requireNonNull(getMasterActorStatus());

        getLog().debug("Receive: {}", msg);

        final DssMasterActorStatus origin = getMasterActorStatus();
        final DssMasterActorStatus status = msg.getStatus();

        if (Objects.nonNull(status)) {
            setMasterActorStatus(status);
            getLog().debug("Update master actor status {} -> {}", origin, status);
        } else {
            getLog().warn("Status is null. Skip updating status: {}", msg);
        }

        return getBehavior();
    }
}
