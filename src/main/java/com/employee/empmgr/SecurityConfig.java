package com.employee.empmgr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.employee.empmgr.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

   /* @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/", "/home").permitAll()
                .requestMatchers("/attendents").authenticated()
				.anyRequest().authenticated()
			)
			.formLogin((form) -> form
				.loginPage("/login")
				.permitAll()
			)
			.logout((logout) -> logout.permitAll());

		return http.build();
	}

   
     * @Bean
     * public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
     * Exception {
     * http
     * .authorizeHttpRequests((requests) -> requests
     * .requestMatchers("/", "/home").permitAll()
     * .anyRequest().authenticated())
     * .formLogin((form) -> form
     * .loginPage("/login")
     * .permitAll())
     * .logout((logout) -> logout.permitAll());
     * 
     * return http.build();
     * }
     * 
     * 
     * @Bean
     * public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder)
     * {
     * UserDetails user = User.builder()
     * .username("user")
     * .password(passwordEncoder.encode("password"))
     * .roles("USER")
     * .build();
     * return new InMemoryUserDetailsManager(user);
     * }
     */
      @Autowired
      UserDetailsServiceImpl userDetailsService;

      
      @Bean
      public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
      }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
            System.out.println(new BCryptPasswordEncoder().encode("admin"));
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        /*httpSecurity
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home").permitAll()
                        .requestMatchers("/attendents").authenticated()
                        .anyRequest().authenticated().and()
                        .authenticationManager(
                                authenticationManager))
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll())
                .logout((logout) -> logout.permitAll());

        return httpSecurity.build();*/

            httpSecurity
                            .csrf().disable() // Disable CSRF for APIs if not needed
                            .authorizeHttpRequests((requests) -> requests
                                            .requestMatchers("/login").permitAll()
                                            .requestMatchers("/resetpassword").permitAll()
                                            .requestMatchers("/otp").permitAll()
                                            .requestMatchers("/api/**").authenticated()
                                            .anyRequest().authenticated().and()
                        .authenticationManager(
                                authenticationManager))
                            .formLogin((form) -> form
                                            .loginPage("/login")
                                            .permitAll())
                            .httpBasic(); // Enable Basic Authentication

            return httpSecurity.build();
    
    }
}
