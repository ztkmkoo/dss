package com.ztkmkoo.dss.core.actor.rest.entity;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 14. 오후 10:33
 */
public class DssRestPathTest {

    @Test
    public void test() {

        final DssRestPath dssRestPath = new DssRestPath("/hi/hello/{good}");
        final List<DssRestPathElement> list = dssRestPath.getElementList();
        assertFalse(list.isEmpty());
    }
}