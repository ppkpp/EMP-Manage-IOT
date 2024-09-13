package com.employee.empmgr.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import com.employee.empmgr.repository.EmployeeRepository;

import jakarta.persistence.EntityNotFoundException;
import com.employee.empmgr.model.Employee;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

import org.springframework.security.crypto.password.PasswordEncoder;
@Controller
public class HomeController {
    @Autowired
    private PasswordEncoder passwordEncoder;
     @Autowired
    private EmployeeRepository employeeRepository;
    @GetMapping("/home")
    public String home() {
        return "home"; // Ensure you have a home.html in your templates folder
    }

    @GetMapping("/")
    public String root() {
        return "home"; // Ensure you have a home.html in your templates folder
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/otp")
    public String otp() {
        return "otp"; // Ensure you have a home.html in your templates folder
    }

     @GetMapping("/resetpassword")
    public String resetpassword() {
        return "recovery"; // Ensure you have a home.html in your templates folder
    }

    @PostMapping("/otp")
    public ModelAndView handleResetPassword(@RequestParam("email") String recipient) {
        // Process the email (e.g., send a password reset link)
        // For now, we'll just print it to the console
          Random random = new Random();
        // Generate a random number between 100000 and 999999
        String sixDigitNumber =  String.valueOf(100000 + random.nextInt(900000));

         Employee employee = employeeRepository.findByEmail(recipient)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found for : "));
        employee.setCode(sixDigitNumber);
        employeeRepository.save(employee);
        System.out.println("Password reset request for email: " + recipient);


         String subject = "Password Reset for EMP";
        String body = "Your Reset Code : "+sixDigitNumber;
        String sender = "maungmaung2212000@gmail.com";
        String password = "ngdc juaq wovu acrq"; // Use your app password here
        
        // Set up properties for the SMTP server
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587"); // Use port 465 for SSL

        // Create a session with an Authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
        });

        try {
            // Create a default MimeMessage object
            MimeMessage message = new MimeMessage(session);

            // Set From: header field
            message.setFrom(new InternetAddress(sender));

            // Set To: header field
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            // Set Subject: header field
            message.setSubject(subject);

            // Set the actual message
            message.setText(body);

            // Send the message
            Transport.send(message);
            System.out.println("Message sent!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }



        // Return a view or redirect to a different page
        ModelAndView modelAndView = new ModelAndView("redirect:/resetpassword");
        modelAndView.addObject("message", "Password reset instructions have been sent to your email.");
         return modelAndView;
    }


    @PostMapping("/resetpassword")
    public ModelAndView setRestPassword(@RequestParam("email") String email,@RequestParam("password") String password,@RequestParam("code") String code) {
                 Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found for : "));
        if (!employee.getCode().equals(code)) {
            ModelAndView modelAndView = new ModelAndView("redirect:/resetpassword");
            modelAndView.addObject("message", "Password reset instructions have been sent to your email.");
             return modelAndView;
        }
        System.out.println("Email "+email);
        System.out.println("COde "+code);
        System.out.println("Password "+password);
        System.out.println("OldEnc Code "+employee.getPassword());
        String newPassword = passwordEncoder.encode(password);
        System.out.println("NewEnc Code "+newPassword);
         employee.setPassword(newPassword);
          employeeRepository.save(employee);

          ModelAndView modelAndView = new ModelAndView("redirect:/login");
      
         return modelAndView;
    }
}
