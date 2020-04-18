package com.ztkmkoo.dss.core.actor.rest;

import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestPath;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestPathComposition;
import com.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import com.ztkmkoo.dss.core.actor.rest.util.DssRestPathUtils;
import com.ztkmkoo.dss.core.exception.DssRestServiceMappingException;
import com.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommand;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 14. 오후 8:44
 */
public class DssRestPathResolver {

    private static final Logger logger = LoggerFactory.getLogger(DssRestPathResolver.class);

    private final Map<DssRestMethodType, Map<String, DssRestPathComposition>> staticPathCompositionMap;
    private final Map<DssRestMethodType, List<DssRestPathComposition>> dynamicPathCompositionMap;

    private DssRestPathResolver(Builder builder) {
        this.staticPathCompositionMap = Collections.unmodifiableMap(builder.staticPathCompositionMap);
        this.dynamicPathCompositionMap = Collections.unmodifiableMap(builder.dynamicPathCompositionMap);
    }

    public Optional<ActorRef<DssRestServiceActorCommand>> getPathServiceActor(DssRestMethodType methodType, String path) {
        final String fixedPath = DssRestPathUtils.getFixedPath(path);
        final Optional<ActorRef<DssRestServiceActorCommand>> fixedOptional = getStaticPathServiceActor(methodType, fixedPath);
        if (fixedOptional.isPresent()) {
            return fixedOptional;
        }

        return getDynamicPathServiceActor(methodType, fixedPath);
    }

    private Optional<ActorRef<DssRestServiceActorCommand>> getStaticPathServiceActor(DssRestMethodType methodType, String fixedPath) {
        if (!staticPathCompositionMap.containsKey(methodType)) {
            return Optional.empty();
        }

        final Map<String, DssRestPathComposition> map = staticPathCompositionMap.get(methodType);

        if (!map.containsKey(fixedPath)) {
            return Optional.empty();
        }

        return Optional.of(map.get(fixedPath).getServiceActor());
    }

    private Optional<ActorRef<DssRestServiceActorCommand>> getDynamicPathServiceActor(DssRestMethodType methodType, String fixedPath) {
        if (!dynamicPathCompositionMap.containsKey(methodType)) {
            return Optional.empty();
        }

        final List<DssRestPathComposition> list = dynamicPathCompositionMap.get(methodType);
        if (list.isEmpty()) {
            return Optional.empty();
        }

        for (DssRestPathComposition composition : list) {
            if(composition.getDssRestPath().match(fixedPath)) {
                return Optional.of(composition.getServiceActor());
            }
        }

        return Optional.empty();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Map<DssRestMethodType, Map<String, DssRestPathComposition>> staticPathCompositionMap = new EnumMap<>(DssRestMethodType.class);
        private final Map<DssRestMethodType, List<DssRestPathComposition>> dynamicPathCompositionMap = new EnumMap<>(DssRestMethodType.class);

        public Builder addService(DssRestActorService service, ActorRef<DssRestServiceActorCommand> serviceActor) {
            Objects.requireNonNull(service);
            Objects.requireNonNull(service.getMethodType());
            Objects.requireNonNull(service.getPath());
            Objects.requireNonNull(serviceActor);

            final String fixedPath = DssRestPathUtils.getFixedPath(service.getPath());
            if (DssRestPathUtils.isDynamicPath(fixedPath)) {
                addDynamicPathService(service, serviceActor);
            } else {
                addStaticPathService(service, serviceActor);
            }

            return this;
        }

        private void addStaticPathService(DssRestActorService service, ActorRef<DssRestServiceActorCommand> serviceActor) {
            if(!staticPathCompositionMap.containsKey(service.getMethodType())) {
                staticPathCompositionMap.put(service.getMethodType(), new HashMap<>());
            }

            final Map<String, DssRestPathComposition> map = staticPathCompositionMap.get(service.getMethodType());
            final String path = service.getPath();
            if (map.containsKey(path)) {
                throw new DssRestServiceMappingException("Cannot map same path: " + path);
            }

            map.put(
                    service.getPath(),
                    DssRestPathComposition
                            .builder()
                            .serviceActor(serviceActor)
                            .dssRestPath(
                                    DssRestPath
                                            .builder()
                                            .path(path)
                                            .build()
                            )
                            .build()
            );
        }

        private void addDynamicPathService(DssRestActorService service, ActorRef<DssRestServiceActorCommand> serviceActor) {
            if(!dynamicPathCompositionMap.containsKey(service.getMethodType())) {
                dynamicPathCompositionMap.put(service.getMethodType(), new ArrayList<>());
            }

            final List<DssRestPathComposition> list = dynamicPathCompositionMap.get(service.getMethodType());
            final String path = service.getPath();

            list.add(
                    DssRestPathComposition
                            .builder()
                            .serviceActor(serviceActor)
                            .dssRestPath(
                                    DssRestPath
                                            .builder()
                                            .path(path)
                                            .build()
                            )
                            .build()
            );
        }

        public DssRestPathResolver build() {
            logStaticMappingInfo();
            logDynamicMappingInfo();

            return new DssRestPathResolver(this);
        }

        private void logStaticMappingInfo() {
            if (staticPathCompositionMap.isEmpty()) {
                return;
            }

            staticPathCompositionMap.forEach((methodType, map) ->
                    map.forEach((path, composition) ->
                            logger.info("Add static mapping: [{}] {} to {}",
                                    methodType, path, composition.getServiceActor().path().name())
                    )
            );
        }

        private void logDynamicMappingInfo() {
            if (dynamicPathCompositionMap.isEmpty()) {
                return;
            }

            dynamicPathCompositionMap.forEach((methodType, map) ->
                    map.forEach(composition ->
                            logger.info("Add static mapping: [{}] {} to {}", methodType,
                                    composition.getDssRestPath().getRowPath(), composition.getServiceActor().path().name())
                    )
            );
        }
    }
}
