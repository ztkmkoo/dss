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

    private static final String MSG_BEHAVIOR_CREATOR_NULL = "DssBehaviorCreator is null";
    private static final String MSG_PROPERTY_NULL = "DssActorProperty is null";
    private static final String MSG_ACTOR_NULL = "AbstractDssActor is null";
    private static final String MSG_ACTOR_CONTEXT_NULL = "ActorContext is null";
    private static final String MSG_BEHAVIOR_NULL = "Behavior is null";

    private DssCommonActorUtils() {}

    public static <C extends DssCommand, P extends DssCommand, T extends DssActorProperty> ActorRef<C> spawn(
            AbstractDssActor<P> parent,
            DssBehaviorCreator<C, T> creator,
            T property,
            String name) {
        return spawn(
                parent,
                Objects
                        .requireNonNull(creator, MSG_BEHAVIOR_CREATOR_NULL)
                        .create(Objects.requireNonNull(property, MSG_PROPERTY_NULL)),
                name);
    }

    public static <C extends DssCommand, P extends DssCommand> ActorRef<C> spawn(
            AbstractDssActor<P> parent,
            Behavior<C> behavior,
            String name) {
        return Objects
                .requireNonNull(Objects.requireNonNull(parent, MSG_ACTOR_NULL).getContext(), MSG_ACTOR_CONTEXT_NULL)
                .spawn(Objects.requireNonNull(behavior, MSG_BEHAVIOR_NULL), name);
    }
}
