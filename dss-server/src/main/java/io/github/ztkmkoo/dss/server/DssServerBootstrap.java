package io.github.ztkmkoo.dss.server;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-10-26 23:07
 */
public interface DssServerBootstrap {

    /**
     * Start the server.
     * It will be blocked until the server is terminated.
     * @throws InterruptedException ServerBootstrap bind and channel future sync
     */
    void start() throws InterruptedException;
}
