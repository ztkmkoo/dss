package io.github.ztkmkoo.dss.core.common;

import io.github.ztkmkoo.dss.core.common.logging.DssLogLevel;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 03:33
 */
public class CommonStaticContents {

    private static final String DEFAULT_HOST = "0.0.0.0";
    private static final int DEFAULT_PORT = 8080;
    private static final DssLogLevel DEFAULT_LOG_LEVEL = DssLogLevel.DEBUG;
    private static final String TEXT_HOST = "host";
    private static final String TEXT_PORT = "port";
    private static final String TEXT_LOG_LEVEL = "logLevel";

    public static String getDefaultHost() {
        return DEFAULT_HOST;
    }

    public static int getDefaultPort() {
        return DEFAULT_PORT;
    }

    public static DssLogLevel getDefaultLogLevel() {
        return DEFAULT_LOG_LEVEL;
    }

    public static String getTextHost() {
        return TEXT_HOST;
    }

    public static String getTextPort() {
        return TEXT_PORT;
    }

    public static String getTextLogLevel() {
        return TEXT_LOG_LEVEL;
    }

    private CommonStaticContents() {}
}
