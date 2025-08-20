package com.example.evote.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.evote.Security.JwtAuthenticationFilter;
import com.example.evote.Security.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;  // For mobile app
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;  // For admin panel

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers(
                    "/api/voting/validate-nic",
                    "/api/voting/set-password", 
                    "/api/voting/login",
                    "/api/admin/auth/login"
                ).permitAll()
                
                // Mobile app endpoints - keep as is
                .requestMatchers("/api/voting/**").authenticated()
                
                // Admin endpoints 
                .requestMatchers("/api/admin/super/admins").hasRole("SUPER_ADMIN")
                .requestMatchers("/api/admin/candidates").hasRole("SUPER_ADMIN")
                .requestMatchers("/api/admin/divisions").hasRole("SUPER_ADMIN")
                .requestMatchers("/api/admin/voters").hasRole("SUPER_ADMIN")
                .requestMatchers("/api/admin/results/election").hasRole("SUPER_ADMIN")
                
                .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
            )
            // Add filters in correct order
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtFilter,  UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}