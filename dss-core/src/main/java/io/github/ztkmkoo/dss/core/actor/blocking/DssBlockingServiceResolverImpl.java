package io.github.ztkmkoo.dss.core.actor.blocking;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingCommand;
import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingRestCommand;
import io.github.ztkmkoo.dss.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-18 02:11
 */
public class DssBlockingServiceResolverImpl implements DssBlockingServiceResolver {

    private static <T extends DssBlockingCommand> ActorRef<T> getBlockingServiceActor(Logger logger, Map<String, ActorRef<T>> map, String name) {
        if (StringUtils.isEmpty(name) || !map.containsKey(name)) {
            logger.debug("Not exist blocking service {}", name);
            return null;
        }

        return map.get(name);
    }

    private final Logger logger = LoggerFactory.getLogger(DssBlockingServiceResolverImpl.class);
    private final Map<String, ActorRef<DssBlockingRestCommand>> blockingHttpClientServiceMap;

    public DssBlockingServiceResolverImpl() {
        this.blockingHttpClientServiceMap = new HashMap<>();
    }

    public DssBlockingServiceResolverImpl(DssBlockingServiceResolverImpl resolver) {
        this.blockingHttpClientServiceMap = resolver.blockingHttpClientServiceMap;
    }

    @Override
    public ActorRef<DssBlockingRestCommand> getBlockingHttpClientServiceActor(String name) {
        return getBlockingServiceActor(logger, blockingHttpClientServiceMap, name);
    }

    @Override
    public DssBlockingServiceResolverImpl addDssHttpClientService(String name, ActorRef<DssBlockingRestCommand> actorRef) {
        if (blockingHttpClientServiceMap.containsKey(name)) {
            logger.warn("Duplicated blocking service name {} and skipped", name);
            return this;
        }
        blockingHttpClientServiceMap.put(name, actorRef);
        return this;
    }
}
