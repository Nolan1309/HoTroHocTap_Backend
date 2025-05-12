package com.example.hotrohoctapbackend.security;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class SecurityLogger {
    private static final Logger logger = LoggerFactory.getLogger(SecurityLogger.class);

    /**
     * Log thông tin về yêu cầu hiện tại và trạng thái xác thực
     */
    public void logSecurityInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String remoteAddr = request.getRemoteAddr();
        
        String username = authentication != null ? authentication.getName() : "anonymous";
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
        
        Collection<? extends GrantedAuthority> authorities = 
            authentication != null ? authentication.getAuthorities() : null;
        String roles = authorities != null ? 
            authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", ")) : "none";
        
        logger.info("Security Info - Request: {} {} from {}, User: {}, Authenticated: {}, Roles: {}", 
                method, uri, remoteAddr, username, isAuthenticated, roles);
    }
    
    /**
     * Log thông tin khi quyền truy cập bị từ chối
     */
    public void logAccessDenied(String endpoint, String method) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "anonymous";
        String authorities = authentication != null ? 
            authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", ")) : "none";
        
        logger.warn("Access Denied - User: {}, Authorities: {}, tried to access: {} {}", 
                username, authorities, method, endpoint);
    }
    
    /**
     * Log thông tin khi quyền truy cập được cấp
     */
    public void logAccessGranted(String endpoint, String method) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "anonymous";
        
        logger.info("Access Granted - User: {} accessed: {} {}", username, method, endpoint);
    }
} 