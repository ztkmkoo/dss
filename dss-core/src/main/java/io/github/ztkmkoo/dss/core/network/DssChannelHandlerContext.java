package io.github.ztkmkoo.dss.core.network;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-11-28 14:48
 */
public interface DssChannelHandlerContext {

    String getChannelId();

    void write(Object msg);

    void writeAndFlush(Object msg);
}
