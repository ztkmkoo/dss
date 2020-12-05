package io.github.ztkmkoo.dss.core.network;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-11-28 14:50
 */
public class DssNettyChannelHandlerContext implements DssChannelHandlerContext {

    private final ChannelHandlerContext ctx;

    public DssNettyChannelHandlerContext(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public String getChannelId() {
        return ctx.channel().id().asShortText();
    }

    @Override
    public void write(Object msg) {
        ctx.write(msg);
    }

    @Override
    public void writeAndFlush(Object msg) {
        ctx.writeAndFlush(msg);
    }
}
