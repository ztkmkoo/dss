package io.github.ztkmkoo.dss.core.actor.rest;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import io.github.ztkmkoo.dss.core.message.rest.*;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 14. 오후 9:39
 */
public class DssRestServiceActor {

    public static Behavior<DssRestServiceActorCommand> create(DssRestActorService dssRestActorService, ActorRef<DssRestExceptionHandlerCommand> exceptionHandler) {
        return Behaviors.setup(context -> new DssRestServiceActor(context, dssRestActorService, exceptionHandler).dssRestServiceActor());
    }

    private final ActorContext<DssRestServiceActorCommand> context;
    private final DssRestActorService dssRestActorService;
    private final ActorRef<DssRestExceptionHandlerCommand> exceptionHandler;

    private DssRestServiceActor(ActorContext<DssRestServiceActorCommand> context, DssRestActorService dssRestActorService, ActorRef<DssRestExceptionHandlerCommand> exceptionHandler) {
        this.context = context;
        this.dssRestActorService = dssRestActorService;
        this.exceptionHandler = exceptionHandler;
    }

    private Behavior<DssRestServiceActorCommand> dssRestServiceActor() {
        return Behaviors
                .receive(DssRestServiceActorCommand.class)
                .onMessage(DssRestServiceActorCommandRequest.class, this::onHandlingDssRestServiceActorCommandRequest)
                .build();
    }

    private Behavior<DssRestServiceActorCommand> onHandlingDssRestServiceActorCommandRequest(
            DssRestServiceActorCommandRequest request) {
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
        }catch (Exception e) {
            DssRestExceptionHandlerCommandRequest commandRequest = DssRestExceptionHandlerCommandRequest.builder()
                    .service(this.dssRestActorService)
                    .exception(e)
                    .request(request)
                    .build();

            this.exceptionHandler.tell(commandRequest);
        }

        return Behaviors.same();
    }

    private void replyRequest(DssRestServiceActorCommandRequest request, HttpResponseStatus status, DssRestServiceResponse response) {
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

    private void replyRequest(DssRestServiceActorCommandRequest request, HttpResponseStatus status) {
        replyRequest(request, status, null);
    }
}
