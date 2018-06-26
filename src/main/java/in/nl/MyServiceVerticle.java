package in.nl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import redis.clients.jedis.Jedis;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class MyServiceVerticle extends AbstractVerticle {

    private final Jedis jedis;
    private final String hostname;

    public MyServiceVerticle(Jedis redClient, String host){
        this.jedis = redClient;
        this.hostname = host;
    }

    public MyServiceVerticle(){
        String hostname1;
        this.jedis = new Jedis("redis",6379);
        try {
            hostname1 = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hostname1 = UUID.randomUUID().toString();
            e.printStackTrace();
        }
        this.hostname = hostname1;
    }

    @Override
    public void start(Future<Void> future) {

        Handler<HttpServerRequest> httpServerRequestHandler = r -> {
            r.response().end("Welcome to Vert.x Intro. Page hits: "+jedis.incr("hits")+". Request handled by "+hostname);
        };

        vertx.createHttpServer()
                .requestHandler(httpServerRequestHandler)
                .listen(config().getInteger("http.port", 9090),
                        result -> {
                            if (result.succeeded()) {
                                future.complete();
                            } else {
                                future.fail(result.cause());
                            }
                        });
    }
}
