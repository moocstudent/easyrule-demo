package com.example.easyruledemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * @Author: Frank
 * @Date: 2021-07-01 9:08
 */
@RestController
@RequestMapping("")
public class EasyRuleController {
//
//    @Autowired
//    private IRuleService ruleService;
    @GetMapping("/notify")
    public String notify1(){
        System.out.println("notify invoke");
        return "notify invoke";
    }
}
