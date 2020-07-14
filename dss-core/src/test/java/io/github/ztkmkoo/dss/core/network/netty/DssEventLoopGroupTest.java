package io.github.ztkmkoo.dss.core.network.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.Test;

import java.io.IOException;

public class DssEventLoopGroupTest {

    @Test
    public void test() throws InterruptedException, IOException {

        final EventLoopGroup bossGroup = new DssEventLoopGroup();
        final EventLoopGroup workerGroup = new DssEventLoopGroup();

        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);

        final Channel channel = bootstrap
                .channel(DssServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.TRACE))
                .childHandler(new ChannelInitializer<NioServerSocketChannel>() {
                    @Override
                    protected void initChannel(NioServerSocketChannel ch) throws Exception {
                        final ChannelPipeline p = ch.pipeline();
                        p.addLast(new HttpRequestDecoder());
                        p.addLast(new HttpResponseEncoder());
                        p.addLast(new SimpleChannelInboundHandler<Object>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
                                int a = 0;
                            }
                        });
                    }
                })
                .bind("127.0.0.1", 8080)
                .sync()
                .channel();

//        Thread.sleep(5000);
//
//        final Socket socket = new Socket();
//        socket.connect(new InetSocketAddress("127.0.0.1", 8080));
//
//        assertTrue(socket.isConnected());

        channel
                .closeFuture()
                .sync();
    }

}