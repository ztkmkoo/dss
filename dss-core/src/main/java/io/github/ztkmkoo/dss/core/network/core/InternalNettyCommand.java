package io.github.ztkmkoo.dss.core.network.core;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-16 03:15
 */
interface InternalNettyCommand extends Serializable {

    @Getter @Builder
    class InitServerBootstrap implements InternalNettyCommand {
        private final Integer bossGroupThread;
        private final Integer workerGroupThread;
    }
}
