package com.ztkmkoo.dss.server.actor.http;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import com.ztkmkoo.dss.server.message.HttpMessages;
import org.junit.After;
import org.junit.Test;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오후 6:30
 */
public class HttpRequestHandlerActorTest {

    private static final ActorTestKit testKit = ActorTestKit.create();

    @After
    public void cleanup() {
        testKit.shutdownTestKit();
    }

    @Test
    public void test() {

        final TestProbe<HttpMessages.Response> httpResponseHandler = testKit.createTestProbe();

        final ActorRef<HttpMessages.Request> httpRequestHandler = testKit.spawn(HttpRequestHandlerActor.create(httpResponseHandler.getRef()));

        httpRequestHandler.tell(new HttpMessages.Request("hi", null));

        httpResponseHandler.expectMessageClass(HttpMessages.Response.class);
    }
}