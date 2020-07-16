package io.github.ztkmkoo.dss.core.network.core;

import akka.actor.typed.ActorRef;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-16 03:15
 */
interface InternalNettyCommand extends Serializable {

    @Getter
    abstract class ContainSenderCommand implements InternalNettyCommand {
        private final ActorRef<InternalNettyCommand> sender;

        protected ContainSenderCommand(ActorRef<InternalNettyCommand> sender) {
            this.sender = sender;
        }
    }

    @Getter @Builder
    class InitServerBootstrap implements InternalNettyCommand {
        private final Integer bossGroupThread;
        private final Integer workerGroupThread;
    }

    @Getter
    class InitBossMaster extends ContainSenderCommand {
        private final int bossGroupThread;

        @Builder
        InitBossMaster(ActorRef<InternalNettyCommand> sender, int bossGroupThread) {
            super(sender);
            this.bossGroupThread = bossGroupThread;
        }
    }

    class BossMasterReady extends ContainSenderCommand {
        @Builder
        BossMasterReady(ActorRef<InternalNettyCommand> sender) {
            super(sender);
        }
    }
}
