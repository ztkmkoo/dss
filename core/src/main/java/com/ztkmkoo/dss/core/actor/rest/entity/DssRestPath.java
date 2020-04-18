package com.ztkmkoo.dss.core.actor.rest.entity;

import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 13. 오전 2:06
 */
@Getter
public class DssRestPath implements Serializable {

    private static final long serialVersionUID = 8287023454874773276L;

    private final transient Logger logger = LoggerFactory.getLogger(DssRestPath.class);

    private static final String PATH_SEPARATOR_REG_EX = "\\/";
    private static final String PATH_NORMAL_REG_EX = "[^\\/]+";
    private static final String PATH_DIGITAL_REG_EX = "(\\d+)";
    private static final String PATH_VARIABLE_REG_EX = "\\{[^\\/]+\\}";
    private static final Pattern PATH_NORMAL_PATTERN = Pattern.compile(PATH_NORMAL_REG_EX);
    private static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile(PATH_VARIABLE_REG_EX);

    private final Pattern pattern;
    private final String rowPath;
    private final Map<Integer, String> staticPathMap = new HashMap<>();

    @Builder
    private DssRestPath(String path, Map<String, Class<? extends Object>> classMap) {
        final String regex = getRegEx(path, Objects.nonNull(classMap) ? classMap : Collections.emptyMap());
        this.pattern = Pattern.compile(regex);
        this.rowPath = path;
        logger.info("Regex: {}", regex);
    }

    private String getRegEx(String path, Map<String, Class<? extends Object>> classMap) {
        final StringBuilder sb = new StringBuilder();

        final List<String> pathElements = pathElements(path);
        for (int i = 0; i < pathElements.size(); i++) {
            final String pathElement = pathElements.get(i);
            if (isPathVariablePattern(pathElement)) {
                final String pathVariableName = pathElement.substring(1, pathElement.length() - 1);

                final Class<? extends Object> tClass = classMap.getOrDefault(pathVariableName, String.class);
                final String regEx = regExFromClassType(tClass);
                sb.append(PATH_SEPARATOR_REG_EX).append(regEx);
            } else {
                sb
                        .append(PATH_SEPARATOR_REG_EX)
                        .append(PATH_NORMAL_REG_EX);
                staticPathMap.put(i, pathElement);
            }
        }

        return sb.append("$").toString();
    }

    public boolean match(String path) {
        Objects.requireNonNull(pattern);
        final String fixedPath = getFixedPath(path);
        final Matcher matcher = pattern.matcher(fixedPath);
        if (!matcher.find()) {
            logger.debug("Path regEx not matched: {}", path);
            return false;
        }

        logger.debug("Path regEx matched, try to check static path element: {}", path);
        return matchStaticPath(fixedPath, staticPathMap);
    }

    private boolean matchStaticPath(String path, Map<Integer, String> staticPathMap) {
        final List<String> pathElements = pathElements(path);
        logger.debug("user request path: {}, expected path: {}", pathElements, rowPath);

        for (Map.Entry<Integer, String> entry : staticPathMap.entrySet()) {
            final int index = entry.getKey();
            final String staticPathElement = entry.getValue();

            if (index >= pathElements.size()) {
                return false;
            }

            final String pathElement = pathElements.get(index);
            if (!staticPathElement.equals(pathElement)) {
                return false;
            }
        }

        return true;
    }

    private static List<String> pathElements(String path) {
        final List<String> list = new ArrayList<>();

        final String fixedPath = getFixedPath(path);

        final Matcher matcher = PATH_NORMAL_PATTERN.matcher(fixedPath);
        while (matcher.find()) {
            list.add(matcher.group());
        }

        return list;
    }

    private static boolean isPathVariablePattern(String pathElement) {
        final Matcher matcher = PATH_VARIABLE_PATTERN.matcher(pathElement);
        return matcher.find();
    }

    private static String regExFromClassType(Class<? extends Object> tClass) {
        if (Number.class.isAssignableFrom(tClass)) {
            return PATH_DIGITAL_REG_EX;
        } else {
            return PATH_NORMAL_REG_EX;
        }
    }

    public static String getFixedPath(String path) {
        if (path.contains("?")) {
            return path.substring(0, path.indexOf('?'));
        } else {
            return path;
        }
    }
}