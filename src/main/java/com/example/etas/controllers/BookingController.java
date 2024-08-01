package com.example.etas.controllers;

import com.example.etas.MainVerticle;
import com.example.etas.dtos.BookingRequest;
import com.example.etas.dtos.RequestError;
import com.example.etas.dtos.RequestSuccess;
import com.example.etas.services.BookingService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.vertx.core.json.Json;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class BookingController {

  private final BookingService bookingService;
  private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

  @Inject
  public BookingController(BookingService bookingService) {
    this.bookingService = bookingService;
    logger.debug("BookingController Created");
  }

  public void mount(Router router)
  {
    router.route().handler(BodyHandler.create());

    router.get("/bookings").handler(this::getAllBookings);
    router.get("/booking/:bookingId").handler(this::getBookingById);
    router.delete("/booking/:bookingId").handler(this::deleteBooking);

    router.post("/request").handler(this::requestCab);
    router.get("/request/:requestId").handler(this::getRequestStatus);
  }

  private void getAllBookings(RoutingContext context) {
    bookingService.getAllBookings().subscribe(result -> {
      context.response()
        .putHeader("content-type", "application/json")
        .end(Json.encodePrettily(result));
    }, throwable -> {
      context.response()
        .setStatusCode(500)
        .end(throwable.getMessage());
    });
  }

  private void getBookingById(RoutingContext context) {
    Long bookingId = Long.parseLong(context.pathParam("bookingId"));
    bookingService.getBookingById(bookingId).subscribe(result -> {
      context.response()
        .putHeader("content-type", "application/json")
        .end(Json.encodePrettily(result));
    }, throwable -> {
      context.response()
        .setStatusCode(404)
        .end(throwable.getMessage());
    });
  }

  private void deleteBooking(RoutingContext context) {
    Long bookingId = Long.parseLong(context.pathParam("bookingId"));
    bookingService.deleteBooking(bookingId).subscribe(result -> {
      context.response()
        .putHeader("content-type", "application/json")
        .end("{\"deleted\":" + result + "}");
    }, throwable -> {
      context.response()
        .setStatusCode(500)
        .end(throwable.getMessage());
    });
  }

  private void requestCab(RoutingContext context) {
    BookingRequest bookingRequest = Json.decodeValue(context.getBodyAsString(), BookingRequest.class);
    bookingService.requestCab(bookingRequest.getSourceLocation(), bookingRequest.getDateTimeOfJourney(), bookingRequest.getEmployeeId())
      .subscribe(requestId -> {
        context.response()
          .putHeader("content-type", "application/json")
          .end(Json.encodePrettily(new RequestSuccess(requestId)));
      }, throwable -> {
        context.response()
          .setStatusCode(400)
          .end(Json.encodePrettily(new RequestError(throwable.getMessage())));
      });
  }

  private void getRequestStatus(RoutingContext context) {
    Long requestId = Long.parseLong(context.pathParam("requestId"));
    bookingService.getRequestStatus(requestId).subscribe(result -> {
      context.response()
        .putHeader("content-type", "application/json")
        .end(Json.encodePrettily(result));
    }, throwable -> {
      context.response()
        .setStatusCode(404)
        .end(Json.encodePrettily(new RequestError(throwable.getMessage())));
    });
  }

}
