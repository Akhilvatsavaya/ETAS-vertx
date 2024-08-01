package com.example.etas.repositories;

import com.example.etas.models.Employee;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.reactivex.Single;
import io.vertx.core.json.JsonArray;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Singleton
public class EmployeeRepository {

  private final JDBCClient jdbcClient;
  Random random = new Random();

  private final Logger logger = LoggerFactory.getLogger(BookingRepository.class);

  @Inject
  public EmployeeRepository(JDBCClient jdbcClient) {
    this.jdbcClient = jdbcClient;
    logger.debug("EmployeeRepository created");
  }

  public Single<List<Employee>> findAll() {
    return jdbcClient.rxGetConnection().flatMap(conn ->
      conn.rxQuery("SELECT * FROM employees").map(rs -> {
        List<Employee> employees = new ArrayList<>();
        rs.getRows().forEach(json -> {
          Employee employee = json.mapTo(Employee.class);
          employees.add(employee);
        });
        return employees;
      }).doFinally(conn::close)
    );
  }

  public Single<Employee> findById(Long id) {
    return jdbcClient.rxGetConnection().flatMap(conn ->
      conn.rxQueryWithParams("SELECT * FROM employees WHERE id = ?", new JsonArray().add(id)).map(rs -> {
        if (rs.getNumRows() == 0) {
          return null;
        } else {
          return rs.getRows().get(0).mapTo(Employee.class);
        }
      }).doFinally(conn::close)
    );
  }

  public Single<Employee> save(Employee employee) {
    employee.setId(random.nextLong());

    return jdbcClient.rxGetConnection().flatMap(conn ->
      conn.rxUpdateWithParams("INSERT INTO employees (id, full_name, designation, joining_date, email, phone, address) VALUES (?, ?, ?, ?, ?, ?, ?)",
        new JsonArray().add(employee.getId()).add(employee.getFullName()).add(employee.getDesignation()).add(employee.getJoiningDate())
          .add(employee.getEmail()).add(employee.getPhone()).add(employee.getAddress())).map(updateResult -> employee
      ).doFinally(conn::close)
    );
  }

  public Single<Employee> update(Employee employee) {
    return jdbcClient.rxGetConnection().flatMap(conn ->
      conn.rxUpdateWithParams("UPDATE employees SET full_name = ?, designation = ?, joining_date = ?, email = ?, phone = ?, address = ? WHERE id = ?",
        new JsonArray().add(employee.getFullName()).add(employee.getDesignation()).add(employee.getJoiningDate())
          .add(employee.getEmail()).add(employee.getPhone()).add(employee.getAddress()).add(employee.getId())).flatMap(updateResult ->
        findById(employee.getId())
      ).doFinally(conn::close)
    );
  }

  public Single<Boolean> delete(Long id) {
    return jdbcClient.rxGetConnection().flatMap(conn ->
      conn.rxUpdateWithParams("DELETE FROM employees WHERE id = ?", new JsonArray().add(id)).map(updateResult ->
        updateResult.getUpdated() > 0
      ).doFinally(conn::close)
    );
  }

}
