package io.github.ztkmkoo.dss.core.network;

import io.github.ztkmkoo.dss.core.message.DssCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-01 21:47
 */
public interface DssHandler<C extends DssCommand> extends DssActorAcceptable<C> {
}
