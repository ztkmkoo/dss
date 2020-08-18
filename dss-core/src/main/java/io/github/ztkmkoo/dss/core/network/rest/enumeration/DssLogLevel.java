package io.github.ztkmkoo.dss.core.network.rest.enumeration;
import io.netty.handler.logging.LogLevel;

public enum DssLogLevel {

    ERROR(LogLevel.ERROR),
    WARN(LogLevel.WARN),
    INFO( LogLevel.INFO),
    DEBUG( LogLevel.DEBUG),
    TRACE( LogLevel.TRACE)
    ;

    private LogLevel level;

    DssLogLevel( LogLevel level) {
        this.level = level;
    }

    public LogLevel getNettyLogLevel() {
        return level;
    }
}