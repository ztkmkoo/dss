package com.ztkmkoo.dss.server.actor.core;

import akka.actor.typed.Behavior;
import akka.actor.typed.SpawnProtocol;
import akka.actor.typed.javadsl.Behaviors;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오후 8:12
 */
public abstract class DssServerMasterActor {

    public static Behavior<SpawnProtocol.Command> create() {
        return Behaviors.setup(context -> SpawnProtocol.create());
    }

    private DssServerMasterActor() {
    }
}
