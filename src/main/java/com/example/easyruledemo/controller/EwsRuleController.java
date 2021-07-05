package com.example.easyruledemo.controller;

import com.example.easyruledemo.entity.EwsRuleEntity;
import com.example.easyruledemo.service.IEwsRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zhangQi
 * @Date: 2021-07-05 13:36
 */
@RequestMapping("/ews/rule")
@RestController
public class EwsRuleController {

    @Autowired
    private IEwsRuleService ewsRuleService;

    @PostMapping
    public void addRule(@RequestBody EwsRuleEntity ewsRule){

    }

    @PutMapping
    public void updateRule(@RequestBody EwsRuleEntity ewsRule){

    }

    @PostMapping
    public void ruleList(@RequestBody EwsRuleEntity ewsRule){

    }

    @GetMapping("/{ruleId}")
    public void oneRule(@PathVariable("ruleId") String ruleId){

    }

    @DeleteMapping("/{ruleId}")
    public void delRule(@PathVariable("ruleId") String ruleId){

    }
}
