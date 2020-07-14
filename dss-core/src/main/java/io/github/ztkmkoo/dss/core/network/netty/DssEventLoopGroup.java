package io.github.ztkmkoo.dss.core.network.netty;

import io.netty.channel.DefaultSelectStrategyFactory;
import io.netty.channel.EventLoop;
import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.util.concurrent.RejectedExecutionHandlers;
import io.netty.util.internal.PlatformDependent;

import java.util.concurrent.Executor;

public class DssEventLoopGroup extends MultithreadEventLoopGroup {

    public DssEventLoopGroup() {
        this(1, (Executor) null);
    }

    protected DssEventLoopGroup(int nThreads, Executor executor, Object... args) {
        super(nThreads, executor, args);
    }

    @Override
    protected EventLoop newChild(Executor executor, Object... args) throws Exception {
        return new DssEventLoop(
                this,
                executor,
                DefaultSelectStrategyFactory.INSTANCE,
                PlatformDependent.<Runnable>newMpscQueue(),
                PlatformDependent.<Runnable>newMpscQueue(),
                RejectedExecutionHandlers.reject()
        );
    }
}
