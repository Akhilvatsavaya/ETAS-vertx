package com.example.etas;

import com.example.etas.controllers.BookingController;
import com.example.etas.controllers.CabController;
import com.example.etas.controllers.EmployeeController;
import com.example.etas.repositories.BookingRepository;
import com.example.etas.repositories.CabRepository;
import com.example.etas.services.BookingService;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import io.reactivex.Completable;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.reactivex.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class HttpServerVerticle extends AbstractVerticle {

  private final JDBCClient jdbcClient;
  private final Config config;

  private static final Logger logger = LoggerFactory.getLogger(HttpServerVerticle.class);

  @Inject
  public HttpServerVerticle(JDBCClient jdbcClient, Config config) {
    this.jdbcClient = jdbcClient;
    this.config = config;
    logger.debug("HttpServerVerticle Constructor Started");
//    System.out.println("HttpServerVerticle constructor started");
  }

  @Override
  public Completable rxStart() {
    logger.info("Starting HttpServerVerticle");
//    System.out.println("Starting HttpServerVerticle");
    Router router = Router.router(vertx);

    Injector inject = Guice.createInjector(new ServiceModule(jdbcClient, config));

    EmployeeController employeeController = inject.getInstance(EmployeeController.class);

//    Removed after using Google Guice
//    EmployeeRepository employeeRepository = new EmployeeRepository(jdbcClient);
//    EmployeeService employeeService = new EmployeeService(employeeRepository);
//    EmployeeController employeeController = new EmployeeController(employeeService);

    employeeController.mount(router);

    CabController cabController = inject.getInstance(CabController.class);

//    Removed after using Google Guice
//    CabRepository cabRepository = new CabRepository(jdbcClient);
//    CabService cabService = new CabService(cabRepository);
//    CabController cabController = new CabController(cabService);

    cabController.mount(router);

    BookingController bookingController = inject.getInstance(BookingController.class);

//    Removed after using Google Guice
//    BookingRepository bookingRepository = new BookingRepository(jdbcClient);
//    BookingService bookingService = new BookingService(bookingRepository, cabRepository);
//    BookingController bookingController = new BookingController(bookingService);

    bookingController.mount(router);

    logger.info("Routes Mounted");
//    System.out.println("Routes mounted");

    return vertx.createHttpServer()
      .requestHandler(router)
      .rxListen(config.getInt("server.port"))
      .doOnSubscribe(disposable -> logger.info("HTTP server is starting on port: {}", config.getInt("server.port"))) //System.out.println("HTTP server is starting on port " + config.getInt("server.port")))
      .doOnSuccess(server -> logger.info("HTTP server started on port: {}", config.getInt("server.port")))  //System.out.println("HTTP server started on port " + config.getInt("server.port")))
      .doOnError(error -> logger.error("Failed to start HTTP server: {}", error.getMessage()))  //System.err.println("Failed to start HTTP server: " + error.getMessage()))
      .ignoreElement();
  }

}
