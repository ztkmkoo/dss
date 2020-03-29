package com.ztkmkoo.dss.core.actor.rest;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import com.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import com.ztkmkoo.dss.core.message.rest.DssRestChannelHandlerCommandResponse;
import com.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommand;
import com.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 14. 오후 9:39
 */
public class DssRestServiceActor {

    public static Behavior<DssRestServiceActorCommand> create(DssRestActorService dssRestActorService) {
        return Behaviors.setup(context -> new DssRestServiceActor(context, dssRestActorService).dssRestServiceActor());
    }

    private final ActorContext<DssRestServiceActorCommand> context;
    private final DssRestActorService dssRestActorService;

    private DssRestServiceActor(ActorContext<DssRestServiceActorCommand> context, DssRestActorService dssRestActorService) {
        this.context = context;
        this.dssRestActorService = dssRestActorService;
    }

    private Behavior<DssRestServiceActorCommand> dssRestServiceActor() {
        return Behaviors
                .receive(DssRestServiceActorCommand.class)
                .onMessage(DssRestServiceActorCommandRequest.class, this::onHandlingDssRestServiceActorCommandRequest)
                .build();
    }

    private Behavior<DssRestServiceActorCommand> onHandlingDssRestServiceActorCommandRequest(DssRestServiceActorCommandRequest request) {
        context.getLog().info("onHandlingDssRestServiceActorCommandRequest: {}", request);

        final DssRestServiceRequest dssRestServiceRequest = dssRestActorService.convertRequest(request);
        final DssRestServiceResponse dssRestServiceResponse = dssRestActorService.handling(dssRestServiceRequest);

        request
                .getSender()
                .tell(
                        DssRestChannelHandlerCommandResponse
                                .builder()
                                .channelId(request.getChannelId())
                                .response(dssRestServiceResponse)
                                .build()
                );

        return Behaviors.same();
    }
}
