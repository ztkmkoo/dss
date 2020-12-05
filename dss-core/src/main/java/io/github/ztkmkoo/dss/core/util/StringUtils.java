package io.github.ztkmkoo.dss.core.util;

import io.github.ztkmkoo.dss.core.exception.DssStringEmptyException;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 14. 오후 10:00
 */
public class StringUtils {

    private StringUtils() {

    }

    public static boolean isEmpty(String s) {
        return Objects.isNull(s) || s.isEmpty();
    }

    public static String requireNonEmpty(String s) {
        if (StringUtils.isEmpty(s)) {
            throw new DssStringEmptyException();
        }

        return s;
    }
}
