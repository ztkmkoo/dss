package io.github.ztkmkoo.dss.core.common.logging;

import io.netty.handler.logging.LogLevel;
import lombok.Getter;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 02:00
 */
public enum DssLogLevel {
    ERROR(LogLevel.ERROR),
    WARN(LogLevel.WARN),
    INFO(LogLevel.INFO),
    DEBUG(LogLevel.DEBUG),
    TRACE(LogLevel.TRACE)
    ;

    @Getter
    private final LogLevel nettyLogLevel;

    DssLogLevel(LogLevel nettyLogLevel) {
        this.nettyLogLevel = nettyLogLevel;
    }
}
