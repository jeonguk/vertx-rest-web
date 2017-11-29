package com.jeonguk.vertx;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class SimpleServerVerticleTest {
	
	private Vertx vertx;

	@Before
	public void setup(TestContext testContext) {

		VertxOptions options = new VertxOptions(); 
		options.setMaxEventLoopExecuteTime(Long.MAX_VALUE);
		
		vertx = Vertx.vertx(options);

		vertx.deployVerticle(SimpleServerVerticle.class.getName(), testContext.asyncAssertSuccess());
	}

	@After
	public void tearDown(TestContext testContext) {
		vertx.close(testContext.asyncAssertSuccess());
	}

	@Test
	public void whenReceivedResponse_thenSuccess(TestContext testContext) {
		final Async async = testContext.async();

		vertx.createHttpClient().getNow(8080, "localhost", "/", response -> response.handler(responseBody -> {
			testContext.assertTrue(responseBody.toString().contains("Welcome"));
			async.complete();
		}));
	}

}