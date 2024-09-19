package com.employee.empmgr.controller;


import com.employee.empmgr.model.Attendent;
import com.employee.empmgr.model.ManageLog;
import com.employee.empmgr.repository.ManageLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ManageLogController {

    @Autowired
    private ManageLogRepository manageLogRepository;

    @GetMapping("/managelogs")
    public String listManageLogs(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long employeeId,
            Model model) {
        Page<ManageLog> managelogs;
    
            managelogs = manageLogRepository.findAll(PageRequest.of(page, size));
        
        model.addAttribute("managelogs", managelogs);
        return "managelogs";
    }
}
