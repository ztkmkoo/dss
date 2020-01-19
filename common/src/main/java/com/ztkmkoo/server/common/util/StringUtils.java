package com.ztkmkoo.server.common.util;

import java.util.Objects;

public class StringUtils {

    private StringUtils() {}

    public static boolean isEmpty(final String string) {

        return Objects.isNull(string) || string.isEmpty();
    }
}
