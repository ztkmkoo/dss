package io.github.ztkmkoo.dss.core.actor.property;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 01:28
 */
public interface DssNetworkActorProperty {

    /**
     * netty boss event loop thread count
     * If need override this
     */
    default int getBossThreadCount() {
        return 1;
    }

    /**
     * netty worker event loop thread count
     * If need override this
     */
    default int getWorkerThreadCount() {
        return 0;
    }
}
