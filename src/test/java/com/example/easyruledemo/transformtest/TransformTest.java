package com.example.easyruledemo.transformtest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.easyruledemo.BaseTest;
import com.example.easyruledemo.entity.EwsRuleEntity;
import com.example.easyruledemo.entity.EwsTopicEntity;
import com.example.easyruledemo.entity.sub.EwsActionsEntity;
import com.example.easyruledemo.entity.sub.EwsConditionsEntity;
import com.example.easyruledemo.entity.sub.ItemActionsEntity;
import com.example.easyruledemo.enums.ItemActionType;
import com.example.easyruledemo.enums.RuleType;
import com.example.easyruledemo.service.IEwsRuleService;
import microsoft.exchange.webservices.data.property.complex.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-03 12:51
 */
class TransformTest extends BaseTest {

    @Autowired
    private IEwsRuleService ewsRuleService;

    @Test
    public void testTransformAction(){
        //
        String actions = "{'downloadPath':'D://download1'}";
        String actions2 = "{'downloadPath':'123'}";
        JSONObject jsonObject = JSONObject.parseObject(actions);

//        ItemActionsEntity itemActionsEntity = JSONObject.parseObject(actions, ItemActionsEntity.class);
        ItemActionsEntity itemActionsEntity = JSON.toJavaObject(jsonObject, ItemActionsEntity.class);
        System.out.println(itemActionsEntity);
//        System.out.println();
//
//        JSONObject jsonObject1 = JSONObject.parseObject(actions2);
////        ItemActionsEntity itemActionsEntity = JSONObject.parseObject(actions, ItemActionsEntity.class);
//        ItemActionsEntity itemActionsEntity1 = JSON.toJavaObject(jsonObject1, ItemActionsEntity.class);
//        System.out.println(itemActionsEntity1);
    }

    //testok
    //testok actions
    @Test
    public void testTransform1(){
        //testok
        String json = "{'containsSubjectStrings':['带附件','htf'],'hasAttachments':'true'}";
        String json2 = "{'assignCategories':['111','222'],'markAsRead':'true'}";

        EwsRuleEntity build = EwsRuleEntity.builder()
                .displayName("123 peach")
                .conditions(json)
                .actions(json2)
                .priority(1)
                .build();
//        Rule rule1 = ewsRuleService.transformRuleEntity(build);
//        System.out.println("rule1:"+rule1);
//        System.out.println("rule1 bool:"+rule1.getConditions().getHasAttachments());
//        System.out.println("rule1 stringlist:"+rule1.getConditions().getContainsSubjectStrings());
//        System.out.println("rule1 actions stringlist:"+rule1.getActions().getAssignCategories());
//        System.out.println("rule1 actions stringlist:"+rule1.getActions().getMarkAsRead());

//        String actionJson = "{}";
    }

    //testok
    @Test
    public void testTransform2(){
        //test round 2

        EwsConditionsEntity conditionsEntity = EwsConditionsEntity.builder()
                .containsSubjectStrings(Arrays.asList("909","25k","planCircles"))
                .hasAttachments(true)
                .fromAddresses(Arrays.asList("implementsteam@163.com"))
                .build();

        String json3 = "{'containsSubjectStrings':['带附件','htf'],'hasAttachments':'true'}";
        EwsActionsEntity actionsEntity = EwsActionsEntity.builder()
                .moveToFolder("folderId-unionId-00000000folder")
                .redirectToRecipients(Arrays.asList("implementsteam@163.com"))
                .build();

        System.out.println("actionsJSON:"+JSON.toJSONString(actionsEntity));
        EwsRuleEntity ruleEntity = EwsRuleEntity.builder()
                .displayName("apple 123")
                .conditions(JSON.toJSONString(conditionsEntity))
                .actions(JSON.toJSONString(actionsEntity))
                .priority(1)
                .build();

//        Rule rule2 = ewsRuleService.transformRuleEntity(ruleEntity);
//        System.out.println("rule2:"+rule2);
//        System.out.println("rule2 bool:"+rule2.getConditions().getHasAttachments());
//        System.out.println("rule2 stringlist:"+rule2.getConditions().getContainsSubjectStrings());
//        System.out.println("rule2 actions move to folder:"+rule2.getActions().getMoveToFolder());
//        System.out.println("rule2 actions stringlist redirectTo :"+rule2.getActions().getRedirectToRecipients().getItems().get(0));
    }

    //testok
    @Test
    public void transferTopicAndRuleJson(){
        EwsRuleEntity rule1 = EwsRuleEntity.builder()
//                .ruleId(111L) //正式oracle是字符串
                .ruleId("str1") //正式oracle是字符串
                .displayName("rule 1")
                .ruleDesc("desc of rule")
                .priority(1)
                .isEnabled(true)
                .deleteFlag(0)
                .itemActionType(ItemActionType.D.getCode())
                .ruleType(RuleType.MOVE.getCode())
                .build();
        EwsRuleEntity rule2 = EwsRuleEntity.builder()
//                .ruleId(222L) //正式oracle是字符串
                .ruleId("str2") //正式oracle是字符串
                .displayName("rule 2")
                .ruleDesc("desc of rule")
                .priority(2)
                .isEnabled(true)
                .deleteFlag(0)
                .itemActionType(ItemActionType.DC.getCode())
                .ruleType(RuleType.MOVE.getCode())
                .build();

        //或由前端生成topicConfig 或根据传送过来的ruleId进行组合生成
//        ewsRuleService.configTheTopicConfigStr(ids);
//just test
        JSONObject configJson = new JSONObject();
        List<EwsRuleEntity> ruleList=  new ArrayList<>();
        ruleList.add(rule1);
        ruleList.add(rule2);
        ruleList.forEach(rule -> {
            String itemActionType = rule.getItemActionType();
            configJson.put(itemActionType, rule.getRuleId());
        });


        System.out.println(configJson);
        System.out.println(configJson.toJSONString());

        EwsTopicEntity topic = EwsTopicEntity.builder()
                .topicName("topic t1")
                .topicDesc("desc statement")
                .deleteFlag(0)
                .topicConfig(configJson.toJSONString())
                .build();
        String topicJSON = JSON.toJSONString(topic);

        System.out.println("topic json:"+topicJSON);
        System.out.println("topic entity:"+topic);

        JSONObject configJsonObj = JSONObject.parseObject(configJson.toJSONString());
        System.out.println("configJsonObj:"+configJsonObj);
        JSONObject configJsonObj2 = JSONObject.parseObject(topic.getTopicConfig());
        System.out.println("configJsonObj2:"+configJsonObj2);
    }
}
