package com.example.etas.controllers;

import com.example.etas.models.Employee;
import com.example.etas.services.EmployeeService;
import io.vertx.core.json.Json;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;
import java.util.List;

public class EmployeeController {

  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
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

// JSON Format
//  private void getAllEmployees(RoutingContext context) {
//    employeeService.getAllEmployees().subscribe(result -> {
//      context.response()
//        .putHeader("content-type", "application/json")
//        .end(Json.encodePrettily(result));
//    }, throwable -> {
//      context.response()
//        .setStatusCode(500)
//        .end(throwable.getMessage());
//    });
//  }

  // XML Format
  private void getAllEmployees(RoutingContext context)
  {
    employeeService.getAllEmployees().subscribe(
            employees -> {
              try{
                String xmlResponse = generateXMLResponse(employees);
                context.response()
                        .putHeader("Content-Type", "application/xml")
                        .end(xmlResponse);
              }catch (XMLStreamException e){
                context.response()
                        .setStatusCode(500)
                        .end("Error genrating XML: " + e.getMessage());
              }
            }, throwable -> {
              context.response().setStatusCode(500).end(throwable.getMessage());
            }
    );
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

  private String generateXMLResponse(List<Employee> employees) throws XMLStreamException{
    StringWriter stringWriter = new StringWriter();
    XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
    XMLStreamWriter xmlWriter = xmlOutputFactory.createXMLStreamWriter(stringWriter);

    // Start XML document
    xmlWriter.writeStartDocument();
    xmlWriter.writeStartElement("employees");

    for (Employee employee : employees) {
      xmlWriter.writeStartElement("employee");

      xmlWriter.writeStartElement("id");
      xmlWriter.writeCharacters(employee.getId().toString());
      xmlWriter.writeEndElement();

      xmlWriter.writeStartElement("fullName");
      xmlWriter.writeCharacters(employee.getFullName());
      xmlWriter.writeEndElement();

      xmlWriter.writeStartElement("designation");
      xmlWriter.writeCharacters(employee.getDesignation());
      xmlWriter.writeEndElement();

      xmlWriter.writeStartElement("joiningDate");
      xmlWriter.writeCharacters(employee.getJoiningDate());  // Assuming joiningDate is now a String
      xmlWriter.writeEndElement();

      xmlWriter.writeStartElement("email");
      xmlWriter.writeCharacters(employee.getEmail());
      xmlWriter.writeEndElement();

      xmlWriter.writeStartElement("phone");
      xmlWriter.writeCharacters(employee.getPhone());
      xmlWriter.writeEndElement();

      xmlWriter.writeStartElement("address");
      xmlWriter.writeCharacters(employee.getAddress());
      xmlWriter.writeEndElement();

      xmlWriter.writeEndElement(); // end employee
    }

    xmlWriter.writeEndElement(); // end employees
    xmlWriter.writeEndDocument();

    xmlWriter.flush();
    xmlWriter.close();

    return stringWriter.toString();
  }

}
