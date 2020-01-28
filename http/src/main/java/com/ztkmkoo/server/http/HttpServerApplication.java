package com.ztkmkoo.server.http;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.ztkmkoo.server.http.actor.system.ServerActor;
import com.ztkmkoo.server.http.actor.system.message.SystemRunHttpServerMessage;
import scala.concurrent.Future;

import java.io.IOException;

public class HttpServerApplication {

    public void run() {
        run(ActorSystem.create());
    }

    public void run(final String systemName) {
        run(ActorSystem.create(systemName));
    }

    private void run(final ActorSystem actorSystem) {

        final ActorRef serverActor = actorSystem.actorOf(ServerActor.props(), "http-server");
        serverActor.tell(
                SystemRunHttpServerMessage
                        .builder()
//                        .host("127.0.0.1")
//                        .port(8181)
                        .build(),
                ActorRef.noSender()
        );

        systemTerminateProcess(actorSystem);
    }

    private void systemTerminateProcess(final ActorSystem actorSystem) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("Press any key to exit..");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Future f = actorSystem.terminate();
        int checkCount = 0;
        while (checkCount++ < 10) {

            if (f.isCompleted()) {
                break;
            }

            System.out.println("Waiting for actor system is terminated.. " + checkCount);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Shutdown application..");
    }
}
