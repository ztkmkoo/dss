package io.github.ztkmkoo.dss.core.actor.core;

import akka.actor.typed.javadsl.ActorContext;
import io.github.ztkmkoo.dss.core.message.DssCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-22 23:38
 */
abstract class AbstractDssEventGroupActor<T extends DssCommand> extends AbstractDssCoreActor<T> {

    protected AbstractDssEventGroupActor(ActorContext<T> context) {
        super(context);
    }
}
