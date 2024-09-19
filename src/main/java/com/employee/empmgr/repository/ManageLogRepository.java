package com.employee.empmgr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.employee.empmgr.model.ManageLog;
import com.employee.empmgr.model.Role;

public interface ManageLogRepository  extends JpaRepository<ManageLog, Long>  {
    
}
