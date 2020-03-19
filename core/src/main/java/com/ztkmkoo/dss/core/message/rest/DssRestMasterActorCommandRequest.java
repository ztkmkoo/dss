package com.ztkmkoo.dss.core.message.rest;

import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 10:07
 */
@Getter
public class DssRestMasterActorCommandRequest implements DssRestMasterActorCommand {

    private static final long serialVersionUID = 6046370337632314401L;
    private final String channelId;
    private final ActorRef<DssRestChannelHandlerCommand> sender;
    private final DssRestMethodType methodType;
    private final String path;

    @Builder
    DssRestMasterActorCommandRequest(
            String channelId,
            ActorRef<DssRestChannelHandlerCommand> sender,
            DssRestMethodType methodType,
            String path) {
        this.channelId = channelId;
        this.sender = sender;
        this.methodType = methodType;
        this.path = path;
    }

    @Override
    public String toString() {
        return "DssRestMasterActorCommandRequest{" +
                "channelId: '" + channelId + "', " +
                "sender: '" + (Objects.nonNull(sender)? sender.path().name() : "null") + "', " +
                "methodType: '" + methodType.name() + "', " +
                "path: '" + path + "'" +
                "}";
    }
}
