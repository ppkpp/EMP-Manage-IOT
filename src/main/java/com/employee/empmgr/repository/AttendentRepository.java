
package com.employee.empmgr.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import com.employee.empmgr.model.Attendent;
import com.employee.empmgr.model.Employee;
import com.employee.empmgr.model.Report;

public interface AttendentRepository extends JpaRepository<Attendent, Long> {
    Page<Attendent> findByEmployeeId(Long employeeId, Pageable pageable);

     List<Attendent> findByEmployeeIdAndCreateDate(Long employeeId, LocalDate createDate);

     List<Attendent> findByEmployeeIdAndCreateDateAndStatus(Long employeeId, LocalDate createDate,String status);
    
 

}
// ALTER TABLE attendent ALTER COLUMN createTime TYPE TIME(0);