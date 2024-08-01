package com.example.etas.services;

import com.example.etas.HttpServerVerticle;
import com.example.etas.models.Employee;
import com.example.etas.repositories.EmployeeRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  private static final Logger logger = LoggerFactory.getLogger(HttpServerVerticle.class);

  @Inject
  public EmployeeService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
    logger.debug("EmployeeService Created");
  }

  public Single<List<Employee>> getAllEmployees() {
    return employeeRepository.findAll();
  }

  public Single<Employee> getEmployeeById(Long id) {
    return employeeRepository.findById(id);
  }

  public Single<Employee> addEmployee(Employee employee) {
    return employeeRepository.save(employee);
  }

  public Single<Employee> updateEmployee(Employee employee) {
    return employeeRepository.update(employee);
  }

  public Single<Boolean> deleteEmployee(Long id) {
    return employeeRepository.delete(id);
  }

}
