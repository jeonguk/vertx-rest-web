package com.jeonguk.vertx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeonguk.vertx.model.Article;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class RestServiceVerticle extends AbstractVerticle {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(RestServiceVerticle.class);
    
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new RestServiceVerticle());
    }
    
    @Override
    public void start(Future<Void> future) {
    	
        Router router = Router.router(vertx);
        router.get("/api/articles/article/:id")
                .handler(this::getArticles);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(config().getInteger("http.port", 8080), result -> {
                    if (result.succeeded()) {
                        future.complete();
                    } else {
                        future.fail(result.cause());
                    }
                });
    }

    @Override
    public void stop() {
        LOGGER.info("Shutting down application");
    }
    
    private void getArticles(RoutingContext routingContext) {

        String articleId = routingContext.request().getParam("id");
        Article article = new Article(articleId, "This is an intro to vertx", "jeonguk", "2017-11-29", 1578);

        routingContext.response()
                .putHeader("content-type", "application/json")
                .setStatusCode(200)
                .end(Json.encodePrettily(article));
    }

}