package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.property.DssExceptionActorProperty;
import io.github.ztkmkoo.dss.core.actor.property.DssMasterActorProperty;
import io.github.ztkmkoo.dss.core.actor.util.DssCommonActorUtils;
import io.github.ztkmkoo.dss.core.message.DssExceptionCommand;
import io.github.ztkmkoo.dss.core.message.DssMasterCommand;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-15 02:39
 */
public interface DssExceptionSpawnable extends DssExceptionAcceptable {

    /**
     * Help create exception actor behavior
     */
    DssBehaviorCreator<DssExceptionCommand, DssExceptionActorProperty> getExceptionBehaviorCreator();

    /**
     * Create DssExceptionActorProperty from DssMasterActorProperty
     */
    <E extends DssExceptionActorProperty, M extends DssMasterActorProperty> E createDssExceptionActorProperty(M masterProperty);

    /**
     * spawn exception actor and set it
     */
    default <M extends DssMasterActorProperty> void initializeExceptionActor(AbstractDssActor<DssMasterCommand> master, M masterProperty) {
        final DssExceptionActorProperty property = createDssExceptionActorProperty(Objects.requireNonNull(masterProperty));
        final ActorRef<DssExceptionCommand> actor = DssCommonActorUtils.spawn(master, getExceptionBehaviorCreator(), property, "exception");
        setExceptionActor(actor);
    }
}
