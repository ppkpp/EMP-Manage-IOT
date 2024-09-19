package com.employee.empmgr.services;



import com.employee.empmgr.model.ManageLog;
import com.employee.empmgr.repository.ManageLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import org.springframework.security.core.Authentication;
@Service
public class ManageLogService {

    private final ManageLogRepository manageLogRepository;

    @Autowired
    public ManageLogService(ManageLogRepository manageLogRepository) {
        this.manageLogRepository = manageLogRepository;
    }

    public ManageLog logManagerAction(Authentication authentication,String action) {
       
        String username = authentication.getName();
            String position = "";
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                position = authority.getAuthority();
            }
        if ("ROLE_ROLE_ADMIN".equals(position)) {
            // Skip saving if the position is not "ROLE_MANAGER"
            return null;
        }
        ManageLog manageLog = new ManageLog(action, username, position);
        return manageLogRepository.save(manageLog);
    }
}