package com.bank.crm.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bank.crm.vo.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证入口点，处理未认证的请求
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        Result<Object> result = Result.error(401, "未授权，请登录");
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().println(mapper.writeValueAsString(result));
    }
}