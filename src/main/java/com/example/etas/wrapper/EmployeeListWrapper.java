package com.example.etas.wrapper;

import com.example.etas.models.Employee;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "employees")
public class EmployeeListWrapper {

    private List<Employee> employees;

    @XmlElement(name = "employee")
    public List<Employee> getEmployees()
    {
        return employees;
    }

    public void setEmployees(List<Employee> employees){
        this.employees = employees;
    }

}
