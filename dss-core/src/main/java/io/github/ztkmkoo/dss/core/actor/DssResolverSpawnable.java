package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.property.DssMasterActorProperty;
import io.github.ztkmkoo.dss.core.actor.property.DssResolverActorProperty;
import io.github.ztkmkoo.dss.core.actor.util.DssCommonActorUtils;
import io.github.ztkmkoo.dss.core.message.DssMasterCommand;
import io.github.ztkmkoo.dss.core.message.DssResolverCommand;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-15 02:42
 */
public interface DssResolverSpawnable extends DssResolverAcceptable {

    /**
     * Help create resolver actor behavior
     */
    DssBehaviorCreator<DssResolverCommand, DssResolverActorProperty> getResolverBehaviorCreator();

    <P extends DssResolverActorProperty, M extends DssMasterActorProperty> P createDssResolverActorProperty(M masterProperty);

    default <M extends DssMasterActorProperty> void initializeResolverActor(AbstractDssActor<DssMasterCommand> master, M masterProperty) {
        final DssResolverActorProperty property = createDssResolverActorProperty(Objects.requireNonNull(masterProperty));
        final ActorRef<DssResolverCommand> actor = DssCommonActorUtils.spawn(master, getResolverBehaviorCreator(), property, "resolver");
        setResolverActor(actor);
    }
}
