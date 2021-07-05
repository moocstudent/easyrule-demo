package com.example.easyruledemo.transformtest;

import com.example.easyruledemo.BaseTest;
import com.example.easyruledemo.entity.EwsRuleEntity;
import com.example.easyruledemo.service.IEwsRuleService;
import microsoft.exchange.webservices.data.property.complex.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: zhangQi
 * @Date: 2021-07-03 12:51
 */
class TransformTest extends BaseTest {

    @Autowired
    private IEwsRuleService ewsRuleService;

    //testok
    //testok actions
    @Test
    public void testTransform(){
        String json = "{'containsSubjectStrings':['带附件','htf'],'hasAttachments':'true'}";
        String json2 = "{'assignCategories':['111','222'],'markAsRead':'true'}";
//
//        JSONObject.parseObject(json);
//        EwsConditionsEntity ewsConditionsEntity = JSON.parseObject(json, EwsConditionsEntity.class);
//        System.out.println("ewsConditionsEntity:"+ewsConditionsEntity);
//
//        for (String containsSubjectString : ewsConditionsEntity.getContainsSubjectStrings()) {
//            System.out.println("oneContains:"+containsSubjectString);
//        }
//
//        Rule rule = new Rule();
//        RulePredicates conditions = rule.getConditions();
//        StringList containsSubjectStrings = conditions.getContainsSubjectStrings();
//        rule.getConditions().getHasAttachments();
        EwsRuleEntity build = EwsRuleEntity.builder()
                .displayName("123 peach")
                .conditions(json)
                .actions(json2)
                .priority(1)
                .build();
        Rule rule1 = ewsRuleService.transformRuleEntity(build);
        System.out.println("rule1:"+rule1);
        System.out.println("rule1 bool:"+rule1.getConditions().getHasAttachments());
        System.out.println("rule1 stringlist:"+rule1.getConditions().getContainsSubjectStrings());
        System.out.println("rule1 actions stringlist:"+rule1.getActions().getAssignCategories());
        System.out.println("rule1 actions stringlist:"+rule1.getActions().getMarkAsRead());
    }
}
