package com.employee.empmgr.services;

import com.employee.empmgr.model.Employee;
import com.employee.empmgr.model.Role;
import com.employee.empmgr.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

     
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Employee> employees = employeeRepository.findByNameWithRoles(username);
        
        if (employees.isEmpty()) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }

        if (employees.size() > 1) {
            logger.warn("Multiple users found with username: " + username);
            // Optionally handle this case, such as logging or throwing an exception
        }
       
        Employee employee = employees.get(0); // Assuming the first user found
           Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : employee.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }
      
                
        return User.builder()
                .username(employee.getName())
                .password(employee.getPassword())
                .authorities(authorities)  
                .build();
        
    }
}
