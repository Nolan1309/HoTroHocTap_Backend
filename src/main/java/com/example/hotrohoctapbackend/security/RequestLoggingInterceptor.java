package com.example.hotrohoctapbackend.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);
    
    @Autowired
    private SecurityLogger securityLogger;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String remoteAddr = request.getRemoteAddr();
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anonymous";
        
        logger.info("Request started: {} {} from {}, User: {}", method, uri, remoteAddr, username);
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        int status = response.getStatus();
        
        if (status >= 200 && status < 300) {
            securityLogger.logAccessGranted(uri, method);
            logger.info("Request successful: {} {} - Status: {}", method, uri, status);
        } else if (status == 403) {
            securityLogger.logAccessDenied(uri, method);
            logger.warn("Access denied: {} {} - Status: {}", method, uri, status);
        } else if (status == 401) {
            logger.warn("Unauthorized: {} {} - Status: {}", method, uri, status);
        } else {
            logger.info("Request completed: {} {} - Status: {}", method, uri, status);
        }
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            logger.error("Exception during request processing: {} {}", request.getMethod(), request.getRequestURI(), ex);
        }
    }
} 