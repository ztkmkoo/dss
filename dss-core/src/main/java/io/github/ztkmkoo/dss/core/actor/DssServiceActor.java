package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.message.DssMasterCommand;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.message.DssServiceCommand;
import io.github.ztkmkoo.dss.core.service.AbstractDssService;
import io.github.ztkmkoo.dss.core.service.DssServiceBuilder;
import io.github.ztkmkoo.dss.core.service.DssServiceRequest;
import io.github.ztkmkoo.dss.core.service.DssServiceResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 03:11
 */
public class DssServiceActor <R1 extends DssServiceCommand.DssServiceRequestCommand, S1 extends DssResolverCommand.DssServiceResponseCommand, R2 extends DssServiceRequest, S2 extends DssServiceResponse> extends AbstractDssActor<DssServiceCommand> implements DssActor<DssServiceCommand>, DssMasterAcceptable {

    public static <R1 extends DssServiceCommand.DssServiceRequestCommand, S1 extends DssResolverCommand.DssServiceResponseCommand, R2 extends DssServiceRequest, S2 extends DssServiceResponse> Behavior<DssServiceCommand> create(
            DssServiceBuilder<R1, S1, R2, S2> serviceBuilder, Class<R1> requestCommandType) {
        return Behaviors.setup(context -> new DssServiceActor<>(context, serviceBuilder, requestCommandType));
    }

    private final AbstractDssService<R1, S1, R2, S2> service;
    private final Class<R1> requestCommandType;

    @Getter @Setter
    private ActorRef<DssMasterCommand> masterActor;

    private DssServiceActor(ActorContext<DssServiceCommand> context, DssServiceBuilder<R1, S1, R2, S2> serviceBuilder, Class<R1> requestCommandType) {
        super(context);
        this.service = serviceBuilder.create(getSelf());
        this.requestCommandType = requestCommandType;
    }

    @Override
    public Receive<DssServiceCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(requestCommandType, this::handlingDssServiceRequestCommand)
                .build();
    }

    private Behavior<DssServiceCommand> handlingDssServiceRequestCommand(R1 msg) {
        service.start(msg);
        return Behaviors.same();
    }
}
