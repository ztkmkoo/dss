package io.github.ztkmkoo.dss.core.actor.util;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActor;
import io.github.ztkmkoo.dss.core.actor.DssBehaviorCreator;
import io.github.ztkmkoo.dss.core.actor.property.DssActorProperty;
import io.github.ztkmkoo.dss.core.message.DssCommand;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-15 01:42
 */
public class DssCommonActorUtils {

    private DssCommonActorUtils() {}

    public static <C extends DssCommand, P extends DssCommand, T extends DssActorProperty> ActorRef<C> spawn(
            AbstractDssActor<P> parent,
            DssBehaviorCreator<C, T> creator,
            T property,
            String name) {
        return spawn(parent, Objects.requireNonNull(creator).create(Objects.requireNonNull(property)), name);
    }

    public static <C extends DssCommand, P extends DssCommand> ActorRef<C> spawn(
            AbstractDssActor<P> parent,
            Behavior<C> behavior,
            String name) {
        return Objects
                .requireNonNull(Objects.requireNonNull(parent).getContext())
                .spawn(Objects.requireNonNull(behavior), name);
    }
}
