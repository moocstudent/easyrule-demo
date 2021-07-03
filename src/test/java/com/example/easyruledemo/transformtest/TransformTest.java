package com.example.easyruledemo.transformtest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.easyruledemo.BaseTest;
import com.example.easyruledemo.entity.EwsConditionsEntity;
import microsoft.exchange.webservices.data.property.complex.Rule;
import microsoft.exchange.webservices.data.property.complex.RulePredicates;
import microsoft.exchange.webservices.data.property.complex.StringList;
import org.junit.jupiter.api.Test;

/**
 * @Author: zhangQi
 * @Date: 2021-07-03 12:51
 */
class TransformTest extends BaseTest {


    //testok
    @Test
    public void testTransform(){
        String json = "{'containsSubjectStrings':['带附件','htf'],'hasAttachments':'true'}";

        JSONObject.parseObject(json);
        EwsConditionsEntity ewsConditionsEntity = JSON.parseObject(json, EwsConditionsEntity.class);
        System.out.println("ewsConditionsEntity:"+ewsConditionsEntity);

        for (String containsSubjectString : ewsConditionsEntity.getContainsSubjectStrings()) {
            System.out.println("oneContains:"+containsSubjectString);
        }

        Rule rule = new Rule();
        RulePredicates conditions = rule.getConditions();
        StringList containsSubjectStrings = conditions.getContainsSubjectStrings();



    }
}
