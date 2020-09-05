package io.github.ztkmkoo.dss.core.message;

import io.github.ztkmkoo.dss.core.common.logging.DssLogLevel;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 03:57
 */
public interface DssBindCommand {

    String getHost();

    int getPort();

    DssLogLevel getLogLevel();
}
