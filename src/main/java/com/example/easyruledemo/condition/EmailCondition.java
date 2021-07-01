package com.example.easyruledemo.condition;

import com.alibaba.fastjson.JSON;
import com.example.easyruledemo.entity.RuleConditionEntity;
import com.example.easyruledemo.rules.RuleStuff;
import lombok.Builder;
import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;

/**
 * @Author: Frank
 * @Date: 2021-06-30 8:37
 */
@Builder
public class EmailCondition implements Condition {

        private String ruleConditions;

//    public EmailCondition setRuleConditions(String ruleConditions) {
//        System.out.println("set ruleConditions");
//        this.ruleConditions = ruleConditions;
//        return this;
//    }

    /**
     * @param facts
     * @return
     */
    @Override
    public boolean evaluate(Facts facts) {
        RuleConditionEntity ruleConditionEntity = JSON.parseObject(ruleConditions, RuleConditionEntity.class);
        System.out.println("ruleConditionEntity:"+ruleConditionEntity);
        System.out.println("facts::"+facts);
        return RuleStuff.rule(ruleConditionEntity,facts.asMap());
    }

}
