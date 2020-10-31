package io.github.ztkmkoo.dss.core.actor.property;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 01:28
 */
public class DssNetworkActorProperty implements DssActorProperty {
    private static final long serialVersionUID = 5375444098524937078L;

    /**
     * netty boss event loop thread count
     */
    @Getter @Setter
    private int bossThreadCount;

    /**
     * netty worker event loop thread count
     */
    @Getter @Setter
    private int workerThreadCount;
}
