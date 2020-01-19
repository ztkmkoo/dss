package com.ztkmkoo.server.common.util;

import java.util.Map;
import java.util.Objects;

public class MapUtils {

    private MapUtils() {}

    public static <K, V> boolean isEmpty(final Map<K, V> map) {

        return Objects.isNull(map) || map.isEmpty();
    }
}
