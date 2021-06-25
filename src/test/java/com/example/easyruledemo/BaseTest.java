package com.example.easyruledemo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.web.context.WebApplicationContext;

/**
 * @Author: zhangQi
 * @Date: 2021-01-21 14:26
 */
//@RunWith(SpringRunner.class)
@SpringBootTest(classes = EasyruleDemoApplication.class)
class BaseTest {

//    protected MockMvc mockMvc;
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    @BeforeEach
//    void setUp(){
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
//    }

    @AfterEach
    void tearDown(){
    }

}
