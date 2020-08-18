package io.github.ztkmkoo.dss.core.util;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-19 01:03
 */
public class ObjectUtils {

    private ObjectUtils() {}

    public static <T> T getOrDefaultValue(T o, T defaultValue) {
        if (Objects.nonNull(o)) {
            return o;
        }

        return Objects.requireNonNull(defaultValue);
    }

    public static String getNonEmptyOrDefaultValue(String o, String defaultValue) {
        if (!StringUtils.isEmpty(o)) {
            return o;
        }

        return Objects.requireNonNull(defaultValue);
    }
}
