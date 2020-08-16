package io.github.ztkmkoo.dss.core.network.rest.enumeration;
import io.netty.handler.logging.LogLevel;
import lombok.Getter;

public enum DssLogLevel {

    ERROR(LogLevel.ERROR), WARN(LogLevel.WARN), INFO( LogLevel.INFO), DEBUG( LogLevel.DEBUG), TRACE( LogLevel.TRACE);

    private LogLevel level;

    DssLogLevel( LogLevel level) {
        this.level = level;
    }

    public static DssLogLevel levelOf(LogLevel level) {
        for (DssLogLevel loglevel : DssLogLevel.values()) {
            if (loglevel.level.equals(level)) {
                return loglevel;
            }
        }
        return null;
    }
}