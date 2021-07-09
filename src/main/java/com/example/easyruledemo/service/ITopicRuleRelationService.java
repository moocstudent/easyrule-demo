package com.example.easyruledemo.service;

import com.example.easyruledemo.entity.relation.EwsRuleFolderRelation;
import com.example.easyruledemo.entity.relation.EwsTopicRuleRelation;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-09 10:13
 */
public interface ITopicRuleRelationService {

    Boolean saveOrUpdateRelation(EwsTopicRuleRelation relation);

    Integer delRelation(Long relationId);
    Integer delRelation(String relationId);

    //动态获取规则文件夹关联
    List<EwsTopicRuleRelation> listSelective(EwsTopicRuleRelation relation);

    EwsTopicRuleRelation findOne(Long relationId);
    EwsTopicRuleRelation findOne(String relationId);
}
