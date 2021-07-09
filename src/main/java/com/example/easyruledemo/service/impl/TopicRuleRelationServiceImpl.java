package com.example.easyruledemo.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.easyruledemo.entity.relation.EwsRuleFolderRelation;
import com.example.easyruledemo.entity.relation.EwsTopicRuleRelation;
import com.example.easyruledemo.mapper.EwsTopicRuleRelationMapper;
import com.example.easyruledemo.service.ITopicRuleRelationService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-09 10:13
 */
@Service
public class TopicRuleRelationServiceImpl extends ServiceImpl<EwsTopicRuleRelationMapper,EwsTopicRuleRelation>
        implements ITopicRuleRelationService {
    @Override
    public Boolean saveOrUpdateRelation(EwsTopicRuleRelation relation) {
        return super.saveOrUpdate(relation);
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
    public List<EwsTopicRuleRelation> listSelective(EwsTopicRuleRelation relation) {
        return new LambdaQueryChainWrapper<EwsTopicRuleRelation>(baseMapper)
                .eq(EwsTopicRuleRelation::getDeleteFlag, 0)
                .eq(!StringUtils.isEmpty(relation.getTopicId()), EwsTopicRuleRelation::getTopicId, relation.getTopicId())
                .eq(!StringUtils.isEmpty(relation.getRuleId()), EwsTopicRuleRelation::getRuleId, relation.getRuleId())
                .orderByDesc(EwsTopicRuleRelation::getRelationId)
                .list();
    }

    @Override
    public EwsTopicRuleRelation findOne(Long relationId) {
        return baseMapper.selectById(relationId);
    }

    @Override
    public EwsTopicRuleRelation findOne(String relationId) {
        return baseMapper.selectById(relationId);
    }
}
