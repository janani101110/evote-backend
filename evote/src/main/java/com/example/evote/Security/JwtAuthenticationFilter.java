package com.example.evote.Security;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.evote.Entity.Admin;
import com.example.evote.Repository.AdminRepository;
import com.example.evote.Util.JwtAdminUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends GenericFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    private final JwtAdminUtil jwtUtil;
    private final AdminRepository adminRepository;

    public JwtAuthenticationFilter(JwtAdminUtil jwtUtil, AdminRepository adminRepository) {
        this.jwtUtil = jwtUtil;
        this.adminRepository = adminRepository;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        try {
            String jwt = getJwtFromRequest(request);
            
            if (StringUtils.hasText(jwt)) {
                if (jwtUtil.validateToken(jwt)) {
                    Claims claims = jwtUtil.extractAllClaims(jwt);
        String email = claims.getSubject();
        String role = claims.get("role", String.class);
        
                    if (StringUtils.hasText(email)) {
                        authenticateAdmin(email,role);
                    }
                } else {
                    throw new JwtException("Invalid token");
                }
            }
            
            chain.doFilter(request, response);
            
        } catch (ExpiredJwtException ex) {
            log.warn("Expired JWT token: {}", ex.getMessage());
            sendError(response, "Token expired", HttpStatus.UNAUTHORIZED);
            
        } catch (UnsupportedJwtException | MalformedJwtException ex) {
            log.warn("Invalid JWT token: {}", ex.getMessage());
            sendError(response, "Invalid token", HttpStatus.BAD_REQUEST);
            
        } catch (JwtException ex) {
            log.warn("JWT validation failed: {}", ex.getMessage());
            sendError(response, "Invalid token", HttpStatus.UNAUTHORIZED);
            
        } catch (ServletException | IOException ex) {
            log.error("JWT processing failed", ex);
            sendError(response, "Authentication failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.debug("Received Authorization header: {}", bearerToken); 
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void authenticateAdmin(String email,String role) {
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        
        if (adminOpt.isEmpty()) {
            log.warn("Admin not found for email: {}", email);
            return;
        }
        
        Admin admin = adminOpt.get();
        
        if (!admin.isActive()) {
            log.warn("Admin account inactive: {}", email);
            return;
        }
        
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + admin.getRole().name());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            email, 
            role, 
            Collections.singletonList(authority)
        );
        
        authentication.setDetails(admin);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        log.debug("Authenticated admin: {}", email);
    }

    private void sendError(HttpServletResponse response, String message, HttpStatus status) 
            throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(String.format(
            "{\"error\": \"%s\", \"message\": \"%s\"}", 
            status.getReasonPhrase(), 
            message
        ));
    }
}