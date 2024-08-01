package com.example.etas;

import com.example.etas.controllers.BookingController;
import com.example.etas.controllers.CabController;
import com.example.etas.controllers.EmployeeController;
import com.example.etas.models.Booking;
import com.google.inject.AbstractModule;
import com.example.etas.repositories.BookingRepository;
import com.example.etas.repositories.CabRepository;
import com.example.etas.repositories.EmployeeRepository;
import com.example.etas.services.BookingService;
import com.example.etas.services.CabService;
import com.example.etas.services.EmployeeService;
import com.typesafe.config.Config;
import io.vertx.reactivex.ext.jdbc.JDBCClient;

public class ServiceModule extends AbstractModule {
  private final JDBCClient jdbcClient;
  private final Config config;

  public ServiceModule(JDBCClient jdbcClient, Config config) {
    this.jdbcClient = jdbcClient;
    this.config = config;
  }

  @Override
  protected void configure() {
    bind(JDBCClient.class).toInstance(jdbcClient);

    bind(HttpServerVerticle.class).toInstance(new HttpServerVerticle(jdbcClient, config));


    bind(EmployeeRepository.class).toInstance(new EmployeeRepository(jdbcClient));
    bind(CabRepository.class).toInstance(new CabRepository(jdbcClient));
    bind(BookingRepository.class).toInstance(new BookingRepository(jdbcClient));


    bind(EmployeeService.class).toInstance(new EmployeeService(new EmployeeRepository(jdbcClient)));
    bind(CabService.class).toInstance(new CabService(new CabRepository(jdbcClient)));
    bind(BookingService.class).toInstance(new BookingService(new BookingRepository(jdbcClient), new CabRepository(jdbcClient)));

    bind(EmployeeController.class).toInstance(new EmployeeController(new EmployeeService(new EmployeeRepository(jdbcClient))));
    bind(CabController.class).toInstance(new CabController(new CabService(new CabRepository(jdbcClient))));
    bind(BookingController.class).toInstance(new BookingController(new BookingService(new BookingRepository(jdbcClient), new CabRepository(jdbcClient))));
  }
}
