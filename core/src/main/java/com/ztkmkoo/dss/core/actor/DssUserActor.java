package com.ztkmkoo.dss.core.actor;

import akka.actor.typed.Behavior;
import com.ztkmkoo.dss.core.message.DssCommand;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 2. 오전 12:56
 */
public interface DssUserActor<T extends DssCommand> {

    Behavior<T> create();
}
