package com.bank.crm.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 编码配置类，确保UTF-8编码
 */
@Configuration
public class EncodingConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 配置StringHttpMessageConverter为UTF-8
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        stringConverter.setWriteAcceptCharset(false);
        
        // 配置MappingJackson2HttpMessageConverter为UTF-8
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setDefaultCharset(StandardCharsets.UTF_8);
        
        // 添加到转换器列表中
        converters.add(stringConverter);
        converters.add(jsonConverter);
    }
}