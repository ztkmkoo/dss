package io.github.ztkmkoo.dss.core.actor.rest;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.DssServiceActorResolvable;
import io.github.ztkmkoo.dss.core.message.DssServiceCommand;
import lombok.Getter;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-12-06 00:10
 */
@Getter
public class DssRestServiceActorResolvable implements DssServiceActorResolvable<String> {

    private final String key;
    private final ActorRef<DssServiceCommand> actorRef;

    public DssRestServiceActorResolvable(String key, ActorRef<DssServiceCommand> actorRef) {
        this.key = key;
        this.actorRef = actorRef;
    }
}
