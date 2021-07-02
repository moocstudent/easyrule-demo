package com.example.easyruledemo.service.impl;

import com.example.easyruledemo.container.EwsContainer;
import com.example.easyruledemo.entity.EwsRuleEntity;
import com.example.easyruledemo.service.IEwsRuleService;
import com.example.easyruledemo.util.BeanUtil;
import microsoft.exchange.webservices.data.property.complex.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Frank
 * @Date: 2021-07-01 13:55
 */
@Service
public class EwsRuleServiceImpl implements IEwsRuleService {

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

    @Override
    public Integer ewsRuleFire(EwsRuleEntity ewsRuleEntity) {
        Rule rule = new Rule();
        rule.setDisplayName(ewsRuleEntity.getDisplayName());
        rule.setPriority(1);
        //是否启用
        rule.setIsEnabled(true);
        RulePredicates conditions = rule.getConditions();
        StringList containsSubjectStrings = conditions.getContainsSubjectStrings();
        //testok
        containsSubjectStrings.add("带附件");
        RuleActions actions = rule.getActions();
        try {
//            actions.setMoveToFolder(new FolderId(WellKnownFolderName.DeletedItems));
            actions.setMoveToFolder(FolderId.getFolderIdFromString(ewsRuleEntity.getItemMovedFolderIdStr()));
//            actions.set
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("set error");
            return -1;
        }
        CreateRuleOperation createOperation = new CreateRuleOperation(rule);
        List<RuleOperation> ruleList = new ArrayList<RuleOperation>();
        ruleList.add(createOperation);
        try {
            //执行规则更新
            EwsContainer.defaultExchangeService().updateInboxRules(ruleList, true);
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
            ruleCollection = EwsContainer.defaultExchangeService().getInboxRules(emailAddr);
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
            boolean add = ewsRuleEntityList.add(BeanUtil.copyInstance(rule,EwsRuleEntity.builder().build()));
            System.out.println("add one?"+add);
        }
        return ewsRuleEntityList;
    }

    @Override
    public Integer deleteRuleByEmAddr(String emailAddr) {
        RuleCollection ruleCollection = null;
        try {
            ruleCollection = EwsContainer.defaultExchangeService().getInboxRules(emailAddr);
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
            EwsContainer.defaultExchangeService().updateInboxRules(deleterules, true);
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
            ruleCollection = EwsContainer.defaultExchangeService().getInboxRules(emailAddr);
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
            EwsContainer.defaultExchangeService().updateInboxRules(disabledRules, true);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return disabledRules.size();
    }
}
