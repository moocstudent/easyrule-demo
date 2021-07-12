package com.example.easyruledemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.easyruledemo.entity.relation.EwsRuleFolderRelation;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-09 10:10
 */
@Repository
public interface EwsRuleFolderRelationMapper extends BaseMapper<EwsRuleFolderRelation> {
    //遍历folderRelation根据ruleId
    List<EwsRuleFolderRelation> listFolderRelationByRuleId(Long ruleId);
    List<EwsRuleFolderRelation> listFolderRelationByRuleId(String ruleId);
    //关联到ews_folder文件表,查询规则文件关联关系
    EwsRuleFolderRelation findOneByConditions(Long ruleId, String folderCode, Long mailId);
    EwsRuleFolderRelation findOneByConditions(String ruleId, String folderCode, String mailId);
}
