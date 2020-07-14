package io.github.ztkmkoo.dss.core.network.netty;

import io.netty.channel.*;
import io.netty.channel.socket.DefaultServerSocketChannelConfig;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.ServerSocketChannelConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.spi.SelectorProvider;

@Slf4j
public class DssServerSocketChannel extends AbstractChannel implements ServerSocketChannel {

    private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
    private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);

    private final SelectableChannel ch;
    private final ServerSocketChannelConfig config;
    protected final int readInterestOp = SelectionKey.OP_READ;

    volatile SelectionKey selectionKey;
    boolean readPending;

    public DssServerSocketChannel() {
        super(null);
        SelectableChannel ch = null;
        try {
            ch = DEFAULT_SELECTOR_PROVIDER.openServerSocketChannel();
            ch.configureBlocking(false);
        } catch (IOException e) {
            try {
                if (ch != null) {
                    ch.close();
                }
            } catch (IOException e2) {
                log.warn(
                        "Failed to close a partially initialized socket.", e2);
            }

            throw new ChannelException("Failed to enter non-blocking mode.", e);
        }

        this.ch = ch;

        this.config = new DssServerSocketConfig(this, ((java.nio.channels.ServerSocketChannel)ch).socket());
    }

    @Override
    protected DefaultChannelPipeline newChannelPipeline() {
        return new DssChannelPipeline(this);
    }

    @Override
    protected AbstractUnsafe newUnsafe() {
        return new DssMessageUnsafe();
    }

    @Override
    protected boolean isCompatible(EventLoop loop) {
        return loop instanceof DssEventLoop;
    }

    @Override
    protected SocketAddress localAddress0() {
        return null;
    }

    @Override
    protected SocketAddress remoteAddress0() {
        return null;
    }

    @Override
    protected void doBind(SocketAddress localAddress) throws Exception {
        ((java.nio.channels.ServerSocketChannel)ch).bind(localAddress, config.getBacklog());
    }

    @Override
    protected void doDisconnect() throws Exception {

    }

    @Override
    protected void doClose() throws Exception {

    }

    @Override
    protected void doBeginRead() throws Exception {
        // Channel.read() or ChannelHandlerContext.read() was called
        final SelectionKey selectionKey = this.selectionKey;
        if (!selectionKey.isValid()) {
            return;
        }

        readPending = true;

        final int interestOps = selectionKey.interestOps();
        if ((interestOps & readInterestOp) == 0) {
//            selectionKey.interestOps(interestOps | readInterestOp);
            selectionKey.interestOps(SelectionKey.OP_ACCEPT);
        }
    }

    @Override
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {

    }

    @Override
    public boolean isOpen() {
        return ch.isOpen();
    }

    @Override
    public boolean isActive() {
        return isOpen() && (((java.nio.channels.ServerSocketChannel)ch).socket()).isBound();
    }

    @Override
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override
    public ServerSocketChannelConfig config() {
        return config;
    }
    @Override
    public InetSocketAddress localAddress() {
        return null;
    }
    @Override
    public InetSocketAddress remoteAddress() {
        return null;
    }

    @Override
    protected void doRegister() throws Exception {
        boolean selected = false;
        for (;;) {
            try {
                selectionKey = ((java.nio.channels.ServerSocketChannel)ch).register(eventLoop().unwrappedSelector(), 0, this);
                return;
            } catch (CancelledKeyException e) {
                if (!selected) {
                    // Force the Selector to select now as the "canceled" SelectionKey may still be
                    // cached and not removed because no Select.select(..) operation was called yet.
                    eventLoop().selectNow();
                    selected = true;
                } else {
                    // We forced a select operation on the selector before but the SelectionKey is still cached
                    // for whatever reason. JDK bug ?
                    throw e;
                }
            }
        }
    }

    @Override
    public DssEventLoop eventLoop() {
        return (DssEventLoop) super.eventLoop();
    }

    private final class DssMessageUnsafe extends AbstractUnsafe implements DssUnsafe {
        @Override
        public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
            int a =0;
        }

        @Override
        public SelectableChannel ch() {
            return ch;
        }

        @Override
        public void finishConnect() {

        }

        @Override
        public void read() {
            int a = 0;
        }

        @Override
        public void forceFlush() {

        }
    }

    private static class DssServerSocketConfig extends DefaultServerSocketChannelConfig {

        /**
         * Creates a new instance.
         *
         * @param channel
         * @param javaSocket
         */
        public DssServerSocketConfig(ServerSocketChannel channel, ServerSocket javaSocket) {
            super(channel, javaSocket);
        }
    }

    public interface DssUnsafe extends Unsafe {
        /**
         * Return underlying {@link SelectableChannel}
         */
        SelectableChannel ch();

        /**
         * Finish connect
         */
        void finishConnect();

        /**
         * Read from underlying {@link SelectableChannel}
         */
        void read();

        void forceFlush();
    }
}
