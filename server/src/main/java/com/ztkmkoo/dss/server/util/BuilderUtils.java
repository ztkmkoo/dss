package com.ztkmkoo.dss.server.util;

import io.netty.util.internal.StringUtil;

import java.util.Collection;
import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 17. 오후 11:06
 */
public class BuilderUtils {

    private BuilderUtils() {}

    public static <T extends Object> T getDefaultValueIfEmpty(T v, T defaultValue) {
        Objects.requireNonNull(defaultValue);
        return Objects.isNull(v) ? defaultValue : v;
    }

    public static String getDefaultValueIfEmpty(String v, String defaultValue) {

        if (StringUtil.isNullOrEmpty(defaultValue)) {
            throw new NullPointerException("default value cannot be null or empty");
        }
        return StringUtil.isNullOrEmpty(v) ? defaultValue : v;
    }

    public static <T extends Collection> T getDefaultValueIfEmpty(T v, T defaultValue) {

        Objects.requireNonNull(defaultValue);
        return Objects.isNull(v) ? defaultValue : v;
    }
}
