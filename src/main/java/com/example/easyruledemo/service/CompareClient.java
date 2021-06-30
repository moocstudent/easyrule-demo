package com.example.easyruledemo.service;

import org.jeasy.rules.api.Rule;
import org.jeasy.rules.core.RuleBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: Frank
 * @Date: 2021-06-28 16:51
 */
@Component
public class CompareClient {

    @PostConstruct
    public void compare(){
//        CompareDiffResultServiceImpl.getDiffByTwoData()

        Rule rule1 = new RuleBuilder()
                .name("规则1")
                .description("介绍")
                .priority(1)
                .when(facts -> true == true)
                .then(facts -> System.out.println("规则3"))
                .build();



    }
}
