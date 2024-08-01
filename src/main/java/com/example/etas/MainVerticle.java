package com.example.etas;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.typesafe.config.ConfigFactory;
import io.vertx.reactivex.core.AbstractVerticle;

import com.typesafe.config.Config;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainVerticle extends AbstractVerticle {

  private JDBCClient jdbcClient;

  private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Config config = ConfigFactory.load();

    JsonObject dbConfig = new JsonObject()
      .put("url", config.getString("database.url"))
      .put("driver_class", config.getString("database.driver"))
      .put("user", config.getString("database.user"))
      .put("password", config.getString("database.password"))
      .put("max_pool_size", config.getInt("database.maxPoolSize"));

    jdbcClient = JDBCClient.createShared(vertx, dbConfig);

    Injector inject = Guice.createInjector(new ServiceModule(jdbcClient, config));

    HttpServerVerticle serverVerticle = inject.getInstance(HttpServerVerticle.class);

    vertx.rxDeployVerticle(serverVerticle)
      .subscribe(
        id -> {
          logger.info("HttpServerVerticle deployed with id {}", id);
//          System.out.println("HttpServer Verticle deployed with id: " + id);
          startPromise.complete();
        }, startPromise::fail
      );

  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.rxDeployVerticle(new MainVerticle())
      .subscribe(id -> logger.info("Main Verticle deployed with id: {}", id), //System.out.println("Main Verticle deployed with id: " + id),
        Throwable::printStackTrace);
  }

}
