package com.ztkmkoo.dss.core.util;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 14. 오후 10:00
 */
public class StringUtils {

    private StringUtils() {}

    public static boolean isEmpty(String s) {
        return Objects.isNull(s) || s.isEmpty();
    }
}
