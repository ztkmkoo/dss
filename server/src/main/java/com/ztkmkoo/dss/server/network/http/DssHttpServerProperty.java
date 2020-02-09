package com.ztkmkoo.dss.server.network.http;

import com.ztkmkoo.dss.server.network.core.DssServerProperty;
import com.ztkmkoo.dss.server.network.core.creator.DssServerHandlerCreator;
import com.ztkmkoo.dss.server.network.enumeration.DssNetworkType;
import lombok.Getter;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 */
@Getter
public class DssHttpServerProperty implements DssServerProperty {

    private static final DssNetworkType networkType = DssNetworkType.HTTP;
    private final boolean ssl;
    private final String host;
    private final int port;
    private final int bossThread;
    private final int workerThread;
    private final DssServerHandlerCreator handlerCreator;

    private DssHttpServerProperty(Builder builder) {

        this.ssl = builder.ssl;
        this.host = builder.host;
        this.port = builder.port;
        this.bossThread = builder.bossThread;
        this.workerThread = builder.workerThread;
        this.handlerCreator = builder.handlerCreator;
    }

    @Override
    public DssNetworkType getNetworkType() {
        return networkType;
    }

    public static Builder builder(final boolean ssl) {
        return new Builder(ssl);
    }

    public static class Builder {

        private static final String DEFAULT_HOST = "0.0.0.0";
        private static final int DEFAULT_PORT = 8080;
        private static final int DEFAULT_SSL_PORT = 8443;
        private static final int DEFAULT_BOSS_THREAD = 0;
        private static final int DEFAULT_WORKER_THREAD = 0;
        private static final DssServerHandlerCreator DEFAULT_HANDLER_CREATOR = DssHttpSimpleHandler::new;

        private final boolean ssl;

        private String host;
        private Integer port;
        private Integer bossThread;
        private Integer workerThread;
        private DssServerHandlerCreator handlerCreator;

        private Builder(final boolean ssl) {
            this.ssl = ssl;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder bossThread(int bossThread) {
            this.bossThread = bossThread;
            return this;
        }

        public Builder workerThread(int workerThread) {
            this.workerThread = workerThread;
            return this;
        }

        public Builder handlerCreator(DssServerHandlerCreator handlerCreator) {
            this.handlerCreator = handlerCreator;
            return this;
        }

        public DssHttpServerProperty build() {

            if (Objects.isNull(host)) {
                this.host = DEFAULT_HOST;
            }

            if (Objects.isNull(port)) {
                this.port = ssl ? DEFAULT_SSL_PORT : DEFAULT_PORT;
            }

            if (Objects.isNull(bossThread)) {
                this.bossThread = DEFAULT_BOSS_THREAD;
            }

            if (Objects.isNull(workerThread)) {
                this.workerThread = DEFAULT_WORKER_THREAD;
            }

            if (Objects.isNull(handlerCreator)) {
                this.handlerCreator = DEFAULT_HANDLER_CREATOR;
            }

            return new DssHttpServerProperty(this);
        }
    }
}
