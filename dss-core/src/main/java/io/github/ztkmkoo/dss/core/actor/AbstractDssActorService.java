package io.github.ztkmkoo.dss.core.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.javadsl.ActorContext;
import io.github.ztkmkoo.dss.core.actor.blocking.DssBlockingServiceResolver;
import io.github.ztkmkoo.dss.core.actor.blocking.DssBlockingServiceResolverReadOnlyImpl;
import io.github.ztkmkoo.dss.core.message.DssCommand;
import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingRestCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestMasterActorCommand;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-18 23:56
 */
public abstract class AbstractDssActorService<M extends DssCommand, R extends DssRestMasterActorCommand> implements DssActorService<M> {

    private ActorRef<M> actorRef;

    @Getter(AccessLevel.PROTECTED)
    private Logger log;

    private DssBlockingServiceResolver blockingServiceResolver;

    public abstract void onReceive(R req);

    public final void initializeFromActor(ActorContext<M> context) {
        Objects.requireNonNull(context);

        this.actorRef = context.getSelf();
        this.log = context.getLog();
    }

    @Override
    public final void finish(M res) {
        actorRef.tell(res);
    }

    @Override
    public void setBlockingServiceResolver(DssBlockingServiceResolver resolver) {
        if (Objects.nonNull(this.blockingServiceResolver)) {
            this.getLog().warn("Duplicated request. Blocking service resolver is already initialized.");
        }
        this.blockingServiceResolver = new DssBlockingServiceResolverReadOnlyImpl(resolver);
    }

    protected <R extends DssBlockingRestCommand.HttpRequest> void httpClientRequest(String service, R request) {
        Objects.requireNonNull(blockingServiceResolver);

        final ActorRef<DssBlockingRestCommand> serviceActor = blockingServiceResolver.getBlockingHttpClientServiceActor(service);
        serviceActor.tell(request);
    }
}
