package io.github.ztkmkoo.dss.core.message.core;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssCommand;
import io.github.ztkmkoo.dss.core.message.DssToStringCommand;
import lombok.Builder;
import lombok.Getter;

import java.nio.channels.SelectionKey;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-22 23:09
 */
public interface DssInternalSystemCommand extends DssCommand {

    @Getter
    class RootInitialize extends DssToStringCommand implements DssInternalSystemCommand {
        private static final long serialVersionUID = 4836378915193435405L;

        private final int bossGroupCount;
        private final int workerGroupCount;

        @Builder
        private RootInitialize(int workerGroupCount) {
            this(1, workerGroupCount);
        }

        private RootInitialize(int bossGroupCount, int workerGroupCount) {
            this.bossGroupCount = bossGroupCount;
            this.workerGroupCount = workerGroupCount;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

    @Getter
    class BossMasterInitialize extends DssToStringCommand implements DssInternalSystemCommand {
        private static final long serialVersionUID = -750357927234171323L;

        private final int bossGroupCount;
        private final ActorRef<DssInternalSystemCommand> worker;

        @Builder
        private BossMasterInitialize(int bossGroupCount, ActorRef<DssInternalSystemCommand> worker) {
            this.bossGroupCount = bossGroupCount;
            this.worker = worker;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

    @Getter
    class BossInitialize extends DssToStringCommand implements DssInternalSystemCommand {
        private static final long serialVersionUID = -157184164239666680L;

        private final ActorRef<DssInternalSystemCommand> worker;

        @Builder
        private BossInitialize(ActorRef<DssInternalSystemCommand> worker) {
            this.worker = worker;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

    @Getter
    class ServerListen extends DssToStringCommand implements DssInternalSystemCommand {
        private static final long serialVersionUID = -1302110639582453373L;

        private final String host;
        private final int port;

        @Builder
        private ServerListen(String host, int port) {
            this.host = host;
            this.port = port;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

    @Getter
    class ServiceChannel extends DssToStringCommand implements DssInternalSystemCommand {
        private static final long serialVersionUID = 7696671056368891976L;

        private final transient SelectionKey key;
        private final int hash;

        @Builder
        public ServiceChannel(SelectionKey key) {
            this.key = key;
            this.hash = key.hashCode();
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }
}
