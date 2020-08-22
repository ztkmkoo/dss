package io.github.ztkmkoo.dss.server;

import io.github.ztkmkoo.dss.core.actor.DssActorService;
import io.github.ztkmkoo.dss.core.exception.handler.DssExceptionHandler;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-01 21:51
 */
public interface DssServer<S extends DssActorService> {

    /**
     * Start the server.
     * It will be blocked until the server is terminated.
     * @throws InterruptedException ServerBootstrap bind and channel future sync
     */
    void start() throws InterruptedException;

    /**
     * Close netty channel and terminate akka system.
     * @throws InterruptedException Netty channel close
     */
    void stop() throws InterruptedException;

    /**
     * If server is activated.
     * Thread-safe
     */
    boolean isActivated();

    /**
     * If server is shutdown.
     * Thread-safe
     */
    boolean isShutdown();

    /**
     * add service for mapping
     * @return self(for method chain)
     */
    DssServer<S> addDssService(S service);

    DssServer<S> addExceptionHandler(DssExceptionHandler exceptionHandler);
}
