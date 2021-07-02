package com.example.easyruledemo.serv;

//import com.example.easyruledemo.entity.EasyRuleEntity;

import java.util.List;
import java.util.Map;

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

//    List<EasyRuleEntity> getAllUsed();

    Integer fireRuleByType(final String ruleType,final String action);

    /**
     * 规则条件实体判定
     */
//    boolean rule(final RuleConditionEntity entity, final Map factsMap);

}
