package com.ztkmkoo.dss.core.actor.rest;

import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommand;

import java.util.*;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 14. 오후 8:44
 */
public class DssRestPathResolver {

    private final Map<String, ActorRef<DssRestServiceActorCommand>> fixedGetServiceActorMap;

    private DssRestPathResolver(Map<String, ActorRef<DssRestServiceActorCommand>> fixedGetServiceActorMap) {
        this.fixedGetServiceActorMap = Collections.unmodifiableMap(new HashMap<>(fixedGetServiceActorMap));
    }

    public Optional<ActorRef<DssRestServiceActorCommand>> getServiceActorByPath(String path) {
        return Optional.ofNullable(fixedGetServiceActorMap.get(path));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Map<String, ActorRef<DssRestServiceActorCommand>> serviceActorMap = new HashMap<>();

        public Builder addGetServiceActor(String path, ActorRef<DssRestServiceActorCommand> serviceActor) {
            Objects.requireNonNull(path);
            Objects.requireNonNull(serviceActor);
            serviceActorMap.put(path, serviceActor);
            return this;
        }

        public DssRestPathResolver build() {
            return new DssRestPathResolver(serviceActorMap);
        }
    }
}
