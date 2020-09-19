package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.exception.DssSpawnActorException;
import io.github.ztkmkoo.dss.core.actor.property.DssMasterActorProperty;
import io.github.ztkmkoo.dss.core.actor.property.DssServiceActorProperty;
import io.github.ztkmkoo.dss.core.actor.util.DssCommonActorUtils;
import io.github.ztkmkoo.dss.core.message.DssMasterCommand;
import io.github.ztkmkoo.dss.core.message.DssServiceCommand;
import io.github.ztkmkoo.dss.core.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-15 03:04
 */
public interface DssServiceSpawnable extends DssServiceAcceptable {

    /**
     * Help create service actor behavior
     */
    DssBehaviorCreator<DssServiceCommand, DssServiceActorProperty> getServiceBehaviorCreator();

    /**
     * Create DssServiceActorProperty list from DssMasterActorProperty
     */
    <M extends DssMasterActorProperty> List<DssServiceActorProperty> createDssServiceActorPropertyList(M masterProperty);

    /**
     * Create DssServiceActorResolvable from DssServiceActorProperty and service actor
     */
    <P extends DssServiceActorProperty> DssServiceActorResolvable<String> createDssServiceActorResolvable(P property, ActorRef<DssServiceCommand> actor);

    /**
     * spawn service actors and set it
     */
    default <M extends DssMasterActorProperty> void initializeServiceActor(AbstractDssActor<DssMasterCommand> master, M masterProperty) {
        final List<DssServiceActorProperty> propertyList = createDssServiceActorPropertyList(masterProperty);
        if (Objects.isNull(propertyList)) {
            return;
        }

        propertyList
                .stream()
                .filter(Objects::nonNull)
                .forEach(p -> initializeServiceActor(master, p));
    }

    /**
     * spawn service actor and set it
     */
    default <P extends DssServiceActorProperty> void initializeServiceActor(AbstractDssActor<DssMasterCommand> master, P property) {
        final ActorRef<DssServiceCommand> actor = DssCommonActorUtils.spawn(master, getServiceBehaviorCreator(), property, "service-" + property.getName());
        final DssServiceActorResolvable<String> resolvable = createDssServiceActorResolvable(property, actor);
        putServiceActorResolvable(resolvable);
    }

    /**
     * put service actor
     */
    default void putServiceActorResolvable(DssServiceActorResolvable<String> resolvable) {
        Objects.requireNonNull(resolvable);
        if (StringUtils.isEmpty(resolvable.getKey())) {
            throw new DssSpawnActorException("Spawn service actor failed. DssServiceActorResolvable key is empty.");
        }

        putServiceActorResolvable(resolvable.getKey(), resolvable);
    }
}
