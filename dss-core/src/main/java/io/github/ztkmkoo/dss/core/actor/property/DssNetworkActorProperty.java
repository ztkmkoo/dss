package io.github.ztkmkoo.dss.core.actor.property;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 01:28
 */
public interface DssNetworkActorProperty {

    /**
     * netty boss event loop thread count
     */
    int getBossThreadCount();

    /**
     * netty worker event loop thread count
     */
    int getWorkerThreadCount();
}
