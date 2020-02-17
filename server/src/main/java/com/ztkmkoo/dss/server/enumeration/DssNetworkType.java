package com.ztkmkoo.dss.server.enumeration;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.Props;
import akka.actor.typed.SpawnProtocol;
import com.ztkmkoo.dss.server.actor.http.HttpMasterActor;
import com.ztkmkoo.dss.server.message.ServerMessages;
import com.ztkmkoo.dss.server.network.core.creator.DssServerCreator;
import com.ztkmkoo.dss.server.network.http.DssHttpServerImpl;
import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 */
public enum DssNetworkType {

    HTTP(DssHttpServerImpl::new, HttpMasterActor.create(), "http-master"),
    ;

    @Getter
    private final DssServerCreator creator;

    @Getter
    private final Behavior<? extends ServerMessages.Req> masterActorCreator;

    @Getter
    private final String masterActorName;

    <T extends ServerMessages.Req> DssNetworkType(
            DssServerCreator creator,
            Behavior<T> masterActorCreator,
            String masterActorName) {
        this.creator = creator;
        this.masterActorCreator = masterActorCreator;
        this.masterActorName = masterActorName;
    }

    public <T> SpawnProtocol.Spawn spawnMessage(ActorRef<T> replyTo) {
        return new SpawnProtocol.Spawn(masterActorCreator, masterActorName, Props.empty(),replyTo);
    }
}
