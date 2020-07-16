package io.github.ztkmkoo.dss.core.network.core.util;

import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-16 22:15
 */
public class NetworkCoreUtils {

    private static final int DEFAULT_THREADS_COUNT;

    private NetworkCoreUtils() {}

    static {
        DEFAULT_THREADS_COUNT = Math.max(1, SystemPropertyUtil.getInt(
                "io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
    }

    public static int getThreadsCount(int threadCount) {
        if (threadCount <= 0) {
            return DEFAULT_THREADS_COUNT;
        }

        return threadCount;
    }
}
