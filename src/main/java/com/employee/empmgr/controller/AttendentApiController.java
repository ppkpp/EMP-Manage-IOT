package com.employee.empmgr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.employee.empmgr.model.Attendent;
import com.employee.empmgr.model.Employee;
import com.employee.empmgr.repository.AttendentRepository;
import com.employee.empmgr.repository.EmployeeRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class AttendentApiController {

    @Autowired
    private AttendentRepository attendentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/attendents")
    public String createAttendentByRFID(@RequestParam String rfid, @RequestParam String status) {
        // Find employee by RFID
        Employee employee = employeeRepository.findByRfid(rfid)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found for RFID: " + rfid));

        Long employeeId = employee.getId();
        LocalDate today = LocalDate.now();
        System.out.println(today);
        // Check if there is an existing Attendent record for today with status 'start'
        List<Attendent> startAttendent = attendentRepository.findByEmployeeIdAndCreateDateAndStatus(employeeId, today, "start");
        List<Attendent>  finishAttendents = attendentRepository.findByEmployeeIdAndCreateDateAndStatus(employeeId,
                today, "finish");
       
        if (startAttendent.isEmpty()) {
            // If found, update the status to 'finish'
            Attendent attendent = new Attendent();
            attendent.setEmployee(employee);
            attendent.setStatus("start");
            attendentRepository.save(attendent);
            return "status updated to finish";
        } else {
            if(finishAttendents.isEmpty())
            {// Otherwise, create a new Attendent instance
            Attendent attendent = new Attendent();
            attendent.setEmployee(employee);
            attendent.setStatus("finish"); // Assuming default status
            // Automatically set createDate and createTime using @PrePersist method in
            // Attendent class
            attendentRepository.save(attendent);
            return "Finish Attendent";}
            else{
                return "Error";
            }
        }
    }

}

    /*
     * 
     * curl -X POST \
     * http://localhost:8080/api/attendent/create \
     * -H 'Content-Type: application/x-www-form-urlencoded' \
     * -d 'rfid=1'
     * 
     */
//   @PostMapping("/attendents")
//     public String createAttendentByRFID(@RequestParam String rfid, @RequestParam String status) {
//         // Find employee by RFID
       
//         Employee employee = employeeRepository.findByRfid(rfid)
//                 .orElseThrow(() -> new EntityNotFoundException("Employee not found for RFID: " + rfid));
//         System.out.println(employee.getId());
//         // Create new Attendent instance
//         Attendent attendent = new Attendent();
//         attendent.setEmployee(employee);
//         attendent.setStatus(status); // Assuming default status

//         // Automatically set createDate and createTime using @PrePersist method in Attendent class

//         // Save Attendent
//          attendentRepository.save(attendent);
//          return "success";
//     }