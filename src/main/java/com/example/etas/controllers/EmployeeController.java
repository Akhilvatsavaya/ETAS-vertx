package com.example.etas.controllers;

import com.example.etas.MainVerticle;
import com.example.etas.models.Employee;
import com.example.etas.services.EmployeeService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.vertx.core.json.Json;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class EmployeeController {

  private final EmployeeService employeeService;
  private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

  @Inject
  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
    logger.debug("EmployeeController Created");
  }

  public void mount(Router router)
  {
    router.route().handler(BodyHandler.create());
    router.get("/employees").handler(this::getAllEmployees);
    router.get("/employee/:id").handler(this::getEmployeeById);
    router.post("/employee").handler(this::addEmployee);
    router.put("/employee").handler(this::updateEmployee);
    router.delete("/employee/:id").handler(this::deleteEmployee);
  }

  private void getAllEmployees(RoutingContext context) {
    employeeService.getAllEmployees().subscribe(result -> {
      context.response()
        .putHeader("content-type", "application/json")
        .end(Json.encodePrettily(result));
    }, throwable -> {
      context.response()
        .setStatusCode(500)
        .end(throwable.getMessage());
    });
  }

  private void getEmployeeById(RoutingContext context) {
    Long id = Long.parseLong(context.pathParam("id"));
    employeeService.getEmployeeById(id).subscribe(result -> {
      context.response()
        .putHeader("content-type", "application/json")
        .end(Json.encodePrettily(result));
    }, throwable -> {
      context.response()
        .setStatusCode(404)
        .end(throwable.getMessage());
    });
  }

  private void addEmployee(RoutingContext context) {
    Employee employee = Json.decodeValue(context.getBodyAsString(), Employee.class);
    employeeService.addEmployee(employee).subscribe(result -> {
      context.response()
        .putHeader("content-type", "application/json")
        .end(Json.encodePrettily(result));
    }, throwable -> {
      context.response()
        .setStatusCode(500)
        .end(throwable.getMessage());
    });
  }

  private void updateEmployee(RoutingContext context) {
    Employee employee = Json.decodeValue(context.getBodyAsString(), Employee.class);
    employeeService.updateEmployee(employee).subscribe(result -> {
      context.response()
        .putHeader("content-type", "application/json")
        .end(Json.encodePrettily(result));
    }, throwable -> {
      context.response()
        .setStatusCode(404)
        .end(throwable.getMessage());
    });
  }

  private void deleteEmployee(RoutingContext context) {
    Long id = Long.parseLong(context.pathParam("id"));
    employeeService.deleteEmployee(id).subscribe(result -> {
      context.response()
        .putHeader("content-type", "application/json")
        .end("{\"deleted\":" + result + "}");
    }, throwable -> {
      context.response()
        .setStatusCode(500)
        .end(throwable.getMessage());
    });
  }

}
