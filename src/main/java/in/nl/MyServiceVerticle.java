package in.nl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import redis.clients.jedis.Jedis;

public class MyServiceVerticle extends AbstractVerticle {

    private final Jedis jedis;

    public MyServiceVerticle(Jedis redClient){
        this.jedis = redClient;
    }

    public MyServiceVerticle(){
        this.jedis = new Jedis("redis",6379);
    }

    @Override
    public void start(Future<Void> future) {

        Handler<HttpServerRequest> httpServerRequestHandler = r -> {
            r.response().end("Welcome to Vert.x Intro. Page hits: "+jedis.incr("hits"));
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
