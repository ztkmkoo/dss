package com.ztkmkoo.dss.server.network.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztkmkoo.dss.server.network.core.creator.DssChannelInitializerCreator;
import com.ztkmkoo.dss.server.network.rest.entity.DssRestResponse;
import com.ztkmkoo.dss.server.network.rest.entity.DssRestSuccessResponse;
import com.ztkmkoo.dss.server.network.rest.handler.DssRestChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import static org.junit.Assert.*;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 19. 오전 12:22
 */
public class DssRestChannelTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void bind() throws InterruptedException, IOException {

        final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            bindServer(bossGroup, workerGroup, DssRestChannelInitializer::new, 8181);
            assertTrue(serverPortIsOpen(8181));
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @Test(expected = SocketTimeoutException.class)
    public void bindAndSendTimeout() throws InterruptedException, IOException {

        final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            bindServer(bossGroup, workerGroup, DssRestChannelInitializer::new, 8181);
            sendMessageToServer(8181);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @Test
    public void bindAndSendGetHttpAndReceiveNotBindPath() throws InterruptedException, IOException {

        final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            bindServer(bossGroup, workerGroup, DssRestChannelInitializer::new, 8181);

            final String url = "http://localhost:8181/test";

            final HttpURLConnection httpClient =
                    (HttpURLConnection) new URL(url).openConnection();

            // optional default is GET
            httpClient.setRequestMethod("GET");

            //add request header
            httpClient.setRequestProperty("Content-Type", "application/json");

            final int responseCode = httpClient.getResponseCode();
            assertEquals(400, responseCode);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @Test
    public void bindAndSendGetHttpAndReceive() throws InterruptedException, IOException {

        final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            bindServer(bossGroup, workerGroup, DssRestChannelInitializer::new, 8181);

            final String url = "http://localhost:8181/hello/pure";

            final HttpURLConnection httpClient =
                    (HttpURLConnection) new URL(url).openConnection();

            // optional default is GET
            httpClient.setRequestMethod("GET");

            //add request header
            httpClient.setRequestProperty("Content-Type", "application/json");

            final int responseCode = httpClient.getResponseCode();
            assertEquals(200, responseCode);

            final DssRestResponse response = getHttpResponse(httpClient);
            assertNotNull(response);
            assertTrue(response.isSuccessful());
            System.out.println(response);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private void bindServer(
            NioEventLoopGroup bossGroup,
            NioEventLoopGroup workerGroup,
            DssChannelInitializerCreator dssChannelInitializerCreator,
            int port
    ) throws InterruptedException {

        final DssRestChannel dssRestChannel = new DssRestChannel();

        final ServerBootstrap b = new ServerBootstrap()
                .group(bossGroup, workerGroup);

        final DssRestChannelProperty property = DssRestChannelProperty
                .builder(dssChannelInitializerCreator)
                .port(port)
                .build();

        final Channel channel = dssRestChannel.bind(b, property, null);
        channel.closeFuture();
    }

    private boolean serverPortIsOpen(int port) throws IOException {

        final Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", port));
        return socket.isConnected();
    }

    private static void sendMessageToServer(int port) throws IOException {
        sendMessageToServer(port, "Hi");
    }

    private static void sendMessageToServer(int port, String msg) throws IOException {

        final Socket socket = connectServer("127.0.0.1", port);

        if (socket.isConnected()) {
            final DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            outputStream.writeBytes(msg);
            outputStream.flush();

            final String response = reader.readLine();
            System.err.println(response);
        }
    }

    private static Socket connectServer(String host, int port) throws IOException {

        final Socket socket = new Socket();
        socket.setSoTimeout(3000);
        socket.connect(new InetSocketAddress(host, port));

        return socket;
    }

    private static DssRestResponse getHttpResponse(HttpURLConnection httpClient) throws IOException {

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode jsonNode = mapper.readTree(response.toString());
            final int status = jsonNode.get("status").asInt();
            final String message = jsonNode.get("message").asText();
            return new DssRestSuccessResponse(status, message);
        }
    }
}