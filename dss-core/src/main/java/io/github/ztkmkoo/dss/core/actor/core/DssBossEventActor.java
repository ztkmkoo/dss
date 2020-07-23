package io.github.ztkmkoo.dss.core.actor.core;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.message.DssToStringCommand;
import io.github.ztkmkoo.dss.core.message.core.DssInternalSystemCommand;
import io.github.ztkmkoo.dss.core.network.core.entity.DssSelectionKeyWrapper;
import io.netty.util.internal.ObjectUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-22 23:55
 */
final class DssBossEventActor extends AbstractDssCoreActor<DssInternalSystemCommand> {

    public static Behavior<DssInternalSystemCommand> create() {
        return AbstractDssCoreActor.create(Behaviors.setup(DssBossEventActor::new));
    }

    private static final long TIME_OUT_MILLIS = 1000;

    private final Queue<DssSelectionKeyWrapper> selectionKeyQueue = new ArrayDeque<>();
    private ActorRef<DssInternalSystemCommand> worker;
    private Selector selector;

    private DssBossEventActor(ActorContext<DssInternalSystemCommand> context) {
        super(context);
    }

    @Override
    public Receive<DssInternalSystemCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(DssInternalSystemCommand.BossInitialize.class, this::handlingBossInitialize)
                .onMessage(DssInternalSystemCommand.ServerListen.class, this::handlingServerListen)
                .onMessage(SelectCommand.class, this::handlingSelectCommand)
                .onMessage(CloseTimeoutChannelCommand.class, this::handlingCloseTimeoutChannelCommand)
                .build();
    }

    private Behavior<DssInternalSystemCommand> handlingBossInitialize(DssInternalSystemCommand.BossInitialize msg) {
        this.worker = ObjectUtil.checkNotNull(msg.getWorker(), "worker-master");
        return Behaviors.same();
    }

    private Behavior<DssInternalSystemCommand> handlingServerListen(DssInternalSystemCommand.ServerListen msg) throws IOException {
        final InetSocketAddress address = new InetSocketAddress(msg.getHost(), msg.getPort());

        try (final ServerSocketChannel channel = ServerSocketChannel.open();){
            final ServerSocket socket = channel.socket();
            selector = Selector.open();

            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_ACCEPT);

            getLog().info("Try to bind server at {}", address);
            socket.bind(address);

            getSelf().tell(SelectCommand.INSTANCE);
        }

        return Behaviors.same();
    }

    private Behavior<DssInternalSystemCommand> handlingSelectCommand(SelectCommand msg) throws IOException {
        Objects.requireNonNull(selector);
        getLog().debug("Try to select keys");

        final int select = selector.select(TIME_OUT_MILLIS);
        if (select > 0) {
            final Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                final SelectionKey key = it.next();
                it.remove();
                selectionKeyQueue.add(new DssSelectionKeyWrapper(key));
                worker.tell(DssInternalSystemCommand.ServiceChannel
                        .builder()
                        .key(key)
                        .build());
            }
        }

        getSelf().tell(CloseTimeoutChannelCommand.INSTANCE);
        getSelf().tell(SelectCommand.INSTANCE);

        return Behaviors.same();
    }

    private Behavior<DssInternalSystemCommand> handlingCloseTimeoutChannelCommand(CloseTimeoutChannelCommand msg) {
        final long thresholdMillis = System.currentTimeMillis() - TIME_OUT_MILLIS;

        while (true) {
            final DssSelectionKeyWrapper wrapper = selectionKeyQueue.peek();
            if (Objects.nonNull(wrapper)) {
                if (wrapper.getOpenMillis() >= thresholdMillis) {
                    break;
                }

                wrapper.getSelectionKey().cancel();
                final SelectableChannel channel = wrapper.getSelectionKey().channel();
                if (Objects.nonNull(channel) && channel.isOpen()) {
                    synchronized(channel.blockingLock()) {
                        try {
                            channel.close();
                        } catch (IOException e) {
                            getLog().error("Close timeout channel failed", e);
                        }
                    }
                }
            }

            selectionKeyQueue.poll();
        }

        return Behaviors.same();
    }

    private static class SelectCommand extends DssToStringCommand implements DssInternalSystemCommand {
        private static final long serialVersionUID = 6978756363887186057L;

        private static final SelectCommand INSTANCE = new SelectCommand();

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

    private static class CloseTimeoutChannelCommand extends DssToStringCommand implements DssInternalSystemCommand {
        private static final long serialVersionUID = 1907361029348009749L;

        private static final CloseTimeoutChannelCommand INSTANCE = new CloseTimeoutChannelCommand();

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }
}
