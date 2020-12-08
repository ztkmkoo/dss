package io.github.ztkmkoo.dss.core.actor.rest;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.LogOptions;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActor;
import io.github.ztkmkoo.dss.core.actor.DssResolverActor;
import io.github.ztkmkoo.dss.core.actor.DssServiceActorResolvable;
import io.github.ztkmkoo.dss.core.message.DssMasterCommand;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestResolverCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceCommand;
import io.github.ztkmkoo.dss.core.network.DssChannelHandlerContext;
import io.github.ztkmkoo.dss.core.network.rest.entity.DssDefaultRestResponse;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.event.Level;

import java.util.*;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-11-28 15:01
 */
public class DssRestResolverActor extends AbstractDssActor<DssResolverCommand> implements DssResolverActor {

    public static Behavior<DssResolverCommand> create() {
        return DssRestResolverActor.create(Collections.emptyList());
    }

    public static Behavior<DssResolverCommand> create(List<DssServiceActorResolvable<String>> serviceActorList) {
        return Behaviors.logMessages(
                LogOptions.create().withLevel(Level.DEBUG),
                Behaviors.setup(context -> new DssRestResolverActor(context, serviceActorList))
        );
    }

    @Getter
    private final Map<String, DssServiceActorResolvable<String>> serviceActorMap;
    private final Map<String, DssChannelHandlerContext> channelHandlerContextMap = new HashMap<>();

    @Getter @Setter
    private ActorRef<DssMasterCommand> masterActor;

    private DssRestResolverActor(ActorContext<DssResolverCommand> context, List<DssServiceActorResolvable<String>> serviceActorList) {
        super(context);

        Objects.requireNonNull(serviceActorList);
        this.serviceActorMap = new HashMap<>(serviceActorList.size());
        initServiceActorMap(serviceActorList);
    }

    private void initServiceActorMap(List<DssServiceActorResolvable<String>> serviceActorList) {
        for (DssServiceActorResolvable<String> serviceActor : serviceActorList) {
            serviceActorMap.put(serviceActor.getKey(), serviceActor);
        }
    }

    @Override
    public Receive<DssResolverCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(DssRestResolverCommand.RestRequest.class, this::handlingRestRequest)
                .onMessage(DssRestResolverCommand.RestResponse.class, this::handlingRestResponse)
                .build();
    }

    private Behavior<DssResolverCommand> handlingRestRequest(DssRestResolverCommand.RestRequest msg) {
        final String path = msg.getPath();
        if (serviceActorMap.containsKey(path)) {
            final DssChannelHandlerContext context = msg.getCtx();
            channelHandlerContextMap.put(context.getChannelId(), context);

            final DssServiceActorResolvable<String> resolvable = serviceActorMap.get(path);
            final DssRestServiceCommand.RestRequest request = DssRestServiceCommand.RestRequest
                    .builder(msg, context.getChannelId(), getSelf())
                    .build();
            resolvable.getActorRef().tell(request);
        } else {
            msg.getCtx().writeAndFlush(DssDefaultRestResponse.getHTTP_NOT_FOUND());
        }
        return Behaviors.same();
    }

    private Behavior<DssResolverCommand> handlingRestResponse(DssRestResolverCommand.RestResponse msg) {
        if (channelHandlerContextMap.containsKey(msg.getChannelId())) {
            final DssChannelHandlerContext context = channelHandlerContextMap.remove(msg.getChannelId());
            final HttpResponse response = makeResponse(HttpResponseStatus.valueOf(msg.getStatus()), msg.getBody());

             context.writeAndFlush(response);
        } else {
            getLog().error("Invalid channel id: {}", msg.getChannelId());
        }

        return Behaviors.same();
    }

    @Override
    public void putServiceActorResolvable(String key, DssServiceActorResolvable<String> value) {
        serviceActorMap.put(key, value);
    }

    private static HttpResponse makeResponse(HttpResponseStatus status, String content) {
        Objects.requireNonNull(status);
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
    }
}
