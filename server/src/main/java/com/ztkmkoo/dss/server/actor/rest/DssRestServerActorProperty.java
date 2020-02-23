package com.ztkmkoo.dss.server.actor.rest;

import akka.actor.typed.Behavior;
import com.ztkmkoo.dss.server.actor.core.DssServerActorProperty;
import com.ztkmkoo.dss.server.message.ServerMessages;
import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 24. 오전 1:17
 */
public class DssRestServerActorProperty implements DssServerActorProperty {

    private static final long serialVersionUID = 8314671790013566035L;

    @Getter
    private final Behavior<ServerMessages.Req> masterBehavior;

    @Getter
    private final String masterName;

    private DssRestServerActorProperty(Builder builder) {
        this.masterBehavior = builder.masterBehavior;
        this.masterName = builder.masterName;
    }

    public static Builder builder(Behavior<ServerMessages.Req> masterBehavior) {
        return new Builder(masterBehavior);
    }

    public static Builder builder(Behavior<ServerMessages.Req> masterBehavior, String masterName) {
        return new Builder(masterBehavior, masterName);
    }

    public static class Builder {

        private static final String MASTER_NAME = "rest-master";

        private final Behavior<ServerMessages.Req> masterBehavior;
        private final String masterName;

        private Builder(Behavior<ServerMessages.Req> masterBehavior, String masterName) {
            this.masterBehavior = masterBehavior;
            this.masterName = masterName;
        }

        private Builder(Behavior<ServerMessages.Req> masterBehavior) {
            this(masterBehavior, MASTER_NAME);
        }

        public DssRestServerActorProperty build() {
            return new DssRestServerActorProperty(this);
        }
    }
}
