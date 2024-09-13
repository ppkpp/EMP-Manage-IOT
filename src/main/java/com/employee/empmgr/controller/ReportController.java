package com.employee.empmgr.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

@Controller
public class ReportController {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // Endpoint to handle confirming or rejecting a report
    @PostMapping("/report/{status}/{id}")
    public String updateReportStatus(@PathVariable String status, @PathVariable Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Report not found with id: " + id));

        if ("confirm".equalsIgnoreCase(status)) {
            report.setStatus("CONFIRM");
        } else if ("reject".equalsIgnoreCase(status)) {
            report.setStatus("REJECT");
        } else if ("pending".equalsIgnoreCase(status)) {
            report.setStatus("PENDING");
        } else {
            throw new IllegalArgumentException("Invalid status provided: " + status);
        }

        reportRepository.save(report);
        return "redirect:/report"; // Redirect back to report list
    }

    
    @GetMapping("/report")
    public String getReportsByStatus(@RequestParam(name = "status", required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
      List<Report> reports =   reportRepository.findByStatus(status);
            model.addAttribute("reports", reports);
            model.addAttribute("status", status);
        return "report"; 
    }

    
    @GetMapping("/create_report")
    public String showCreateEmployeeForm(Model model) {
        model.addAttribute("report", new Report());
        return "create_report";
    }

   
    @PostMapping("/save_report")
    public String saveReport(@ModelAttribute Report report) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
   

         Employee employee = employeeRepository.findByName(authentication
                 .getName())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found for : "));

        report.setEmployee(employee);
        reportRepository.save(report);
        return "redirect:/create_report";
    }

}
