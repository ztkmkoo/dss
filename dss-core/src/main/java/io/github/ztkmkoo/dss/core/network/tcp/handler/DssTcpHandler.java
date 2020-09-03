package io.github.ztkmkoo.dss.core.network.tcp.handler;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class DssTcpHandler extends SimpleChannelInboundHandler<Object> {

    private final ByteBuf message = Unpooled.buffer(256);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        String readMessage = ((ByteBuf)msg).toString(Charset.defaultCharset());
        for (byte b : readMessage.getBytes()) {
            message.writeByte(b);
        }
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
