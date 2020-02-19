package com.ztkmkoo.dss.server.network.rest.handler;

import java.util.Objects;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 20. 오전 12:43
 */
public interface DssRestUriHandler {

    String getUri();

    default boolean sameUri(String uri) {

        final String myUri = getFixedUri(getUri());
        final String targetUri = getFixedUri(uri);

        if (myUri.equals(targetUri)) {
            return true;
        }

        final String[] mySplits = myUri.split("/");
        final String[] targetSplits = targetUri.split("/");

        if (mySplits.length != targetSplits.length) {
            return false;
        }

        final int count = mySplits.length;
        for (int i = 0; i < count; i++) {
            final String s = mySplits[i];
            if (isPathVariable(s)) {
                continue;
            }

            final String t = targetSplits[i];
            if (!s.equals(t)) {
                return false;
            }
        }

        return true;
    }

    static String getFixedUri(String uri) {

        Objects.requireNonNull(uri);

        final int startIndex = uri.charAt(0) == '/' ? 1 : 0;
        final int lastIndex = uri.length() - 1;
        final int queryIndex = uri.indexOf('?');
        if (queryIndex < 0) {
            return uri.substring(startIndex, uri.charAt(lastIndex) == '/' ? lastIndex : uri.length());
        }

        return uri.substring(startIndex, uri.charAt(queryIndex - 1) == '/' ? queryIndex - 1 : queryIndex);
    }

    static boolean isPathVariable(String s) {
        return s.charAt(0) == '{' && s.charAt(s.length() - 1) == '}';
    }
}
