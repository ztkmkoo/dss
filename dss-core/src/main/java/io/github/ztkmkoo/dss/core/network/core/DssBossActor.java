package io.github.ztkmkoo.dss.core.network.core;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.actor.AbstractDssBehavior;
import io.netty.channel.DefaultSelectStrategyFactory;
import io.netty.channel.SelectStrategy;
import io.netty.util.IntSupplier;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-16 22:40
 */
public class DssBossActor extends AbstractDssBehavior<InternalNettyCommand> implements DssSingleThreadEventLoop {

    public static Behavior<InternalNettyCommand> create(ActorRef<InternalNettyCommand> workerMaster) {
        return Behaviors.setup(context -> new DssBossActor(context, workerMaster));
    }

    private static final Main RUN_MAIN_MSG = new Main();
    private static final long NONE = Long.MAX_VALUE;

    private final ActorRef<InternalNettyCommand> workerMaster;
    private final SelectStrategy selectStrategy = DefaultSelectStrategyFactory.INSTANCE.newSelectStrategy();
    private final IntSupplier selectNowSupplier = this::selectNow;

    private Selector selector;

    private DssBossActor(ActorContext<InternalNettyCommand> context, ActorRef<InternalNettyCommand> workerMaster) {
        super(context);
        Objects.requireNonNull(workerMaster);
        this.workerMaster = workerMaster;
    }

    @Override
    public Receive<InternalNettyCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(InternalNettyCommand.BossRun.class, this::handlingBossRun)
                .onMessage(Main.class, this::handlingMain)
                .build();
    }

    private Behavior<InternalNettyCommand> handlingBossRun(InternalNettyCommand.BossRun msg) {
        getSelf().tell(RUN_MAIN_MSG);
        return Behaviors.same();
    }

    private Behavior<InternalNettyCommand> handlingMain(Main msg) {
        try {
            int strategy = selectStrategy.calculateStrategy(selectNowSupplier, hasTasks());
            switch (strategy) {
                case SelectStrategy.CONTINUE:
                    continueMain();
                    return Behaviors.same();
                case SelectStrategy.SELECT:

                    break;
                default:
                    //
            }
        } catch (Exception e) {
            getLog().error("handlingMain error", e);
        }
        return Behaviors.same();
    }

    private void continueMain() {
        getSelf().tell(RUN_MAIN_MSG);
    }

    private int selectNow() throws IOException {
        return selector.selectNow();
    }

    private boolean hasTasks() {
        return false;
    }

    private int select(long deadlineNanos) throws IOException {
        if (deadlineNanos == NONE) {
            return selector.select();
        }
        return selector.selectNow();
    }

    private static class Main implements InternalNettyCommand {}
}
