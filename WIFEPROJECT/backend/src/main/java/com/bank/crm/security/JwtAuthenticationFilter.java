package com.bank.crm.security;

import com.bank.crm.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        final String requestTokenHeader = request.getHeader("Authorization");
        logger.debug("Processing request for " + request.getRequestURI() + " with Authorization header: " + requestTokenHeader);

        String username = null;
        String jwtToken = null;

        // JWT Token格式为 "Bearer token"
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(jwtToken);
                logger.debug("Username extracted from token: " + username);
            } catch (Exception e) {
                logger.error("无法获取JWT Token中的用户名", e);
            }
        } else {
            logger.debug("No JWT token found in request");
        }

        // 验证token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            logger.debug("UserDetails loaded for user: " + userDetails.getUsername());

            // 如果token有效，配置Spring Security手动设置认证
            if (jwtUtil.validateToken(jwtToken) && username.equals(userDetails.getUsername())) {
                logger.debug("Token validated successfully for user: " + username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("Authentication set in SecurityContext for user: " + username);
            } else {
                logger.debug("Token validation failed for user: " + username);
            }
        }
        filterChain.doFilter(request, response);
    }
}