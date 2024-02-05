package com.restapi.assignment.services;

import com.restapi.assignment.entity.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> findALl();
    Employee findById(int theId);

    Employee saveEmployee(Employee theEmployee);

    void deleteEmployeeById(int theId);

    void clearCache();


}
