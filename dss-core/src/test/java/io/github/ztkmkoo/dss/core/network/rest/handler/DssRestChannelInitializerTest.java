package io.github.ztkmkoo.dss.core.network.rest.handler;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import io.github.ztkmkoo.dss.core.actor.exception.DssUserActorDuplicateBehaviorCreateException;
import io.github.ztkmkoo.dss.core.message.rest.DssRestChannelInitializerCommand;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 2. 오전 1:07
 */
class DssRestChannelInitializerTest {

    private static final ActorTestKit testKit = ActorTestKit.create();

    @Mock
    private SocketChannel socketChannel;

    @Mock
    private ChannelPipeline channelPipeline;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterAll
    static void cleanUp() {
        testKit.shutdownTestKit();
    }

    @Test
    void create() {

        final DssRestChannelInitializer dssRestChannelInitializer = new DssRestChannelInitializer(Collections.emptyList());
        final Behavior<DssRestChannelInitializerCommand> behavior = dssRestChannelInitializer.create();

        assertNotNull(behavior);

        final ActorRef<DssRestChannelInitializerCommand> actorRef = testKit.spawn(behavior);
        assertNotNull(actorRef);
    }

    @Test
    void createTwiceForOneObject() {
        final DssRestChannelInitializer dssRestChannelInitializer = new DssRestChannelInitializer(Collections.emptyList());
        dssRestChannelInitializer.create();

        assertThrows(DssUserActorDuplicateBehaviorCreateException.class, dssRestChannelInitializer::create);
    }

    @Test
    void initChannel() throws Exception {

        Mockito
                .when(socketChannel.pipeline())
                .thenReturn(channelPipeline);

        Mockito
                .when(channelPipeline.addLast(any()))
                .thenReturn(channelPipeline);

        Mockito
                .when(channelPipeline.first())
                .thenReturn(new HttpRequestDecoder());

        final DssRestChannelInitializer dssRestChannelInitializer = Mockito.mock(DssRestChannelInitializer.class);
        dssRestChannelInitializer.create();

        dssRestChannelInitializer.initChannel(socketChannel);

        final ChannelPipeline p = socketChannel.pipeline();
        assertEquals(HttpRequestDecoder.class, p.first().getClass());
    }
}