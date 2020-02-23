package com.ztkmkoo.dss.server.actor.core;

import akka.actor.typed.Behavior;
import com.ztkmkoo.dss.server.message.ServerMessages;

import java.io.Serializable;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오후 8:01
 */
public interface DssServerActorProperty extends Serializable {

    /**
     * @return : master request handler actor behavior
     */
    Behavior<ServerMessages.Req> getMasterBehavior();

    /**
     * @return : master request handler actor name
     */
    String getMasterName();
}
