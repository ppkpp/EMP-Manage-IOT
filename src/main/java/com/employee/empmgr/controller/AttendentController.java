package com.employee.empmgr.controller;

import com.employee.empmgr.model.Attendent;
import com.employee.empmgr.repository.AttendentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AttendentController {

    @Autowired
    private AttendentRepository attendentRepository;

    @GetMapping("/attendent")
    public String listAttendents(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long employeeId,
            Model model) {
        Page<Attendent> attendents;
        if (employeeId != null) {
            attendents = attendentRepository.findByEmployeeId(employeeId, PageRequest.of(page, size));
        } else {

            attendents = attendentRepository.findAll(PageRequest.of(page, size));
        }
        model.addAttribute("attendents", attendents);
        return "attendent";
    }
}
