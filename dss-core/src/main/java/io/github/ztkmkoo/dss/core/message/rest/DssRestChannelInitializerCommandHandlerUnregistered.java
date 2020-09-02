package io.github.ztkmkoo.dss.core.message.rest;

import akka.actor.typed.ActorRef;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 15. 오전 2:33
 */
@Getter
public class DssRestChannelInitializerCommandHandlerUnregistered implements DssRestChannelInitializerCommand {
    private static final long serialVersionUID = -4010892587871866884L;

    private final String name;
    private final ActorRef<DssRestChannelHandlerCommand> handlerActor;

    @Builder
    private DssRestChannelInitializerCommandHandlerUnregistered(String name, ActorRef<DssRestChannelHandlerCommand> handlerActor) {
        this.name = name;
        this.handlerActor = handlerActor;
    }

    @Override
    public String toString() {
        return "DssRestChannelInitializerCommandHandlerUnregistered{" +
                "name: '" + name + "', " +
                "handlerActor: '" + (Objects.nonNull(handlerActor) ? handlerActor.path().name() : "null") + "'" +
                "}";
    }
}
