package com.restapi.assignment;

import com.restapi.assignment.repository.EmployeeDao;
import com.restapi.assignment.entity.Employee;
import com.restapi.assignment.services.EmployeeService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EmployeeServiceImplTest {
    @Autowired
    EmployeeService theEmployeeService;

    @MockBean
    EmployeeDao theEmployeeDao;
    public List<Employee> employeeList= new ArrayList<>();
    Employee testEmployee;

    @Before
    public void setup() {
      testEmployee= new Employee("test","test","test@gmail.com");

    }

    @Test
    public void findAllTest(){
        when(theEmployeeDao.findAll()).thenReturn( Stream.of(new Employee("saurav","deore","saurav@gmail.com"),new Employee("test","test","test@gmail.com")).collect(Collectors.toList()));
//        when(theEmployeeDao.findAll()).thenReturn(employeeList);
        System.out.println(employeeList);
        assertEquals(2,(theEmployeeService.findALl()).size());

    }

    @Test
    public void findEmployeeByIdTest(){
        int theId=1;
        when(theEmployeeDao.findById(theId)).thenReturn(Optional.ofNullable(testEmployee));
        assertEquals(testEmployee,theEmployeeService.findById(theId));
    }


    @Test
    public void saveTest(){
        when(theEmployeeDao.save(testEmployee)).thenReturn(testEmployee);
        assertEquals(testEmployee,theEmployeeService.saveEmployee(testEmployee));
    }

    @Test
    public void deleteByIdTest(){
        theEmployeeService.deleteEmployeeById(1);
        verify(theEmployeeDao,times(1)).delete(testEmployee);
    }



}
