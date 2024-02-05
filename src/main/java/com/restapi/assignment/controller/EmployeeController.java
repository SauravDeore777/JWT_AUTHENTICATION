package com.restapi.assignment.controller;

import com.restapi.assignment.entity.Employee;
import com.restapi.assignment.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/crud")
public class EmployeeController {
    @Autowired
    EmployeeService theEmployeeService;

    @GetMapping("/employees")
    public List<Employee> findAll(){
        return this.theEmployeeService.findALl();
    }

    @GetMapping("/getemployee/{theId}")
    public Employee findBYId(@PathVariable("theId")int theId){
        return this.theEmployeeService.findById(theId);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/employee")
    public Employee insertEmployee(@RequestBody Employee theEmployee){
        theEmployee.setId(0);
        return this.theEmployeeService.saveEmployee(theEmployee);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/employee")
    public Employee updateEmployee( @RequestBody Employee theEmployee){

        return this.theEmployeeService.saveEmployee(theEmployee);

    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/employee/{theId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("theId") int theId){
        theEmployeeService.deleteEmployeeById(theId);
        return new ResponseEntity<>("this user has been successfully deleted",HttpStatus.OK);
    }

    @GetMapping("/currentUser")
    public String getLoggedInUser(Principal principal){
        return principal.getName();
    }

    @GetMapping("/evict")
    public ResponseEntity<String> evictCache(){
        theEmployeeService.clearCache();
        return ResponseEntity.ok("cache Cleared Successfully");
    }


}
