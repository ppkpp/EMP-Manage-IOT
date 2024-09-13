
package com.employee.empmgr.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Month;
import com.employee.empmgr.model.Report;
import com.employee.empmgr.model.Employee;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByStatus(String status);

    @Query("SELECT r FROM Report r WHERE r.employee.id = :employeeId AND r.status = :status")
    List<Report> findByEmployeeIdAndStatus(@Param("employeeId") Long employeeId, @Param("status") String status);
    
}
