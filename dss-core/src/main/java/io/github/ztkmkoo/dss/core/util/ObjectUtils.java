package io.github.ztkmkoo.dss.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-09-06 02:07
 */
public class ObjectUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectUtils.class);

    private ObjectUtils() {}

    public static <T> T defaultValueIfNull(T value, T defaultValue, String text) {
        if (Objects.nonNull(value)) {
            return value;
        }

        LOGGER.debug("{} is null. return default value", text);
        return defaultValue;
    }
}
