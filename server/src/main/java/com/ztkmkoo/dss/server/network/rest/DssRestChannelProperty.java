package com.ztkmkoo.dss.server.network.rest;

import com.ztkmkoo.dss.server.network.core.DssNetworkChannelProperty;
import com.ztkmkoo.dss.server.network.core.enumeration.NettyLogLevelWrapperType;
import com.ztkmkoo.dss.server.network.core.handler.DssChannelInitializer;
import com.ztkmkoo.dss.server.network.core.service.DssServiceCreator;
import com.ztkmkoo.dss.server.util.BuilderUtils;
import io.netty.channel.socket.SocketChannel;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 17. 오후 10:40
 */
public class DssRestChannelProperty implements DssNetworkChannelProperty {

    @Getter
    private final String host;

    @Getter
    private final int port;

    @Getter
    private final int bossThread;

    @Getter
    private final int workerThread;

    @Getter
    private final List<DssServiceCreator> channelServiceCreatorList;

    @Getter
    private final NettyLogLevelWrapperType logLevel;

    @Getter
    private final DssChannelInitializer<SocketChannel> dssChannelInitializer;

    private DssRestChannelProperty(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.bossThread = builder.bossThread;
        this.workerThread = builder.workerThread;
        this.channelServiceCreatorList = Collections.unmodifiableList(builder.channelServiceCreatorList);
        this.logLevel = builder.logLevel;
        this.dssChannelInitializer = builder.dssChannelInitializer;
    }

    public static Builder builder(DssChannelInitializer<SocketChannel> dssChannelInitializer) {
        return new Builder(dssChannelInitializer);
    }

    public static class Builder {

        private static final String DEFAULT_HOST = "0.0.0.0";
        private static final int DEFAULT_PORT = 8080;
        private static final int DEFAULT_BOSS_THREAD = 0;
        private static final int DEFAULT_WORKER_THREAD = 0;
        private static final NettyLogLevelWrapperType DEFAULT_LOG_LEVEL = NettyLogLevelWrapperType.INFO;

        private final DssChannelInitializer<SocketChannel> dssChannelInitializer;

        private String host;
        private Integer port;
        private Integer bossThread;
        private Integer workerThread;
        private List<DssServiceCreator> channelServiceCreatorList;
        private NettyLogLevelWrapperType logLevel;


        private Builder(DssChannelInitializer<SocketChannel> dssChannelInitializer) {
            this.dssChannelInitializer = dssChannelInitializer;
        }

        public DssRestChannelProperty build() {

            Objects.requireNonNull(dssChannelInitializer, "DssChannelInitializer cannot be null");

            host = BuilderUtils.getDefaultValueIfEmpty(host, DEFAULT_HOST);
            port = BuilderUtils.getDefaultValueIfEmpty(port, DEFAULT_PORT);
            bossThread = BuilderUtils.getDefaultValueIfEmpty(bossThread, DEFAULT_BOSS_THREAD);
            workerThread = BuilderUtils.getDefaultValueIfEmpty(workerThread, DEFAULT_WORKER_THREAD);
            channelServiceCreatorList = BuilderUtils.getDefaultValueIfEmpty(channelServiceCreatorList, Collections.emptyList());
            logLevel = BuilderUtils.getDefaultValueIfEmpty(logLevel, DEFAULT_LOG_LEVEL);

            return new DssRestChannelProperty(this);
        }
    }
}
