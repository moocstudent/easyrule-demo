package com.example.easyruledemo.controller;

import com.example.easyruledemo.serv.IRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * @Author: Frank
 * @Date: 2021-07-01 9:08
 */
@RestController
@RequestMapping("easyRule")
public class EasyRuleController {

    @Autowired
    private IRuleService ruleService;
}
