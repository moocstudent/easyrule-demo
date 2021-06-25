package com.example.easyruledemo.service;

import com.example.easyruledemo.entity.EasyRuleEntity;

import java.util.List;

/**
 * @Author: Frank
 * @Date: 2021-06-25 9:35
 */
public interface IRuleService {

    /**
     * 根据ruleCode执行rule
     * @param ruleCode
     * @return
     */
    Integer execRuleByCode(int ruleCode);

    List<?> getAll();

    List<EasyRuleEntity> getAllUsed();

    Integer fireRuleByType(String ruleType,String action);

}
