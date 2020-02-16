package com.ztkmkoo.dss.server.actor;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import org.junit.AfterClass;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertTrue;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 17. 오전 1:38
 */
public abstract class AbstractActorTest<T> {

    protected static final ActorTestKit testKit = ActorTestKit.create();

    protected abstract Class<T> getActorClass();

    @AfterClass
    public static void cleanup() {
        testKit.system().log().info("cleanup");
        testKit.shutdownTestKit();
    }

    @Test(expected = InstantiationException.class)
    public void constructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Constructor<T> constructor = getActorClass().getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
