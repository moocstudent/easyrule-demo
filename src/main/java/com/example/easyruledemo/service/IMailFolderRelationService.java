package com.example.easyruledemo.service;

import com.example.easyruledemo.entity.relation.EwsMailFolderRelation;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-09 10:12
 */
public interface IMailFolderRelationService {

    Boolean saveOrUpdateRelation(EwsMailFolderRelation relation);

    Integer delRelation(Long relationId);
    Integer delRelation(String relationId);

//    List<EwsMailFolderRelation> listByRuleId(Long ruleId);
//
//    List<EwsMailFolderRelation> listByRuleId(String ruleId);

    //根据条件查询关联,获取其中folderId为主要目的
    EwsMailFolderRelation findOneByCondition(Long ruleId, Long ewsFolderId, Long mailId);
    //同上 不过用folderCode
    EwsMailFolderRelation findOneByConditionCode(Long ruleId, String folderCode, Long mailId);
    EwsMailFolderRelation findOneByConditionCode(String ruleId, String folderCode, String mailId);

    //动态获取规则文件夹关联
    List<EwsMailFolderRelation> listSelective(EwsMailFolderRelation relation);

    EwsMailFolderRelation findOne(Long relationId);
    EwsMailFolderRelation findOne(String relationId);

    List<EwsMailFolderRelation> findByMailId(Long mailId);
}
