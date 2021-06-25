package com.example.easyruledemo;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.easyruledemo.BaseTest;
import com.example.easyruledemo.entity.EasyRuleEntity;
import com.example.easyruledemo.ruletest.EmailEntity;
import com.example.easyruledemo.ruletest.RuleConditionEntity;
import com.example.easyruledemo.service.IRuleService;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zhangQi
 * @Date: 2021-02-27 15:36
 */
class RuleTest extends BaseTest {

    @Autowired
    private IRuleService ruleService;

    @Test
    void testRuleAll() {
        List<?> all = ruleService.getAll();
        all.forEach(System.out::println);
    }

    @Test
    void testRuleFire() {
        List<EmailEntity> emailList = new ArrayList<>();
        emailList.add(EmailEntity.builder().subject("1").senderAddress("1").build());
        emailList.add(EmailEntity.builder().subject("系统访问操作信息").senderAddress("htffund.com").build());
        emailList.add(EmailEntity.builder().subject("3").senderAddress("3").build());
        //email仿照邮件放入列表
        Facts allFacts = new Facts();

//        for (EmailEntity email : emailList) {
//            String subject = email.getSubject();
//            String senderAddress = email.getSenderAddress();
//            //循环放入所有需要fire的fact
//            allFacts.put("subject", subject);
//            allFacts.put("senderAddress", senderAddress);
//        }
        //获取所有使用中的easy rule
        List<EasyRuleEntity> allUsedRules = ruleService.getAllUsed();
        Rules rules = new Rules();
        allUsedRules.stream()
                .map(r -> {
                    Rule rule = new RuleBuilder()
                            .name(r.getRuleName())
                            .description(r.getRuleDesc())
                            .priority(r.getPriority().intValue())
                            .when(facts -> facts.get("subject").toString().equals(JSON.parseObject(r.getRuleConditions(), RuleConditionEntity.class).getSubject()))
                            .then(facts -> ruleService.fireRuleByType(r.getActionType(), r.getActions()))
                            .build();
                    return rule;
                }).map(rl -> {
            rules.register(rl);
            return rl;
        }).collect(Collectors.toList());

        emailList.stream()
                .map(el->{
                    allFacts.put("subject",el.getSubject());
                    allFacts.put("senderAddress",el.getSenderAddress());

                    // fire rules on known facts
                    RulesEngine rulesEngine = new DefaultRulesEngine();
                    rulesEngine.fire(rules, allFacts);
                    return allFacts;
                }).count();



//
//        List<Rule> rules = allUsedRules.stream()
//                .map(r -> {
//                    System.out.println("r:"+r);
//                    Rule rule = new RuleBuilder()
//                            .name(r.getRuleName())
//                            .description(r.getRuleDesc())
//                            .priority(r.getPriority().intValue())
//                            .when(facts -> {
//                                System.out.println("facts when:"+facts);
//                                return facts.get("subject").equals(JSON.parseObject(r.getRuleConditions(), RuleConditionEntity.class).getSubject());
//                            })
////                            .then(facts -> ruleService.fireRuleByType(r.getActionType(), r.getActions()))
//                            .then(facts -> System.out.println("facts:"+facts))
//                            .build();
//                    try {
//                        rule.execute(allFacts);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return rule;
//                })
//                .collect(Collectors.toList());
//

//        //判断
//        for(EasyRuleEntity r : allUsedRules){
//            Rule rule = new RuleBuilder()
//                    .name(r.getRuleName())
//                    .description(r.getRuleDesc())
//                    .priority(r.getPriority().intValue())
//                    .when(facts -> facts.get("subject").equals(JSON.parseObject(r.getRuleConditions(), RuleConditionEntity.class).getSubject()))
//                    .then(facts -> ruleService.fireRuleByType(r.getActionType(),r.getActions()))
//                    .build();
//            try {
//                rule.execute(allFacts);
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//        }

//
//        List<List<Rule>> collect = emailList.stream()
//                .map(e -> {
//                    allFacts.put("subject", e.getSubject());
//                    allFacts.put("senderAddress", e.getSenderAddress());
//                    List<Rule> ruleList = allUsedRules.stream()
//                            .map(r -> {
//                                String subject = JSON.parseObject(r.getRuleConditions(), RuleConditionEntity.class).getSubject();
//                                System.out.println("subject in db:"+subject);
//                                Rule rule = new RuleBuilder()
//                                        .name(r.getRuleName())
//                                        .description(r.getRuleDesc())
//                                        .priority(r.getPriority().intValue())
//                                        .when(facts -> facts.get("subject").equals(JSON.parseObject(r.getRuleConditions(),RuleConditionEntity.class).getSubject()))
//                                        .then(facts -> ruleService.fireRuleByType(r.getActionType()))
//                                        .build();
//                                try {
//                                    rule.execute(allFacts);
//                                } catch (Exception exception) {
//                                    exception.printStackTrace();
//                                }
//                                return rule;
//                            })
////                            .map(r2 -> {
////                                System.out.println("r2:");
////                                try {
////                                    r2.execute(allFacts);
////                                } catch (Exception exception) {
////                                    exception.printStackTrace();
////                                }
////                                return r2;
////                            })
//                            .collect(Collectors.toList());
//                    return ruleList;
//                }).collect(Collectors.toList());
//
    }


}
