import io.github.ztkmkoo.dss.server.rest.DssRestServer;

public class Application {
    public static void main(String[] args) throws InterruptedException {

        DssRestServer dssRestServer = new DssRestServer("127.0.0.1", 8181);

        dssRestServer
                .addDssRestService(new HelloService());
        dssRestServer.start();
    }
}