package com.employee.empmgr.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import com.employee.empmgr.model.Attendent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.employee.empmgr.model.Employee;
import com.employee.empmgr.model.Role;
import com.employee.empmgr.repository.EmployeeRepository;
import com.employee.empmgr.repository.RoleRepository;
import com.employee.empmgr.services.AttendentService;
import com.employee.empmgr.repository.AttendentRepository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;

     @Autowired
    private PasswordEncoder passwordEncoder;

   

    @Autowired
    private AttendentRepository attendentRepository;

    @Autowired
    private AttendentService attendentService;
    private static String UPLOAD_DIR = "uploads/";
   /*
    * @GetMapping("/employee")
    * public String listEmployees(Model model) {
    * model.addAttribute("employees", employeeRepository.findAll());
    * return "employees";
    * }
    */

    @GetMapping("/employee")
    //@Secured("ROLE_ROLE_ADMIN")
   @PreAuthorize("hasRole('ROLE_ROLE_ADMIN') or hasRole('ROLE_ROLE_MANAGER')")
    public String listEmployees(@RequestParam(defaultValue = "0") int page, 
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            String role = "";
            for (GrantedAuthority authority : authentication.getAuthorities()) {
            role = authority.getAuthority();
            }
            System.out.println("******************");
            System.out.println(role);

        Page<Employee> employees = employeeRepository.findAll(PageRequest.of(page, size));
        
        model.addAttribute("employees", employees);
        return "employees";
    }

    @GetMapping("/create_emp")
  //  @Secured("ROLE_ROLE_MANAGER")
    public String showCreateEmployeeForm(Model model) {
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("employee", new Employee());
        model.addAttribute("roles", roles);
        return "create_emp";
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }

    
    @PostMapping("/save_emp")
   // @Secured("ROLE_ROLE_MANAGER")
    public String saveEmployee(@ModelAttribute Employee employee, @RequestParam("image") MultipartFile file,
            @RequestParam("role") Long roleId) {
        try {
             Set<Role> roles = new HashSet<>();
            Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(role);
            if (file != null && !file.isEmpty()) {
               
               // String fileName = file.getOriginalFilename();

                String fileExtension = getFileExtension(file.getOriginalFilename());
                String fileName = UUID.randomUUID().toString() + fileExtension;


                Path filePath = Paths.get(UPLOAD_DIR, fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());
                
                employee.setImageUrl(filePath.toString());
                employee.setRoles(roles);
            }
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        employeeRepository.save(employee);
        return "redirect:/employee";
    }

    @GetMapping("/edit_emp/{id}")
    @PreAuthorize("hasRole('ROLE_ROLE_ADMIN') or hasRole('ROLE_ROLE_MANAGER')")
    public String editEmployee(@PathVariable Long id, Model model) {
        Employee employee = employeeRepository.getById(id);
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("employee", employee);
        model.addAttribute("roles", roles);
        return "edit_emp"; // Return the name of your edit employee Thymeleaf template
    }

    @PostMapping("/update_emp/{id}")
    @PreAuthorize("hasRole('ROLE_ROLE_ADMIN') or hasRole('ROLE_ROLE_MANAGER')")
    public String updateEmployee(@PathVariable Long id, @ModelAttribute("employee") Employee updatedEmployee,
            @RequestParam("file") MultipartFile file, @RequestParam("role") Long roleId) throws IOException {
        Employee existingEmployee = employeeRepository.getById(id);
        existingEmployee.setName(updatedEmployee.getName());
        existingEmployee.setFirstname(updatedEmployee.getFirstname());
        existingEmployee.setLastname(updatedEmployee.getLastname());
        existingEmployee.setDob(updatedEmployee.getDob());
        existingEmployee.setGender(updatedEmployee.getGender());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setAddress(updatedEmployee.getAddress());
        existingEmployee.setPosition(updatedEmployee.getPosition());
        existingEmployee.setHiredate(updatedEmployee.getHiredate());
        existingEmployee.setDepartment(updatedEmployee.getDepartment());
        existingEmployee.setSalary(updatedEmployee.getSalary());
        existingEmployee.setStatus(updatedEmployee.getStatus());
        existingEmployee.setRfid(updatedEmployee.getRfid());
        existingEmployee.setPhone(updatedEmployee.getPhone());

        // Update employee roles
        Set<Role> roles = new HashSet<>();
        Role selectedRole = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        roles.add(selectedRole); // Add the selected role to the employee
        existingEmployee.setRoles(roles);

        // Update other fields as needed

        if (!file.isEmpty()) {
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String uuidFileName = UUID.randomUUID().toString() + fileExtension;
            Path filePath = Paths.get(UPLOAD_DIR + uuidFileName);
            Files.write(filePath, file.getBytes());
            existingEmployee.setImageUrl("/uploads/" + uuidFileName);
        }

        employeeRepository.save(existingEmployee); // Save or update employee
        return "redirect:/employee"; // Redirect to the employee list page after update
    }
    
    @GetMapping("/employee_detail/{id}")
    @PreAuthorize("hasRole('ROLE_ROLE_ADMIN') or hasRole('ROLE_ROLE_MANAGER')")
    public String getPayroll(@PathVariable Long id, @RequestParam(defaultValue = "") String selectMonth, Model model) {
        Duration workingHours;
        int dayOfMonth;
        String queryMonth;
        if(selectMonth.isEmpty())
        {
           
            workingHours = attendentService.calculateWorkingHoursForSelectedMonth(id);
            dayOfMonth = attendentService.countDaysPassedExcludingSundays();
            queryMonth = YearMonth.now().toString();
        }else{
             workingHours = attendentService.calculateWorkingHoursForSelectedMonth(id, selectMonth);
             dayOfMonth = attendentService.countDaysPassedExcludingSundays(selectMonth);
             queryMonth = selectMonth;
        }
            long hours = workingHours.toHours();

            Employee employee = employeeRepository.getById(id);

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

   
   /*
    * @PostMapping("/save_emp")
    * public String saveEmployee(@ModelAttribute("employee") Employee employee) {
    * 
    * employeeRepository.save(employee);
    * return "redirect:/employee";
    * }
    */
}
