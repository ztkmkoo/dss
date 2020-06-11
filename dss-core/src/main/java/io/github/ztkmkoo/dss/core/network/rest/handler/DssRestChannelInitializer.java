package io.github.ztkmkoo.dss.core.network.rest.handler;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import io.github.ztkmkoo.dss.core.actor.exception.DssUserActorDuplicateBehaviorCreateException;
import io.github.ztkmkoo.dss.core.actor.rest.DssRestMasterActor;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import io.github.ztkmkoo.dss.core.message.rest.DssRestChannelInitializerCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestChannelInitializerCommandHandlerUnregistered;
import io.github.ztkmkoo.dss.core.message.rest.DssRestMasterActorCommand;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 2. 오전 12:46
 */
public class DssRestChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final AtomicInteger handlerIndex = new AtomicInteger(0);
    private static final String HANDLER_NAME_PREFIX = "rest-handler-";
    private static final int MAX_FREE_HANDLER_SIZE = 16;
    private static final int INITIAL_FREE_HANDLER_SIZE = 2;

    private final Logger logger = LoggerFactory.getLogger(DssRestChannelInitializer.class);
    private final AtomicBoolean initializeBehavior = new AtomicBoolean(false);
    private final List<DssRestActorService> serviceList;
    private final Queue<DssRestHandler> freeHandlerQueue = new ConcurrentLinkedQueue<>();
    private final Map<String, DssRestHandler> activeRestHandlerMap = new ConcurrentHashMap<>();

    private ActorContext<DssRestChannelInitializerCommand> context;
    private ActorRef<DssRestMasterActorCommand> restMasterActorRef;


    public DssRestChannelInitializer(List<DssRestActorService> serviceList) {
        this.serviceList = new ArrayList<>(serviceList);
    }

    private static DssRestHandler newAllocatedDssRestHandler(
            ActorContext<DssRestChannelInitializerCommand> context,
            ActorRef<DssRestMasterActorCommand> restMasterActorRef) {
        final String handlerName = HANDLER_NAME_PREFIX + handlerIndex.incrementAndGet();
        context.getLog().info("freeHandlerQueue is empty. try to initialize new one: {}", handlerName);
        final DssRestHandler restHandler = new DssRestHandler(context.getSelf(), restMasterActorRef, handlerName);
        context.spawn(restHandler.create(), handlerName);

        return restHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        logger.info("Try to initChannel");

        final ChannelPipeline p = ch.pipeline();

        p.addLast(new HttpRequestDecoder());
        p.addLast(new HttpResponseEncoder());

        addHandler(p);
    }

    protected void addHandler(ChannelPipeline p) {
        logger.info("Try to addHandler");

        Objects.requireNonNull(context);

        initRestMasterActorRef();

        final DssRestHandler restHandler = getFreeDssRestHandler();

        p.addLast(restHandler);
    }

    private void initRestMasterActorRef() {
        if (Objects.isNull(restMasterActorRef)) {
            restMasterActorRef = context.spawn(DssRestMasterActor.create(serviceList), "rest-master");
        }
    }

    private DssRestHandler getFreeDssRestHandler() {
        final DssRestHandler freeHandler = freeHandlerQueue.poll();
        if (Objects.nonNull(freeHandler)) {
            return activeDssRestHandler(freeHandler);
        }

        return activeDssRestHandler(newAllocatedDssRestHandler(context, restMasterActorRef));
    }

    private DssRestHandler activeDssRestHandler(DssRestHandler handler) {
        if (Objects.nonNull(handler)) {
            activeRestHandlerMap.putIfAbsent(handler.getName(), handler);
        }
        return handler;
    }

    public Behavior<DssRestChannelInitializerCommand> create() {
        if (initializeBehavior.get()) {
            throw new DssUserActorDuplicateBehaviorCreateException("Cannot setup twice for one object");
        }

        initializeBehavior.set(true);
        return Behaviors.setup(this::dssRestChannelInitializer);
    }

    private Behavior<DssRestChannelInitializerCommand> dssRestChannelInitializer(ActorContext<DssRestChannelInitializerCommand> context) {
        this.context = context;
        context.getLog().info("Setup dssRestChannelInitializer");

        setUpHandlerQueue(INITIAL_FREE_HANDLER_SIZE);

        return Behaviors
                .receive(DssRestChannelInitializerCommand.class)
                .onMessage(DssRestChannelInitializerCommandHandlerUnregistered.class, this::onDssRestChannelInitializerCommandHandlerUnregistered)
                .build();
    }

    private Behavior<DssRestChannelInitializerCommand> onDssRestChannelInitializerCommandHandlerUnregistered(
            DssRestChannelInitializerCommandHandlerUnregistered msg) {
        context.getLog().info("onDssRestChannelInitializerCommandHandlerUnregistered: {}", msg);

        final DssRestHandler dssRestHandler = activeRestHandlerMap.remove(msg.getName());
        if (Objects.nonNull(dssRestHandler)) {

            if (freeHandlerQueue.size() > MAX_FREE_HANDLER_SIZE) {
                context.getLog().info("There are too many handler in free queue. Drop the handler: {}", msg.getName());
                context.stop(msg.getHandlerActor());
            } else {
                context.getLog().info("add {} to freeHandlerQueue", msg.getName());
                freeHandlerQueue.add(dssRestHandler);
            }

        }

        return Behaviors.same();
    }

    private void setUpHandlerQueue(int size) {
        initRestMasterActorRef();
        for (int i = 0; i < size; i++) {
            final DssRestHandler handler = newAllocatedDssRestHandler(context, restMasterActorRef);
            freeHandlerQueue.add(handler);
        }
    }
}
