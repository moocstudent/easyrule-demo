//package com.example.easyruledemo.serv.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.example.easyruledemo.entity.EasyRuleEntity;
//import com.example.easyruledemo.mapper.EasyRuleMapper;
//import com.example.easyruledemo.serv.IRuleService;
//import org.springframework.stereotype.Service;
//
//import java.lang.reflect.Field;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//
///**
// * @Author: Frank
// * @Date: 2021-06-25 9:36
// */
//@Service
//public class RuleServiceImpl extends ServiceImpl<EasyRuleMapper, EasyRuleEntity> implements IRuleService {
//    @Override
//    public Integer execRuleByCode(int ruleCode) {
//        System.out.println("trigger rule :" + ruleCode);
//        return null;
//    }
//
//    @Override
//    public List<?> getAll() {
//        return super.list();
//    }
//
//    @Override
//    public List<EasyRuleEntity> getAllUsed() {
//        List<EasyRuleEntity> easyRuleEntities = baseMapper.selectList(new LambdaQueryWrapper<EasyRuleEntity>()
//                .eq(EasyRuleEntity::getDeleteFlag, 0));
//        return easyRuleEntities;
//    }
//
//    @Override
//    public Integer fireRuleByType(String ruleType, String action) {
//        System.out.println("fire rule with ruleType:" + ruleType + ",download or download and copy file from:" + action);
//        return null;
//    }
//
////    @Override
////    public boolean rule(final RuleConditionEntity entity, final Map factsMap) {
////        //key集合
////        Set factKeys = factsMap.keySet();
////        Object collect = factKeys.stream()
////                .map(factk -> {
////                    try {
////                        Object factValue = factsMap.get(factk);
////                        System.out.println("factValue:"+factValue);
////                        if (factValue != null && factValue != "") {
////                            Field field = entity.getClass().getDeclaredField(factk.toString());
////                            field.setAccessible(true);
////                            Object entityConditionValue = null;
////                            try {
////                                entityConditionValue = field.get(entity);
////                                System.out.println("entityConditionValue:"+entityConditionValue);
////                            } catch (IllegalAccessException e) {
////                                e.printStackTrace();
////                            }
////                            if (factValue.equals(entityConditionValue)) {
////                                return true;
////                            }
////                        }
////                    } catch (NoSuchFieldException e) {
////                        e.printStackTrace();
////                    }
////                    return false;
////                })
////                .filter(judge -> false)
////                .collect(Collectors.toList());
////
////        System.out.println("ruleFalseCollect:"+collect);
////        if (collect!=null ) {
////            //有一条false.即条件不满足
////            return false;
////        }
////        return true;
////    }
//}
