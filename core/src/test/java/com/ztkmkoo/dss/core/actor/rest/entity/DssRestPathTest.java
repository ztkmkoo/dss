package com.ztkmkoo.dss.core.actor.rest.entity;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 4. 13. 오후 1:23
 */
public class DssRestPathTest {

    @Test
    public void match() {
        final Map<String, Class<? extends Object>> classMap = new HashMap<>();
        classMap.put("name", String.class);
        classMap.put("age", Integer.class);

        final DssRestPath dssRestPath = DssRestPath
                .builder()
                .path("/hi/{name}/{age}")
                .classMap(classMap)
                .build();

        assertTrue(dssRestPath.match("/hi/Kebron/34"));
    }

    @Test
    public void match2() {
        final Map<String, Class<? extends Object>> classMap = new HashMap<>();
        classMap.put("name", String.class);
        classMap.put("age", Integer.class);

        final DssRestPath dssRestPath = DssRestPath
                .builder()
                .path("/hi/{name}/{age}")
                .classMap(classMap)
                .build();

        assertFalse(dssRestPath.match("/hi/Kebron/thirtyFour"));
    }

    @Test
    public void match3() {
        final Map<String, Class<? extends Object>> classMap = new HashMap<>();
        classMap.put("name", String.class);
        classMap.put("age", Integer.class);

        final DssRestPath dssRestPath = DssRestPath
                .builder()
                .path("/hi/{name}/{age}/test")
                .classMap(classMap)
                .build();

        assertTrue(dssRestPath.match("/hi/Kebron/15/test?id=ztkmkoo"));
    }
}