package com.example.easyruledemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.example.easyruledemo.mapper")
@SpringBootApplication
public class EasyruleDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyruleDemoApplication.class, args);
    }

}
