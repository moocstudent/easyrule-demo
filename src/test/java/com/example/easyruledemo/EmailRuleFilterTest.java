package com.example.easyruledemo;


import com.example.easyruledemo.entity.EwsRuleEntity;
import com.example.easyruledemo.service.IEwsRuleService;
import microsoft.exchange.webservices.data.property.complex.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author: Frank
 * @Date: 2021-06-30 16:09
 */
class EmailRuleFilterTest extends BaseTest {

    @Autowired
    private IEwsRuleService ewsRuleService;


    //testok
    @Test
    public void testRuleFireEws() {
        ewsRuleService.ewsRuleFire(EwsRuleEntity
                .builder()
                .displayName("only 1")
                .itemMovedFolderIdStr("AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAWrq7QAAAA==")
                .build());
    }

    //testok
    @Test
    public void testRuleFireEwsJustThisOne(){
        ewsRuleService.ewsRuleFireJustThisOne(EwsRuleEntity
                .builder()
                .displayName("just this one 4")
                .itemMovedFolderIdStr("AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAWrq7QAAAA==")
                .build(),"implementsteam@outlook.com");
    }

    @Test
    public void testRuleDisabled(){
        Integer disabledRuleByEmAddr = ewsRuleService.disabledRuleByEmAddr("implementsteam@outlook.com");
        System.out.println("disabled rule:"+disabledRuleByEmAddr);
    }

    @Test
    public void testEmailRules() {
        List<EwsRuleEntity> ewsRulesByEmAddr = ewsRuleService.getEwsRulesByEmAddr("implementsteam@outlook.com");
        if (ewsRulesByEmAddr.size() > 0) {
            ewsRulesByEmAddr.forEach(System.out::println);
        } else {
            System.out.println("0");
        }
    }

    @Test
    public void testEmailDelete() {
        Integer deleteRuleByEmAddr = ewsRuleService.deleteRuleByEmAddr("implementsteam@outlook.com");
        System.out.println("deleteSize:" + deleteRuleByEmAddr);
    }



}
