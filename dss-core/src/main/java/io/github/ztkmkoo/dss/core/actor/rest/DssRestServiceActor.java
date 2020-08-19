package io.github.ztkmkoo.dss.core.actor.rest;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import io.github.ztkmkoo.dss.core.actor.exception.DssRestRequestMappingException;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import io.github.ztkmkoo.dss.core.message.rest.DssRestChannelHandlerCommandResponse;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Objects;

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

    private Behavior<DssRestServiceActorCommand> dssRestServiceActor () {
        return Behaviors
                .receive(DssRestServiceActorCommand.class)
                .onMessage(DssRestServiceActorCommandRequest.class, this::onHandlingDssRestServiceActorCommandRequest)
                .build();
    }

    private Behavior<DssRestServiceActorCommand> onHandlingDssRestServiceActorCommandRequest
            (DssRestServiceActorCommandRequest request){
        context.getLog().info("onHandlingDssRestServiceActorCommandRequest: {}", request);
        if (Objects.isNull(request.getContentType())) {
            context.getLog().warn("Request content-type is null: {}", request);
        }

        try {
            if (dssRestActorService.getConsume().getContentType().equals(request.getContentType())) {
                final DssRestServiceResponse dssRestServiceResponse = dssRestActorService.handling(request);
                replyRequest(request, HttpResponseStatus.OK, dssRestServiceResponse);
            } else {
                context.getLog().error("Cannot handling request for unexpected content-type. Expected: {}, but: {} ",
                        dssRestActorService.getConsume().getContentType(), request.getContentType());
                replyRequest(request, HttpResponseStatus.BAD_REQUEST);
            }
        } catch (DssRestRequestMappingException e) {
            context.getLog().error("Json request mapping error: ", e);
            replyRequest(request, HttpResponseStatus.BAD_REQUEST);
        } catch (Exception e) {
            context.getLog().error("Handling rest request error: ", e);
            replyRequest(request, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }

        return Behaviors.same();
    }

    private void replyRequest (DssRestServiceActorCommandRequest request, HttpResponseStatus status, DssRestServiceResponse response){
        Objects.requireNonNull(request);
        request
                .getSender()
                .tell(
                        DssRestChannelHandlerCommandResponse
                                .builder()
                                .channelId(request.getChannelId())
                                .status(status.code())
                                .response(response)
                                .build()
                );
    }

    private void replyRequest (DssRestServiceActorCommandRequest request, HttpResponseStatus status){
        replyRequest(request, status, null);
    }
}
