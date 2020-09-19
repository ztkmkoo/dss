package io.github.ztkmkoo.dss.core.actor;

import java.util.Map;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-15 03:00
 */
public interface DssServiceAcceptable {

    Map<String, DssServiceActorResolvable<String>> getServiceActorMap();

    void putServiceActorResolvable(String key, DssServiceActorResolvable<String> value);
}
