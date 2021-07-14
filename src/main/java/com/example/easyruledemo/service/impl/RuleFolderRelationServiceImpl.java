package com.example.easyruledemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.entity.relation.EwsRuleFolderRelation;
import com.example.easyruledemo.mapper.EwsRuleFolderRelationMapper;
import com.example.easyruledemo.service.IRuleFolderRelationService;
import com.example.easyruledemo.util.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-09 10:13
 */
@Service
public class RuleFolderRelationServiceImpl extends ServiceImpl<EwsRuleFolderRelationMapper,EwsRuleFolderRelation>
        implements IRuleFolderRelationService {
    @Override
    public Boolean saveOrUpdateRelation(EwsRuleFolderRelation relation) {
        return super.saveOrUpdate(BeanUtil.getTrimClass(relation).get());
    }

    @Override
    public Integer delRelation(Long relationId) {
        return baseMapper.deleteById(relationId);
    }

    @Override
    public Integer delRelation(String relationId) {
        return baseMapper.deleteById(relationId);
    }

    @Override
    public List<EwsRuleFolderRelation> listByRuleId(Long ruleId) {
        return baseMapper.listFolderRelationByRuleId(ruleId);
    }

    @Override
    public List<EwsRuleFolderRelation> listByRuleId(String ruleId) {
        return baseMapper.listFolderRelationByRuleId(ruleId);
    }

    @Override
    public EwsRuleFolderRelation findOneByCondition(Long ruleId, Long ewsFolderId,Long mailId) {
        LambdaQueryWrapper<EwsRuleFolderRelation> queryWrapper
                =new LambdaQueryWrapper<EwsRuleFolderRelation>()
                .eq(EwsRuleFolderRelation::getRuleId,ruleId)
                .eq(EwsRuleFolderRelation::getEwsFolderId,ewsFolderId)
                .eq(EwsRuleFolderRelation::getMailId,mailId)
                .eq(EwsRuleFolderRelation::getDeleteFlag,0);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public EwsRuleFolderRelation findOneByConditionCode(Long ruleId, String folderCode, Long mailId) {
        return baseMapper.findOneByConditions(ruleId,folderCode,mailId);
    }

    @Override
    public EwsRuleFolderRelation findOneByConditionCode(String ruleId, String folderCode, String mailId) {
        return baseMapper.findOneByConditions(ruleId,folderCode,mailId);
    }

    @Override
    public List<EwsRuleFolderRelation> listSelective(EwsRuleFolderRelation relation) {
        return new LambdaQueryChainWrapper<EwsRuleFolderRelation>(baseMapper)
                .eq(EwsRuleFolderRelation::getDeleteFlag, 0)
                .eq(!StringUtils.isEmpty(relation.getRelationId()), EwsRuleFolderRelation::getEwsFolderId, relation.getRelationId())
                .eq(!StringUtils.isEmpty(relation.getMailId()), EwsRuleFolderRelation::getMailId, relation.getMailId())
                .orderByDesc(EwsRuleFolderRelation::getRelationId)
                .list();
    }

    @Override
    public EwsRuleFolderRelation findOne(Long relationId) {
        return baseMapper.selectById(relationId);
    }

    @Override
    public EwsRuleFolderRelation findOne(String relationId) {
        return baseMapper.selectById(relationId);
    }
}
