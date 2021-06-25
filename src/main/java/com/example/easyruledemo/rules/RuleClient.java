package com.example.easyruledemo.rules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RulesEngineParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Frank
 * @Date: 2021-06-24 19:06
 */
@Component
public class RuleClient {

    public static void main(String[] args) {
        RulesEngineParameters parameters = new RulesEngineParameters().skipOnFirstAppliedRule(true);
        RulesEngine defaultRulesEngine = new DefaultRulesEngine(parameters);

        Rules rules = new Rules();
        rules.register(new RuleStuff.SubjectSenderAddressRule1());
//        rules.register(new RuleStuff.SubjectSenderAddressRule2());

        Facts facts = new Facts();

        facts.put("subject", "get from db1");
        facts.put("senderAddress", "get from db address1");
        defaultRulesEngine.fire(rules, facts);
        System.out.println();

        RuleStuff ruleStuff = new RuleStuff();
        ruleStuff.ruleInit();



    }


}
