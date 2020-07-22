package io.github.ztkmkoo.dss.core.network.rest.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import io.github.ztkmkoo.dss.core.actor.exception.DssUserActorDuplicateBehaviorCreateException;
import io.github.ztkmkoo.dss.core.message.rest.DssRestChannelInitializerCommand;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 2. 오전 1:07
 */
public class DssRestChannelInitializerTest {

    private static final ActorTestKit testKit = ActorTestKit.create();

    @Mock
    private SocketChannel socketChannel;

    @Mock
    private ChannelPipeline channelPipeline;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterAll
    public static void cleanUp() {
        testKit.shutdownTestKit();
    }

    @Test
    public void create() {

        final DssRestChannelInitializer dssRestChannelInitializer = new DssRestChannelInitializer(Collections.emptyList());
        final Behavior<DssRestChannelInitializerCommand> behavior = dssRestChannelInitializer.create();

        assertNotNull(behavior);

        final ActorRef<DssRestChannelInitializerCommand> actorRef = testKit.spawn(behavior);
        assertNotNull(actorRef);
    }

    @Test
    public void createTwiceForOneObject() {
        assertThrows(DssUserActorDuplicateBehaviorCreateException.class, () -> {
            final DssRestChannelInitializer dssRestChannelInitializer = new DssRestChannelInitializer(Collections.emptyList());
            dssRestChannelInitializer.create();
            dssRestChannelInitializer.create();
        });
    }

    @Test
    public void initChannel() throws Exception {

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