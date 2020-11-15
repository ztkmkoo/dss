package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.actor.enumeration.DssMasterActorStatus;
import io.github.ztkmkoo.dss.core.message.DssMasterCommand;
import io.github.ztkmkoo.dss.core.message.DssNetworkCommand;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.network.DssNetworkChannel;
import io.github.ztkmkoo.dss.core.network.DssNetworkChannelBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-24 01:46
 */
public class DssNetworkActor extends AbstractDssActor<DssNetworkCommand> implements DssActor<DssNetworkCommand>, DssMasterAcceptable, DssResolverAcceptable {

    public static Behavior<DssNetworkCommand> create(DssNetworkChannelBuilder channelBuilder) {
        return Behaviors.setup(context -> new DssNetworkActor(context, channelBuilder));
    }

    @Getter @Setter
    private ActorRef<DssMasterCommand> masterActor;

    @Getter @Setter
    private ActorRef<DssResolverCommand> resolverActor;

    @Getter @Setter
    private DssNetworkChannel networkChannel;

    private DssNetworkActor(ActorContext<DssNetworkCommand> context, DssNetworkChannelBuilder channelBuilder) {
        super(context);
        this.networkChannel = channelBuilder.build();
    }

    @Override
    public Receive<DssNetworkCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(DssNetworkCommand.ConfigMasterActor.class, this::handlingConfigMasterActor)
                .onMessage(DssNetworkCommand.ConfigResolverActor.class, this::handlingConfigResolverActor)
                .onMessage(DssNetworkCommand.Bind.class, this::handlingBind)
                .onMessage(DssNetworkCommand.Close.class, this::handlingClose)
                .build();
    }

    public Behavior<DssNetworkCommand> handlingConfigMasterActor(DssNetworkCommand.ConfigMasterActor msg) {
        if (Objects.nonNull(msg) && Objects.nonNull(msg.getMasterActor())) {
            getLog().error("Config master actor in NetworkActor: {}", msg.getMasterActor().path());
            setMasterActor(msg.getMasterActor());
        }
        return Behaviors.same();
    }

    public Behavior<DssNetworkCommand> handlingConfigResolverActor(DssNetworkCommand.ConfigResolverActor msg) {
        if (Objects.nonNull(msg) && Objects.nonNull(msg.getResolverActor())) {
            getLog().error("Config resolver actor in NetworkActor: {}", msg.getResolverActor().path());
            setResolverActor(msg.getResolverActor());
        }
        return Behaviors.same();
    }

    public Behavior<DssNetworkCommand> handlingBind(DssNetworkCommand.Bind msg) {
        if (Objects.isNull(networkChannel)) {
            getLog().error("Cannot find DssNetworkChannel: {}", msg);
            return Behaviors.same();
        }

        if (networkChannel.getActive()) {
            getLog().warn("Network actor is already binding: {}", msg);
            return Behaviors.same();
        }

        try {
            networkChannel.bind(msg, getContext().getSelf(), getResolverActor());
            afterBindSuccessful();
        } catch (Exception e) {
            getLog().error("Bind error.", e);
            afterBindFailed(masterActor);
        }

        return Behaviors.same();
    }

    private void afterBindSuccessful() {
        Objects.requireNonNull(masterActor);
        networkChannel.setActive(true);
        masterActor.tell(DssMasterCommand.StatusUpdate.builder().status(DssMasterActorStatus.START).build());
    }

    private void afterBindFailed(ActorRef<DssMasterCommand> masterActor) {
        Objects.requireNonNull(masterActor);
        networkChannel.setActive(false);
        masterActor.tell(DssMasterCommand.StatusUpdate.builder().status(DssMasterActorStatus.PENDING).build());

        networkChannel.close();
        masterActor.tell(DssMasterCommand.StatusUpdate.builder().status(DssMasterActorStatus.SHUTDOWN).build());
    }

    public Behavior<DssNetworkCommand> handlingClose(DssNetworkCommand.Close msg) {
        Objects.requireNonNull(msg);

        if (Objects.isNull(networkChannel)) {
            getLog().error("Cannot find DssNetworkChannel: {}", msg);
            return Behaviors.same();
        }

        networkChannel.close();

        Objects.requireNonNull(getMasterActor()).tell(DssMasterCommand.StatusUpdate.builder().status(DssMasterActorStatus.SHUTDOWN).build());

        return Behaviors.same();
    }
}
