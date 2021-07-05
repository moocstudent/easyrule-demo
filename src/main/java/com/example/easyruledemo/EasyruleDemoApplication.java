package com.example.easyruledemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//import org.mybatis.spring.annotation.MapperScan;


@MapperScan("com.example.easyruledemo.mapper")
@SpringBootApplication
@EnableScheduling
public class EasyruleDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyruleDemoApplication.class, args);
    }

}
