package com.employee.empmgr.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import com.employee.empmgr.repository.AttendentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.employee.empmgr.model.Attendent;
import com.employee.empmgr.model.Employee;
import com.employee.empmgr.model.Report;
import com.employee.empmgr.model.Role;
import com.employee.empmgr.repository.EmployeeRepository;
import com.employee.empmgr.repository.ReportRepository;
import com.employee.empmgr.repository.RoleRepository;

import com.employee.empmgr.services.AttendentService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.time.Duration;
import java.util.UUID;

import java.time.YearMonth;
@Controller
public class ProfileController {
    @Autowired
    private AttendentRepository attendentRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AttendentService attendentService;
    
    @GetMapping("/report_profile")
    public String getProfileReport(@RequestParam(name = "status", required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Employee employee = employeeRepository.findByName(authentication
                .getName())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found for : "));
        List<Report> reports = reportRepository.findByEmployeeIdAndStatus(employee.getId(), status);
        model.addAttribute("reports", reports);
        model.addAttribute("status", status);
        return "report";
    }

    @GetMapping("/attendent_profile")
    public String listProfileAttendents(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long employeeId,
            Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Employee employee = employeeRepository.findByName(authentication
                .getName())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found for : "));
        Page<Attendent> attendents = attendentRepository.findByEmployeeId(employee.getId(), PageRequest.of(page, size));
        model.addAttribute("attendents", attendents);
        return "attendent";
    }


    @GetMapping("/my_profile")
    public String getPayroll(@RequestParam(defaultValue = "") String selectMonth, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Employee employee = employeeRepository.findByName(authentication
                .getName())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found for : "));
        Long id  = employee.getId();
        Duration workingHours;
        int dayOfMonth;
        String queryMonth;
        if (selectMonth.isEmpty()) {

            workingHours = attendentService.calculateWorkingHoursForSelectedMonth(id);
            dayOfMonth = attendentService.countDaysPassedExcludingSundays();
            queryMonth = YearMonth.now().toString();
        } else {
            workingHours = attendentService.calculateWorkingHoursForSelectedMonth(id, selectMonth);
            dayOfMonth = attendentService.countDaysPassedExcludingSundays(selectMonth);
            queryMonth = selectMonth;
        }
        long hours = workingHours.toHours();

       // Employee employee = employeeRepository.getById(id);

        LocalDate today = LocalDate.now();
        // int dayOfMonth = today.getDayOfMonth();

        List<Attendent> attendents = attendentRepository.findByEmployeeIdAndCreateDate(id, today);

        double salary = attendentService.calculateSalary(hours, dayOfMonth * 8,
                Long.parseLong(employee.getSalary()));
        model.addAttribute("attendents", attendents);
        model.addAttribute("employee", employee);
        model.addAttribute("workhour", hours);
        model.addAttribute("defaulthour", dayOfMonth * 8);
        model.addAttribute("salary", salary);
        model.addAttribute("queryMonth", queryMonth);
        return "employee_detail"; // Return the name of your edit employee Thymeleaf template

    }

}
