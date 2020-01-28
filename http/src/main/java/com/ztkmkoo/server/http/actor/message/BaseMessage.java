package com.ztkmkoo.server.http.actor.message;

import java.io.Serializable;
import java.util.Objects;

public abstract class BaseMessage implements Serializable {

    protected static <T> T getNotNullableValue(final T value, final T defaultValue) {

        if (Objects.nonNull(value)) {
            return value;
        }

        Objects.requireNonNull(defaultValue);
        return defaultValue;
    }
}
