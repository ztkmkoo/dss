package com.ztkmkoo.dss.server.message;

import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.server.enumeration.DssNetworkType;
import lombok.Getter;

import java.io.Serializable;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오후 8:13
 */
public class MasterMessages {

    private MasterMessages() {}

    public static class Init implements Serializable {

        private static final long serialVersionUID = 8165911047370475489L;

        @Getter
        private final DssNetworkType networkType;
        @Getter
        private final ActorRef<MasterMessages.InitResult> replyTo;

        public Init(DssNetworkType networkType, ActorRef<MasterMessages.InitResult> replyTo) {
            this.networkType = networkType;
            this.replyTo = replyTo;
        }
    }

    public static class InitResult implements Serializable {

        private static final long serialVersionUID = 2239256439425480475L;

    }
}
