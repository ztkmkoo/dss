package io.github.ztkmkoo.dss.core.network.rest;

import io.github.ztkmkoo.dss.core.network.DssNetworkChannel;
import io.github.ztkmkoo.dss.core.network.DssNetworkChannelBuilder;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-10-26 23:04
 */
public class DssRestNetworkChannelBuilder implements DssNetworkChannelBuilder {

    private final int bossThread;
    private final int workerThread;

    public DssRestNetworkChannelBuilder(int bossThread, int workerThread) {
        this.bossThread = bossThread;
        this.workerThread = workerThread;
    }

    @Override
    public DssNetworkChannel build() {
        return new DssRestNetworkChannel(bossThread, workerThread);
    }
}
