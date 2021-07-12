package com.example.easyruledemo.service;

import com.example.easyruledemo.entity.relation.EwsRuleFolderRelation;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-09 10:12
 */
public interface IRuleFolderRelationService {

    Boolean saveOrUpdateRelation(EwsRuleFolderRelation relation);

    Integer delRelation(Long relationId);
    Integer delRelation(String relationId);

    List<EwsRuleFolderRelation> listByRuleId(Long ruleId);

    List<EwsRuleFolderRelation> listByRuleId(String ruleId);

    //根据条件查询关联,获取其中folderId为主要目的
    EwsRuleFolderRelation findOneByCondition(Long ruleId,Long ewsFolderId,Long mailId);
    //同上 不过用folderCode
    EwsRuleFolderRelation findOneByConditionCode(Long ruleId,String folderCode,Long mailId);
    EwsRuleFolderRelation findOneByConditionCode(String ruleId,String folderCode,String mailId);

    //动态获取规则文件夹关联
    List<EwsRuleFolderRelation> listSelective(EwsRuleFolderRelation relation);

    EwsRuleFolderRelation findOne(Long relationId);
    EwsRuleFolderRelation findOne(String relationId);
}
