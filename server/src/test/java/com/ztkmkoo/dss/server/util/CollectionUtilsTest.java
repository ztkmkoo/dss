package com.ztkmkoo.dss.server.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 13. 오후 10:18
 */
public class CollectionUtilsTest {

    @Test
    public void isEmptyForEmptyList() {
        assertTrue(CollectionUtils.List.isEmpty(Collections.emptyList()));
    }

    @Test
    public void isEmptyForNull() {
        assertTrue(CollectionUtils.List.isEmpty(null));
    }

    @Test
    public void isEmptyForNotEmptyList() {
        final List<Integer> list = Arrays.asList(1, 2, 3, 4);
        assertFalse(CollectionUtils.List.isEmpty(list));
    }
}