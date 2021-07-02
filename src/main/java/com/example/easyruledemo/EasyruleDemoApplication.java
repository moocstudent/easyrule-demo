package com.example.easyruledemo;

import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.Rule;
//import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


//@MapperScan("com.example.easyruledemo.mapper")
@SpringBootApplication
@EnableScheduling
public class EasyruleDemoApplication {

    public static void main(String[] args) {




        SpringApplication.run(EasyruleDemoApplication.class, args);
    }

}
