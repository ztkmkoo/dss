package com.ztkmkoo.dss.core.actor.rest.entity;

import com.ztkmkoo.dss.core.actor.rest.exception.InvalidRestPathException;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 14. 오후 9:50
 */
@Getter(value = AccessLevel.PACKAGE)
class DssRestPathElement {

    private static final char FIRST_BRACKET = '{';
    private static final char LAST_BRACKET = '}';

    private final String pathElement;
    private final boolean fixed;

    DssRestPathElement(String pathElement) {
        this.pathElement = pathElement;
        this.fixed = isFixedPathElement(pathElement);
    }

    private static boolean isFixedPathElement(String pathElement) {

        if (Objects.isNull(pathElement)) {
            throw new InvalidRestPathException("pathElement is null");
        }

        if (pathElement.length() <= 1) {
            return true;
        }

        final char first = pathElement.charAt(0);
        final char last = pathElement.charAt(pathElement.length() - 1);

        return first != FIRST_BRACKET && last != LAST_BRACKET;
    }
}
