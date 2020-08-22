package io.github.ztkmkoo.dss.core.network.tcp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class DssTcpHandler extends SimpleChannelInboundHandler<Object> {

    private final ByteBuf firstMessage = Unpooled.buffer(256);
    private final StringBuilder buffer = new StringBuilder();

    public DssTcpHandler() {
        byte[] str = "Initial Message\n".getBytes();
        for (byte b : str) {
            firstMessage.writeByte(b);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        final ByteBuf readMessage = (ByteBuf)msg;
        if (readMessage.isReadable()) {
            buffer.append(readMessage.toString(CharsetUtil.UTF_8));
        }

        channelHandlerContext.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) {
        channelHandlerContext.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        cause.printStackTrace();
        channelHandlerContext.close();
    }
}
