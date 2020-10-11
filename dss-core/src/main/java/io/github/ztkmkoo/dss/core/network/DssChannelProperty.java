package io.github.ztkmkoo.dss.core.network;

import io.github.ztkmkoo.dss.core.common.logging.DssLogLevel;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-01 21:32
 */
public interface DssChannelProperty {

    String getHost();

    int getPort();

    default DssLogLevel getDssLogLevel() {
        return DssLogLevel.DEBUG;
    }

    default Class<? extends ServerSocketChannel> getServerSocketClass() {
        return NioServerSocketChannel.class;
    }
}
