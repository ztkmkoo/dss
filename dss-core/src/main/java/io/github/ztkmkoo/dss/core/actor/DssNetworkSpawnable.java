package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.property.DssMasterActorProperty;
import io.github.ztkmkoo.dss.core.actor.property.DssNetworkActorProperty;
import io.github.ztkmkoo.dss.core.actor.util.DssCommonActorUtils;
import io.github.ztkmkoo.dss.core.message.DssMasterCommand;
import io.github.ztkmkoo.dss.core.message.DssNetworkCommand;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-15 02:46
 */
public interface DssNetworkSpawnable extends DssNetworkAcceptable {

    /**
     * Help create network actor behavior
     */
    DssBehaviorCreator<DssNetworkCommand, DssNetworkActorProperty> getNetworkBehaviorCreator();

    /**
     * Create DssNetworkActorProperty from DssMasterActorProperty
     */
    <P extends DssNetworkActorProperty, M extends DssMasterActorProperty> P createDssNetworkActorProperty(M masterProperty);

    default <M extends DssMasterActorProperty> void initializeNetworkActor(AbstractDssActor<DssMasterCommand> master, M masterProperty) {
        final DssNetworkActorProperty property = createDssNetworkActorProperty(Objects.requireNonNull(masterProperty));
        final ActorRef<DssNetworkCommand> actor = DssCommonActorUtils.spawn(master, getNetworkBehaviorCreator(), property, "network");
        setNetworkActor(actor);
    }
}
