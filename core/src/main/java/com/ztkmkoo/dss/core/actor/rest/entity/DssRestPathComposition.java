package com.ztkmkoo.dss.core.actor.rest.entity;

import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommand;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 18. 오전 4:09
 */
@Getter
public class DssRestPathComposition implements Serializable {
    private static final long serialVersionUID = 6447860854358860483L;

    private final DssRestPath dssRestPath;
    private final ActorRef<DssRestServiceActorCommand> serviceActor;

    @Builder
    private DssRestPathComposition(DssRestPath dssRestPath, ActorRef<DssRestServiceActorCommand> serviceActor) {
        this.dssRestPath = dssRestPath;
        this.serviceActor = serviceActor;
    }
}
