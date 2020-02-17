package com.ztkmkoo.dss.server.core;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertTrue;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 17. 오전 2:13
 */
public class DssServerConfigTest {

    @Test(expected = IllegalAccessException.class)
    public void constructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Constructor<DssServerConfig> constructor = DssServerConfig.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.newInstance();

    }
}