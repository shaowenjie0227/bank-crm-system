package com.bank.crm.config;


import com.bank.crm.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Object> handleAllExceptions(Exception e, HttpServletRequest request) {
        log.error("请求地址: {}, 发生异常: {}", request.getRequestURI(), e.getMessage(), e);
        
        // 返回详细的错误信息
        return Result.error(500, "服务器内部错误: " + e.getMessage());
    }
}