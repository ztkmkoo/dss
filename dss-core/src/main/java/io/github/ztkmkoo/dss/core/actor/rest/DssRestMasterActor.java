package io.github.ztkmkoo.dss.core.actor.rest;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActor;
import io.github.ztkmkoo.dss.core.actor.DssBehaviorCreator;
import io.github.ztkmkoo.dss.core.actor.DssMasterActor;
import io.github.ztkmkoo.dss.core.actor.DssServiceActorResolvable;
import io.github.ztkmkoo.dss.core.actor.enumeration.DssMasterActorStatus;
import io.github.ztkmkoo.dss.core.actor.property.*;
import io.github.ztkmkoo.dss.core.message.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 10:06
 */
public class DssRestMasterActor extends AbstractDssActor<DssMasterCommand> implements DssMasterActor {

    public static Behavior<DssMasterCommand> create() {
        return Behaviors.setup(DssRestMasterActor::new);
    }

    @Getter
    private DssMasterActorProperty dssMasterActorProperty;

    @Getter @Setter
    private DssMasterActorStatus masterActorStatus = DssMasterActorStatus.READY;

    @Getter @Setter
    private ActorRef<DssNetworkCommand> networkActor;

    @Getter @Setter
    private ActorRef<DssResolverCommand> resolverActor;

    @Getter @Setter
    private ActorRef<DssExceptionCommand> exceptionActor;

    private DssRestMasterActor(ActorContext<DssMasterCommand> context) {
        super(context);
    }

    @Override
    public Receive<DssMasterCommand> createReceive() {
        return masterReceiveBuilder(this)
                .build();
    }

    @Override
    public DssBehaviorCreator<DssExceptionCommand, DssExceptionActorProperty> getExceptionBehaviorCreator() {
        return null;
    }

    @Override
    public <M extends DssMasterActorProperty> DssExceptionActorProperty createDssExceptionActorProperty(M masterProperty) {
        return null;
    }

    @Override
    public DssBehaviorCreator<DssNetworkCommand, DssNetworkActorProperty> getNetworkBehaviorCreator() {
        return null;
    }

    @Override
    public <M extends DssMasterActorProperty> DssNetworkActorProperty createDssNetworkActorProperty(M masterProperty) {
        return null;
    }

    @Override
    public DssBehaviorCreator<DssResolverCommand, DssResolverActorProperty> getResolverBehaviorCreator() {
        return null;
    }

    @Override
    public <M extends DssMasterActorProperty> DssResolverActorProperty createDssResolverActorProperty(M masterProperty) {
        return null;
    }

    @Override
    public DssBehaviorCreator<DssServiceCommand, DssServiceActorProperty> getServiceBehaviorCreator() {
        return null;
    }

    @Override
    public <M extends DssMasterActorProperty> List<DssServiceActorProperty> createDssServiceActorPropertyList(M masterProperty) {
        return Collections.emptyList();
    }

    @Override
    public <P extends DssServiceActorProperty> DssServiceActorResolvable<String> createDssServiceActorResolvable(P property, ActorRef<DssServiceCommand> actor) {
        return null;
    }

    @Override
    public Map<String, DssServiceActorResolvable<String>> getServiceActorMap() {
        return null;
    }

    @Override
    public void putServiceActorResolvable(String key, DssServiceActorResolvable<String> value) {
        // do nothing
    }
}
