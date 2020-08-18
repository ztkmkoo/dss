package io.github.ztkmkoo.dss.core.actor.blocking;

import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-13 01:41
 */
public interface DssBlockingService<T extends DssBlockingCommand> {
    /**
     * Set the name of this service.
     * You could find this service actor ref from this value
     * @return service actor name
     */
    String getName();

    Class<T> getCommandClassType();
}
