package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ReceiveBuilder;
import io.github.ztkmkoo.dss.core.actor.enumeration.DssMasterActorStatus;
import io.github.ztkmkoo.dss.core.actor.property.DssMasterActorProperty;
import io.github.ztkmkoo.dss.core.message.DssCommand;
import io.github.ztkmkoo.dss.core.message.DssMasterCommand;

import java.util.Objects;

/**
 * Master actor control all of its child actor(network, resolver, etc..)
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-24 01:59
 */
public interface DssMasterActor extends DssActor<DssMasterCommand>, DssExceptionSpawnable, DssServiceSpawnable, DssResolverSpawnable, DssNetworkSpawnable {

    /**
     * Get master property
     */
    DssMasterActorProperty getDssMasterActorProperty();

    /**
     * Get current master actor status
     */
    DssMasterActorStatus getMasterActorStatus();

    /**
     * Set current master actor status
     */
    void setMasterActorStatus(DssMasterActorStatus status);

    /**
     * help spawn child exception, service, resolver, network actor
     * do not change the spawn order
     */
    default void initializeMasterActor(AbstractDssActor<DssMasterCommand> master) {
        Objects.requireNonNull(master);
        final DssMasterActorProperty masterProperty = getDssMasterActorProperty();
        Objects.requireNonNull(masterProperty);

        initializeExceptionActor(master, masterProperty);
        initializeServiceActor(master, masterProperty);
        initializeResolverActor(master, masterProperty);
        initializeNetworkActor(master, masterProperty);
    }

    /**
     * make ReceiveBuilder for master actor with common handling message
     */
    default ReceiveBuilder<DssMasterCommand> masterReceiveBuilder(AbstractDssActor<DssMasterCommand> actor) {
        return actor.newReceiveBuilder()
                .onMessage(DssMasterCommand.StatusRequest.class, this::handlingStatusRequest)
                .onMessage(DssMasterCommand.StatusUpdate.class, this::handlingStatusUpdate)
                ;
    }

    /**
     * handling DssMasterCommand.StatusRequest
     * response that current actor status
     */
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

    /**
     * handling DssMasterCommand.StatusUpdate
     * update current status with request
     */
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