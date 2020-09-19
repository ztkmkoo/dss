package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.Behavior;
import io.github.ztkmkoo.dss.core.actor.property.DssActorProperty;
import io.github.ztkmkoo.dss.core.message.DssCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-15 01:33
 */
public interface DssBehaviorCreator <T extends DssCommand, P extends DssActorProperty> {

    Behavior<T> create(P property);
}
