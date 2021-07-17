package com.example.easyruledemo;


import com.alibaba.fastjson.JSON;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.entity.EwsRuleEntity;
import com.example.easyruledemo.entity.sub.EwsActionsEntity;
import com.example.easyruledemo.entity.sub.EwsConditionsEntity;
import com.example.easyruledemo.service.IEwsRuleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: Frank
 * @Date: 2021-06-30 16:09
 */
class EmailRuleFilterTest extends BaseTest {

    @Autowired
    private IEwsRuleService ewsRuleService;


    //testok

    /**
     * 测试权重生成到邮箱中后,权重排列的顺序不按照正常顺序1-10,而是有部分乱序如2,1-3,4,5,10,7
     * 而outlook.live.com的规则执行是按从上到下来执行规则
     */
    @Test
    public void testRuleFireEws() {
        ewsRuleService.ewsRuleFire(EwsRuleEntity
                .builder()
                .displayName("only 7")
                .priority(7)
                .itemMovedFolderIdStr("AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAWrq7QAAAA==")
                .build());
    }

    //testok
    //testok
    @Test
    public void testRuleFireEwsJustThisOne() {
        ewsRuleService.ewsRuleFire(EwsRuleEntity
                        .builder()
                        .displayName("just this one 1")
                .priority(3)
                        .conditions(JSON.toJSONString(EwsConditionsEntity.builder()
                                .containsSubjectStrings(Arrays.asList("带附件"))
                                .fromAddresses(Arrays.asList("34384068111111111@qq.com"))
                                .build()))
                        .actions(JSON.toJSONString(EwsActionsEntity.builder()
                                .moveToFolder("attach").build()))
                        .build(),
                EwsMailEntity.builder()
                        .mailId(1L)
                        .email("implementsteam@outlook.com")
                        .password("zhangqi1112")
                        .build());
//                .moveToFolder("AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAWrq7QAAAA==").build()))
//                .build(),
//                EwsMailEntity.builder()
//                        .email("implementsteam@outlook.com")
//                        .password("zhangqi1112")
//                        .build());
    }

    @Test
    public void testRuleDisabled() {
        Integer disabledRuleByEmAddr = ewsRuleService.disabledRuleByEmAddr("implementsteam@outlook.com");
        System.out.println("disabled rule:" + disabledRuleByEmAddr);
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
        Integer deleteRuleByEmAddr = ewsRuleService.deleteRuleByEmAddr(EwsMailEntity.builder()
                .email("implementsteam@outlook.com").password("zhangqi1112").build());
        System.out.println("deleteSize:" + deleteRuleByEmAddr);
    }


}
