package com.ztkmkoo.dss.core.actor.rest;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import com.ztkmkoo.dss.core.message.rest.DssRestChannelHandlerCommandResponse;
import com.ztkmkoo.dss.core.message.rest.DssRestMasterActorCommand;
import com.ztkmkoo.dss.core.message.rest.DssRestMasterActorCommandRequest;

import java.util.List;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 10:06
 */
public class DssRestMasterActor {

    public static Behavior<DssRestMasterActorCommand> create(List<DssRestActorService> serviceList) {
        return Behaviors.setup(context -> new DssRestMasterActor(context, serviceList).dssRestMasterActor());
    }

    private final ActorContext<DssRestMasterActorCommand> context;

    private DssRestMasterActor(ActorContext<DssRestMasterActorCommand> context, List<DssRestActorService> serviceList) {
        this.context = context;
        serviceList.forEach(dssRestActorService -> {});
    }

    private Behavior<DssRestMasterActorCommand> dssRestMasterActor() {
        return Behaviors
                .receive(DssRestMasterActorCommand.class)
                .onMessage(DssRestMasterActorCommandRequest.class, this::handlingDssRestMasterActorCommandRequest)
                .build();
    }

    private Behavior<DssRestMasterActorCommand> handlingDssRestMasterActorCommandRequest(DssRestMasterActorCommandRequest request) {

        context.getLog().info("DssRestMasterActorCommandRequest: {}", request);

        request.getSender().tell(DssRestChannelHandlerCommandResponse.builder().channelId(request.getChannelId()).build());

        return Behaviors.same();
    }
}
