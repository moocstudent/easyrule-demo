package com.example.easyruledemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.easyruledemo.container.EwsExContainer;
import com.example.easyruledemo.entity.EwsFoldersEntity;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.entity.sub.EwsActionsEntity;
import com.example.easyruledemo.entity.sub.EwsConditionsEntity;
import com.example.easyruledemo.entity.EwsRuleEntity;
import com.example.easyruledemo.enums.RuleEnum;
import com.example.easyruledemo.mapper.EwsRuleMapper;
import com.example.easyruledemo.service.IEwsRuleService;
import com.example.easyruledemo.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.property.complex.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Frank
 * @Date: 2021-07-01 13:55
 */
@Service
@Slf4j
public class EwsRuleServiceImpl extends ServiceImpl<EwsRuleMapper, EwsRuleEntity>
        implements IEwsRuleService {

    @Transactional
    @Override
    public Integer ewsRuleFireJustThisOne(EwsRuleEntity ewsRuleEntity, String emailAddr) {
        Integer disabledRuleSize = this.disabledRuleByEmAddr(emailAddr);
        System.out.println("disabled rule size:" + disabledRuleSize);
        Integer fireRuleCode = this.ewsRuleFire(ewsRuleEntity);
        if (fireRuleCode > 0) {
            System.out.println("fire rule just this one is ok:" + fireRuleCode);
            return fireRuleCode;
        } else {
            return fireRuleCode;
        }
    }

    @Transactional
    @Override
    public Integer ewsRuleFireJustThisOne(EwsRuleEntity ewsRuleEntity, EwsMailEntity ewsMail) {
        Integer disabledRuleSize = this.disabledRuleByEmAddr(ewsMail);
        System.out.println("disabled rule size:" + disabledRuleSize);
        Integer fireRuleCode = this.ewsRuleFire(ewsRuleEntity,ewsMail);
        if (fireRuleCode > 0) {
            System.out.println("fire rule just this one is ok:" + fireRuleCode);
            return fireRuleCode;
        } else {
            return fireRuleCode;
        }
    }

    @Override
    public Integer ewsRuleFire(EwsRuleEntity ewsRuleEntity) {
        Rule rule = this.transformRuleEntity(ewsRuleEntity);
        //是否启用
        rule.setIsEnabled(true);

        CreateRuleOperation createOperation = new CreateRuleOperation(rule);
        List<RuleOperation> ruleList = new ArrayList<RuleOperation>();
        ruleList.add(createOperation);
        try {
            //执行规则更新
            EwsExContainer.defaultExchangeService().updateInboxRules(ruleList, true);
//            EwsContainer.defaultExchangeService().update(ruleList,true);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    @Override
    public Integer ewsRuleFire(EwsRuleEntity ewsRuleEntity, EwsMailEntity ewsMail) {
        Rule rule = this.transformRuleEntity(ewsRuleEntity);
        log.debug("transformRule:{}",rule);
        //是否启用
        rule.setIsEnabled(true);
        CreateRuleOperation createOperation = new CreateRuleOperation(rule);
        List<RuleOperation> ruleList = new ArrayList<RuleOperation>();
        ruleList.add(createOperation);
        try {
            //执行规则更新
            EwsExContainer.getExchangeService(ewsMail.getEmail(),ewsMail.getPassword())
                    .updateInboxRules(ruleList, true);
//            EwsContainer.defaultExchangeService().update(ruleList,true);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    @Override
    public List<EwsRuleEntity> getEwsRulesByEmAddr(String emailAddr) {
        RuleCollection ruleCollection = null;
        try {
            ruleCollection = EwsExContainer.defaultExchangeService().getInboxRules(emailAddr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Collection count: " + ruleCollection.getCount());
        List<EwsRuleEntity> ewsRuleEntityList = null;
        if (ruleCollection.getCount() > 0) {
            if (ewsRuleEntityList == null) {
                ewsRuleEntityList = new ArrayList<>();
            }
        }
        for (Rule rule : ruleCollection) {
            boolean add = ewsRuleEntityList.add(BeanUtil.getClass(EwsRuleEntity.class, rule).orElse(new EwsRuleEntity()));
            System.out.println("add one?" + add);
        }
        return ewsRuleEntityList;
    }

    @Override
    public Integer deleteRuleByEmAddr(String emailAddr) {
        RuleCollection ruleCollection = null;
        try {
            ruleCollection = EwsExContainer.defaultExchangeService().getInboxRules(emailAddr);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        System.out.println("Collection count: " + ruleCollection.getCount());
        List<RuleOperation> deleterules = new ArrayList<RuleOperation>();
        // Write the DisplayName and ID of each rule.
        for (Rule rule : ruleCollection) {
            System.out.println(rule.getDisplayName());
            System.out.println(rule.getId());
            DeleteRuleOperation d = new DeleteRuleOperation(rule.getId());
            deleterules.add(d);
        }
        try {
            EwsExContainer.defaultExchangeService().updateInboxRules(deleterules, true);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return deleterules.size();
    }

    @Override
    public Integer deleteRuleByEmAddr(EwsMailEntity ewsMail) {
        RuleCollection ruleCollection = null;
        try {
            ruleCollection = EwsExContainer.getExchangeService(ewsMail.getEmail(), ewsMail.getPassword())
                    .getInboxRules(ewsMail.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        System.out.println("Collection count: " + ruleCollection.getCount());
        List<RuleOperation> deleterules = new ArrayList<RuleOperation>();
        // Write the DisplayName and ID of each rule.
        for (Rule rule : ruleCollection) {
            System.out.println(rule.getDisplayName());
            System.out.println(rule.getId());
            DeleteRuleOperation d = new DeleteRuleOperation(rule.getId());
            deleterules.add(d);
        }
        try {
            EwsExContainer.getExchangeService(ewsMail.getEmail(), ewsMail.getPassword())
                    .updateInboxRules(deleterules, true);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return deleterules.size();
    }

    @Override
    public Integer disabledRuleByEmAddr(String emailAddr) {
        RuleCollection ruleCollection = null;
        try {
            ruleCollection = EwsExContainer.defaultExchangeService().getInboxRules(emailAddr);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        System.out.println("Collection count: " + ruleCollection.getCount());
        List<RuleOperation> disabledRules = new ArrayList<RuleOperation>();

        // Write the DisplayName and ID of each rule.
        for (Rule rule : ruleCollection) {
            System.out.println(rule.getDisplayName());
            System.out.println(rule.getId());
            System.out.println(rule.getIsEnabled());
            rule.setIsEnabled(false);
//            rule.getActions().setMoveToFolder(new FolderId(WellKnownFolderName.Inbox));
            System.out.println("rule isEnabled must be false now:" + rule.getIsEnabled());
//            System.out.println("rule disabled:"+rule);
            SetRuleOperation updateToDisabledRule = new SetRuleOperation(rule);
            disabledRules.add(updateToDisabledRule);
        }
        try {
            EwsExContainer.defaultExchangeService().updateInboxRules(disabledRules, true);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return disabledRules.size();
    }

    @Override
    public Integer disabledRuleByEmAddrAndRuleId(String emailAddr, String ruleId) {
        return null;
    }

    @Override
    public Integer disabledRuleByEmAddr(EwsMailEntity ewsMail) {
        RuleCollection ruleCollection = null;
        try {
            ruleCollection = EwsExContainer
                    .getExchangeService(ewsMail.getEmail(), ewsMail.getPassword())
                    .getInboxRules(ewsMail.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        System.out.println("Collection count: " + ruleCollection.getCount());
        List<RuleOperation> disabledRules = new ArrayList<RuleOperation>();

        if (ruleCollection.getCount()==0){
            return 0;
        }
        // Write the DisplayName and ID of each rule.
        for (Rule rule : ruleCollection) {
            System.out.println(rule.getDisplayName());
            System.out.println(rule.getId());
            System.out.println(rule.getIsEnabled());
            rule.setIsEnabled(false);
//            rule.getActions().setMoveToFolder(new FolderId(WellKnownFolderName.Inbox));
            System.out.println("rule isEnabled must be false now:" + rule.getIsEnabled());
//            System.out.println("rule disabled:"+rule);
            SetRuleOperation updateToDisabledRule = new SetRuleOperation(rule);
            disabledRules.add(updateToDisabledRule);
        }
        try {
            EwsExContainer.getExchangeService(ewsMail.getEmail(),ewsMail.getPassword())
                    .updateInboxRules(disabledRules, true);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return disabledRules.size();
    }

    @Override
    public Rule transformRuleEntity(EwsRuleEntity ewsRuleEntity) {
        //basic field copy
        Rule rule = BeanUtil.getClass(Rule.class, ewsRuleEntity).orElse(new Rule());
        System.out.println("impl name:" + rule.getDisplayName());
        //the conditions actions and others

        EwsConditionsEntity ewsConditionsEntity
                = JSON.parseObject(ewsRuleEntity.getConditions(), EwsConditionsEntity.class);
        EwsActionsEntity ewsActionsEntity
                = JSON.parseObject(ewsRuleEntity.getActions(), EwsActionsEntity.class);

        this.reflectConditions(ewsConditionsEntity, rule);
        this.reflectActions(ewsActionsEntity, rule);
        //todo 设定folder和folder unionId 邮箱关联 actions

//
//        RulePredicates conditions = rule.getConditions();
//        StringList containsSubjectStrings = conditions.getContainsSubjectStrings();
//        //testok
//        containsSubjectStrings.add("带附件");
//        RuleActions actions = rule.getActions();
//        try {
////            actions.setMoveToFolder(new FolderId(WellKnownFolderName.DeletedItems));
//            actions.setMoveToFolder(FolderId.getFolderIdFromString(ewsRuleEntity.getItemMovedFolderIdStr()));
////            actions.set
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("set error");
//            return null;
//        }
        return rule;

    }

    @Override
    public List<EwsRuleEntity> getRulesByTopicId(String topicId) {
        //关联查询, 获取topicId对应的ruleList table:ews_topic_rule_relation
        return baseMapper.listRuleByTopicId(topicId);
    }

    @Override
    public Integer saveOne(EwsRuleEntity ewsRule) {
        return baseMapper.insert(ewsRule);
    }

    @Override
    public Boolean saveOrUpdateRule(EwsRuleEntity ewsRule) {
        return super.saveOrUpdate(ewsRule);
    }

    @Override
    public List<EwsRuleEntity> listSelective(EwsRuleEntity ewsRule) {
        return new LambdaQueryChainWrapper<EwsRuleEntity>(baseMapper)
                .eq(EwsRuleEntity::getDeleteFlag,0)
                .like(StringUtils.isEmpty(ewsRule.getDisplayName()),EwsRuleEntity::getDisplayName,ewsRule.getDisplayName())
                .eq(StringUtils.isEmpty(ewsRule.getIsEnabled()), EwsRuleEntity::getIsEnabled,ewsRule.getIsEnabled())
                .eq(StringUtils.isEmpty(ewsRule.getItemActionType()), EwsRuleEntity::getItemActionType,ewsRule.getItemActionType())
                .orderByDesc(EwsRuleEntity::getRuleId)
                .list();
    }

    @Override
    public List<EwsRuleEntity> listRulesByTopicConfig(String topicConfig) {
        return null;
    }

    @Override
    public List<EwsRuleEntity> filterRuleByConfigEnum(String config,RuleEnum... ruleTypes) {
        JSONObject ruleConfigJsonObject = JSONObject.parseObject(config);
        List<RuleEnum> ruleEnums = Arrays.asList(ruleTypes);
        List<String> ruleIdList = ruleEnums.stream()
                .filter(rull -> {
                    return ruleConfigJsonObject.containsKey(rull.getCode());
                })
                .map(rule -> {
                    return ruleConfigJsonObject.getString(rule.getCode());
                })
                .collect(Collectors.toList());
        List<EwsRuleEntity> ruleEntityList = new ArrayList<>();
        for(String ruleId:ruleIdList){
//            if(ruleEntityList==null){
//                ruleEntityList = new ArrayList<>();
//            }
            //fixme 需要加一个mapper sql 根据id查实体list
            EwsRuleEntity ruleEntity = this.getById(ruleId);
            ruleEntityList.add(ruleEntity);
        }
        return ruleEntityList;
    }

    @Override
    public List<EwsRuleEntity> filterRuleByConfigEnum(String config, List<RuleEnum> ruleEnums) {
        JSONObject ruleConfigJsonObject = JSONObject.parseObject(config);
        List<String> ruleIdList = ruleEnums.stream()
                .filter(rull -> {
                    return ruleConfigJsonObject.containsKey(rull.getCode());
                })
                .map(rule -> {
                    return ruleConfigJsonObject.getString(rule.getCode());
                })
                .collect(Collectors.toList());
        List<EwsRuleEntity> ruleEntityList = new ArrayList<>();
        for(String ruleId:ruleIdList){
//            if(ruleEntityList==null){
//                ruleEntityList = new ArrayList<>();
//            }
            //fixme 需要加一个mapper sql 根据id查实体list
            EwsRuleEntity ruleEntity = this.getById(ruleId);
            ruleEntityList.add(ruleEntity);
        }
        return ruleEntityList;
    }

    @Override
    public EwsRuleEntity findOne(String ruleId) {
        return baseMapper.selectById(ruleId);
    }

    @Override
    public Integer delOne(String ruleId) {
        return baseMapper.deleteById(ruleId);
    }

    //testok
    private void reflectConditions(EwsConditionsEntity ewsConditionsEntity, Rule rule) {
        try {
            Field[] declaredFields = ewsConditionsEntity.getClass().getDeclaredFields();
            for (Field conditionF : declaredFields) {
                conditionF.setAccessible(true);
                if (conditionF.get(ewsConditionsEntity) != null) {
                    String conditionFName = conditionF.getName();
                    RulePredicates conditions = rule.getConditions();
                    Field declaredFieldPredicates = conditions.getClass().getDeclaredField(conditionFName);
                    declaredFieldPredicates.setAccessible(true);
                    if (conditionFName.endsWith("Strings")) {
                        StringList thisConditionList = new StringList((Iterable<String>) conditionF.get(ewsConditionsEntity));
                        declaredFieldPredicates.set(conditions, thisConditionList);
                    } else if (conditionFName.endsWith("Addresses")) {
                        EmailAddressCollection thisEmailAddresses = new EmailAddressCollection();
                        List<String> dbFromEmailList = (List<String>) conditionF.get(ewsConditionsEntity);
                        for (String from : dbFromEmailList) {
                            thisEmailAddresses.add(from);
                        }
                        declaredFieldPredicates.set(conditions, thisEmailAddresses);
                    } else {
                        declaredFieldPredicates.set(conditions, conditionF.get(ewsConditionsEntity));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    //todo test
    public void reflectActions(EwsActionsEntity ewsActionsEntity, Rule rule) {
        try {
            Field[] declaredFields = ewsActionsEntity.getClass().getDeclaredFields();
            for (Field actionF : declaredFields) {
                actionF.setAccessible(true);
                if (actionF.get(ewsActionsEntity) != null) {
                    String actionFName = actionF.getName();
                    RuleActions actions = rule.getActions();
                    Field declaredFieldActions = actions.getClass().getDeclaredField(actionFName);
                    declaredFieldActions.setAccessible(true);
                    if (actionFName.endsWith("Categories")) {
                        StringList thisActionList = new StringList((Iterable<String>) actionF.get(ewsActionsEntity));
                        declaredFieldActions.set(actions, thisActionList);
                    } else if (actionFName.endsWith("Recipients")) {
                        EmailAddressCollection thisEmailAddresses = new EmailAddressCollection();
                        List<String> dbFromEmailList = (List<String>) actionF.get(ewsActionsEntity);
                        for (String from : dbFromEmailList) {
                            thisEmailAddresses.add(from);
                        }
                        declaredFieldActions.set(actions, thisEmailAddresses);
                    } else if (actionFName.endsWith("Folder")) {
                        FolderId thisFolderId = new FolderId(String.valueOf(actionF.get(ewsActionsEntity)));
                        declaredFieldActions.set(actions, thisFolderId);
                    } else {
                        declaredFieldActions.set(actions, actionF.get(ewsActionsEntity));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
