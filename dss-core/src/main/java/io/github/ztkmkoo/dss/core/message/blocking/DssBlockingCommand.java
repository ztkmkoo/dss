package io.github.ztkmkoo.dss.core.message.blocking;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.DssCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-12 23:48
 */
public interface DssBlockingCommand extends DssCommand {

    /**
     * For tracing back message
     * @return message sequence
     */
    long getSeq();

    /**
     * Sender actor ref for response
     * @return sender actor ref
     */
    ActorRef<DssBlockingRestCommand> getSender();
}
