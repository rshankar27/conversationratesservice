package in.nl;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import redis.clients.jedis.Jedis;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(VertxUnitRunner.class)
public class VertxTest {

    private Vertx vertx;
    private Jedis mockJedis = mock(Jedis.class);

    @Before
    public void setup(TestContext testContext) {
        vertx = Vertx.vertx();
        MyServiceVerticle serviceVerticle = new MyServiceVerticle(mockJedis,"sample");
        when(mockJedis.incr("hits")).thenReturn(1l);
        vertx.deployVerticle(serviceVerticle,testContext.asyncAssertSuccess());
    }

    @Test
    public void testSomething(TestContext context) {
        context.assertFalse(false);
    }

    @Test
    public void whenReceivedResponse_thenSuccess(TestContext testContext) {
        Async async = testContext.async();
        vertx.createHttpClient()
                .getNow(9090, "localhost", "/", response -> {
                    response.handler(responseBody -> {
                        testContext.assertTrue(responseBody.toString().contains("Welcome to Vert.x Intro. Page hits: "+1+". Request handled by sample"));
                        async.complete();
                    });
                });
    }

    @After
    public void tearDown(TestContext testContext) {
        vertx.close(testContext.asyncAssertSuccess());
    }
}