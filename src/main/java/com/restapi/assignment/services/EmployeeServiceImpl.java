package com.restapi.assignment.services;

import com.restapi.assignment.repository.EmployeeDao;
import com.restapi.assignment.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    EmployeeDao theEmployeeDao;
    @Override
    public List<Employee> findALl() {
        List<Employee> employees= theEmployeeDao.findAll();
        return employees;
    }

    @Override
    @Cacheable(value = "findById")
    public Employee findById(int theId) {
        log.info("findById method is callded");
        Optional<Employee> result=this.theEmployeeDao.findById(theId);
        if(result.isPresent()){
            return result.get();
        }
        return null;
    }

    @Override
    public Employee saveEmployee(Employee theEmployee) {
        Employee tempEmployee=this.theEmployeeDao.save(theEmployee);
        return tempEmployee;

    }

    @Override
    public void deleteEmployeeById(int theId) {
        Employee EmployeeToBeDeleted =findById(theId);
        this.theEmployeeDao.delete(EmployeeToBeDeleted);

    }

    @Override
    @CacheEvict(cacheNames = {"findById"},allEntries = true)
    public void clearCache() {
        log.info("Clearing Cache.......");

    }

//    @Scheduled(fixedDelay = 5) // Scheduled refresh every 10 minutes
//    public void scheduledCacheRefresh() {
//        // Cache refresh logic
//        System.out.println("Cache refreshed");
//    }
}
