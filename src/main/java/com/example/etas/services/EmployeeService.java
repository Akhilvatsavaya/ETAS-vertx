package com.example.etas.services;

import com.example.etas.models.Employee;
import com.example.etas.repositories.EmployeeRepository;
import io.reactivex.Single;

import java.util.List;

public class EmployeeService {

  private final EmployeeRepository employeeRepository;

  public EmployeeService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
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
