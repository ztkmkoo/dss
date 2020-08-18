package io.github.ztkmkoo.dss.core.actor.rest.property;

import io.github.ztkmkoo.dss.core.actor.DssActorProperty;
import io.github.ztkmkoo.dss.core.actor.blocking.rest.DssHttpClientService;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingRestCommand;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-18 01:52
 */
@Getter
public class DssRestActorProperty implements DssActorProperty {

    @SuppressWarnings("squid:S3740")
    private final List<DssRestActorService> serviceList;
    private final List<DssHttpClientService<DssBlockingRestCommand.HttpRequest>> httpClientServiceList;

    @Builder @SuppressWarnings("squid:S3740")
    public DssRestActorProperty(List<DssRestActorService> serviceList, List<DssHttpClientService<DssBlockingRestCommand.HttpRequest>> httpClientServiceList) {
        this.serviceList = getUnModifiableList(serviceList);
        this.httpClientServiceList = getUnModifiableList(httpClientServiceList);
    }

    private static <T> List<T> getUnModifiableList(List<T> list) {
        if (isEmptyList(list)) {
            return Collections.unmodifiableList(Collections.emptyList());
        }

        return Collections.unmodifiableList(list);
    }

    private static <T> boolean isEmptyList(List<T> list) {
        return Objects.isNull(list) || list.isEmpty();
    }
}
