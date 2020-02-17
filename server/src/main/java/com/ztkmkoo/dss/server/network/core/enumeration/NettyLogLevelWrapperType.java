package com.ztkmkoo.dss.server.network.core.enumeration;

import io.netty.handler.logging.LogLevel;
import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 17. 오후 10:44
 */
public enum NettyLogLevelWrapperType {

    TRACE(LogLevel.TRACE),
    DEBUG(LogLevel.DEBUG),
    INFO(LogLevel.INFO),
    WARN(LogLevel.WARN),
    ERROR(LogLevel.ERROR)
    ;

    @Getter
    private final LogLevel nettyLogLevel;

    NettyLogLevelWrapperType(LogLevel nettyLogLevel) {
        this.nettyLogLevel = nettyLogLevel;
    }
}
