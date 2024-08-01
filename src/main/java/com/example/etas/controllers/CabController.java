package com.example.etas.controllers;

import com.example.etas.MainVerticle;
import com.example.etas.models.Cab;
import com.example.etas.services.CabService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.vertx.core.json.Json;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class CabController {

  private final CabService cabService;
  private static final Logger logger = LoggerFactory.getLogger(CabController.class);

  @Inject
  public CabController(CabService cabService) {
    this.cabService = cabService;
    logger.debug("CabController Created");
  }

  public void mount(Router router){
    router.route().handler(BodyHandler.create());

    router.get("/cabs").handler(this::getAllCabs);
    router.get("/cabs/:cabId").handler(this::getCabById);
    router.post("/cabs").handler(this::addCab);
    router.put("/cabs").handler(this::updateCab);
    router.delete("/cabs/:cabId").handler(this::deleteCab);
    router.put("/cabs/:cabId/available").handler(this::setCabAvailable);
    router.put("/cabs/:cabId/unavailable").handler(this::setCabUnavailable);
  }

  private void getAllCabs(RoutingContext context) {
    cabService.getAllCabs().subscribe(result -> {
      context.response()
        .putHeader("content-type", "application/json")
        .end(Json.encodePrettily(result));
    }, throwable -> {
      context.response()
        .setStatusCode(500)
        .end(throwable.getMessage());
    });
  }

  private void getCabById(RoutingContext context) {
    Long cabId = Long.parseLong(context.pathParam("cabId"));
    cabService.getCabById(cabId).subscribe(result -> {
      context.response()
        .putHeader("content-type", "application/json")
        .end(Json.encodePrettily(result));
    }, throwable -> {
      context.response()
        .setStatusCode(404)
        .end(throwable.getMessage());
    });
  }

  private void addCab(RoutingContext context) {
    Cab cab = Json.decodeValue(context.getBodyAsString(), Cab.class);
    cabService.addCab(cab).subscribe(result -> {
      context.response()
        .putHeader("content-type", "application/json")
        .end(Json.encodePrettily(result));
    }, throwable -> {
      context.response()
        .setStatusCode(500)
        .end(throwable.getMessage());
    });
  }

  private void updateCab(RoutingContext context) {
    Cab cab = Json.decodeValue(context.getBodyAsString(), Cab.class);
    cabService.updateCab(cab).subscribe(result -> {
      context.response()
        .putHeader("content-type", "application/json")
        .end(Json.encodePrettily(result));
    }, throwable -> {
      context.response()
        .setStatusCode(404)
        .end(throwable.getMessage());
    });
  }

  private void deleteCab(RoutingContext context) {
    Long cabId = Long.parseLong(context.pathParam("cabId"));
    cabService.deleteCab(cabId).subscribe(result -> {
      context.response()
        .putHeader("content-type", "application/json")
        .end("{\"deleted\":" + result + "}");
    }, throwable -> {
      context.response()
        .setStatusCode(500)
        .end(throwable.getMessage());
    });
  }

  private void setCabAvailable(RoutingContext context) {
    Long cabId = Long.parseLong(context.pathParam("cabId"));
    cabService.setCabStatus(cabId, "AVAILABLE").subscribe(result -> {
      context.response()
        .putHeader("content-type", "application/json")
        .end("{\"status_updated\":" + result + "}");
    }, throwable -> {
      context.response()
        .setStatusCode(500)
        .end(throwable.getMessage());
    });
  }

  private void setCabUnavailable(RoutingContext context) {
    Long cabId = Long.parseLong(context.pathParam("cabId"));
    cabService.setCabStatus(cabId, "UNAVAILABLE").subscribe(result -> {
      context.response()
        .putHeader("content-type", "application/json")
        .end("{\"status_updated\":" + result + "}");
    }, throwable -> {
      context.response()
        .setStatusCode(500)
        .end(throwable.getMessage());
    });
  }

}
