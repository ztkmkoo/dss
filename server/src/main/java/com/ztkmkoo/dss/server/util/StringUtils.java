package com.ztkmkoo.dss.server.util;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 20. 오전 12:47
 */
public class StringUtils {

    private StringUtils() {}

    public static boolean isEmptyOrNull(String s) {
        return Objects.isNull(s) || s.isEmpty();
    }
}
