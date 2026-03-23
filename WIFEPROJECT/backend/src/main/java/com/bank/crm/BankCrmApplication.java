package com.bank.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bank.crm.mapper")
public class BankCrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankCrmApplication.class, args);
    }

}