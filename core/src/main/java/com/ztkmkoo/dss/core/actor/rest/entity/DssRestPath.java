package com.ztkmkoo.dss.core.actor.rest.entity;

import com.ztkmkoo.dss.core.actor.rest.exception.InvalidRestPathException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 14. 오후 9:50
 */
public class DssRestPath {

    private static final Logger LOGGER = LoggerFactory.getLogger(DssRestPath.class);

    private static final char PATH_DELIMITER_CHAR = '\\';
    private static final String PATH_DELIMITER = "\\";

    private final List<DssRestPathElement> elementList;

    public DssRestPath(String path) {
        this.elementList = elementList(path);
    }

    private static List<DssRestPathElement> elementList(String path) {
        if (Objects.isNull(path)) {
            throw new InvalidRestPathException("path cannot be null");
        }

        final String fixedPath = getFixedPath(path);
        final String[] splits = fixedPath.split("\\\\");

        return elementList(splits, path);
    }

    private static String getFixedPath(String path) {
        if (path.length() == 0) {
            return PATH_DELIMITER + path;
        }

        if (path.charAt(0) == PATH_DELIMITER_CHAR) {
            return path;
        }

        return PATH_DELIMITER + path;
    }

    private static List<DssRestPathElement> elementList(String[] splits, String path) {
        final int length = splits.length;
        if (length <= 1) {
            throw new InvalidRestPathException("service path is too short: " + length);
        }

        final List<DssRestPathElement> list = new ArrayList<>(length);
        for (int i = 1; i < length; i++) {
            final String s = splits[i];
            Objects.requireNonNull(s);

            if (i == 1) {
                list.add( new DssRestPathElement(s));
            } else if (i == length - 1) {
                if (!s.isEmpty()) {
                    list.add( new DssRestPathElement(s));
                } else {
                    LOGGER.warn("last path element is empty. Try to remove the last path delimiter: {}", path);
                }
            } else {
                if (s.isEmpty()) {
                    throw new InvalidRestPathException("Rest uri path format is invalid. Middle path element on " + i + "(th) is Empty");
                } else {
                    list.add( new DssRestPathElement(s));
                }
            }
        }

        if (list.isEmpty()) {
            throw new InvalidRestPathException("Path is not invalid: " + path);
        }

        return list;
    }

    List<DssRestPathElement> getElementList() {
        return elementList;
    }
}
