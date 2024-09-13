package com.employee.empmgr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import com.employee.empmgr.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    
    Optional<Employee> findByRfid(String rfid);
    
    //List<Employee> findByName(String name);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.roles WHERE e.name = :name")
    List<Employee> findByNameWithRoles(@Param("name") String name);

    Optional<Employee> findByName(String name);
    Optional<Employee> findByEmail(String email);
}
