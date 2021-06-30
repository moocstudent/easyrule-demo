package com.example.easyruledemo.rules;

import com.example.easyruledemo.entity.RuleConditionEntity;
import com.example.easyruledemo.util.MapObjUtil;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @Author: Frank
 * @Date: 2021-06-24 18:40
 */
@Component
public class RuleStuff {

//    Facts allFacts = new Facts();
//
//    public void ruleInit() {
//
//        org.jeasy.rules.api.Rule build = new RuleBuilder().name("rule name from db")
//                .description("rule desc")
//                .when(facts -> facts.get("Subject").equals("1"))
//                .then(facts -> System.out.println("testok"))
//                .build();
//
//        allFacts.put("Subject", "1");
////        facts.put("Subject","2");
//
//
//        try {
//            build.execute(allFacts);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//        }
//
//
//    }
//
//
//    @Rule(priority = 1)
//    public static class SubjectSenderAddressRule1 {
//        @Condition
////        public boolean subjectAndSenderAddressCondition1(){
//        public boolean subjectAndSenderAddressCondition1(@Fact("subject") String subject, @Fact("senderAddress") String senderAddress) {
//            String subjectInDb1 = "get from db1";
//            String senderAddressInDb1 = "get from db address1";
//            return subjectInDb1.contains(subject) && senderAddressInDb1.contains(senderAddress);
//        }
//
//        @Action
//        public void condition1Action() {
//            System.out.println("condition1Action");
//        }
//    }
//
////    @Rule(priority = 2)
////    public static class SubjectSenderAddressRule2{
////        @Condition
//////        public boolean subjectAndSenderAddressCondition2(){
////        public boolean subjectAndSenderAddressCondition2(@Fact("subject") String subject,@Fact("senderAddress") String senderAddress){
////            String subjectInDb2 = "get from db2";
////            String senderAddressInDb2 = "get from db address2";
////            return subjectInDb2.contains(subject)&&senderAddressInDb2.contains(senderAddress);
////        }
////
////        @Action
////        public void condition2Action(){
////            System.out.println("condition2Action");
////        }
////    }


    /**
     * 静态调用
     */
    public static boolean rule(final RuleConditionEntity entity, final Map factsMap) {
        //判定事实facts里的参数,对比规则表里设定的
        Object factEntity = null;
        try {
            factEntity = MapObjUtil.map2Object(factsMap, RuleConditionEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return Objects.equals(entity,factEntity);
    }


    public static Integer fire(final String actionType,final String actions) {
        System.out.println("fire rule with actionType:" + actionType + ",download or download and copy file from:" + actions);
        return null;
    }

}
