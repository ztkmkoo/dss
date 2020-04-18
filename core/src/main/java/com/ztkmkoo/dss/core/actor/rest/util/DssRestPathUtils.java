package com.ztkmkoo.dss.core.actor.rest.util;

import com.ztkmkoo.dss.core.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 18. 오전 4:23
 */
public class DssRestPathUtils {

    // normal static string
    private static final String EMPTY_STRING = "";

    // regEx
    private static final String PATH_VARIABLE_REG_EX = "\\{[^\\/]+\\}";

    // regEx pattern
    private static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile(PATH_VARIABLE_REG_EX);

    private DssRestPathUtils() {}

    /**
     * get pure uri path without query params
     * @param path : uri path
     * @return pure uri path
     */
    public static String getFixedPath(String path) {
        if (containRequestParam(path)) {
            return path.substring(0, path.indexOf('?'));
        } else {
            return path;
        }
    }

    /**
     * get query param as string
     * @param path : uri path
     * @return query param string
     */
    public static String getRequestParamString(String path) {
        if (containRequestParam(path)) {
            return path.substring(path.indexOf('?') + 1);
        } else {
            return EMPTY_STRING;
        }
    }

    /**
     * check if contains query params
     * @param path : uri path
     * @return contains query params return true
     */
    public static boolean containRequestParam(String path) {
        return path.contains("?");
    }

    public static Map<String, String> getRequestParams(String query) {
        if (StringUtils.isEmpty(query)) {
            return Collections.emptyMap();
        }

        final String[] queries = query.split("&");
        final Map<String, String> map = new HashMap<>(queries.length);

        for (String q : queries) {
            if (q.contains("=")) {
                final int index = q.indexOf('=');
                final String key = q.substring(0, index);
                final String value = q.substring(index + 1);
                map.put(key, value);
            }
        }

        return map;
    }

    public static boolean isDynamicPath(String path) {
        final String fixedPath = getFixedPath(path);
        final Matcher matcher = PATH_VARIABLE_PATTERN.matcher(fixedPath);
        return matcher.find();
    }
}