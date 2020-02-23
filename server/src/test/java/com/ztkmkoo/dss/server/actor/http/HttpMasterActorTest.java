package com.ztkmkoo.dss.server.actor.http;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import com.ztkmkoo.dss.server.actor.AbstractActorTest;
import com.ztkmkoo.dss.server.message.HttpMessages;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 17. 오전 1:36
 */
public class HttpMasterActorTest extends AbstractActorTest<HttpMasterActor> {

    @Override
    protected Class<HttpMasterActor> getActorClass() {
        return HttpMasterActor.class;
    }

    @Test
    public void create() {

        final Behavior<HttpMessages.Request> behavior = HttpMasterActor.create();
        assertNotNull(behavior);
    }

    @Override
    @Test(expected = IllegalArgumentException.class)
    public void constructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Constructor<HttpMasterActor> constructor = getActorClass().getDeclaredConstructor(ActorContext.class);
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void tell() throws InterruptedException {

        final ActorRef<HttpMessages.Request> httpMaster = testKit.spawn(HttpMasterActor.create());
        httpMaster.tell(new HttpMessages.Request(null, "/hi", "Hello World"));

        assertTrue(true);
    }
}