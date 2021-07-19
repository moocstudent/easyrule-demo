package com.example.easyruledemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.easyruledemo.entity.relation.EwsMailFolderRelation;
import com.example.easyruledemo.mapper.EwsMailFolderRelationMapper;
import com.example.easyruledemo.service.IMailFolderRelationService;
import com.example.easyruledemo.util.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-09 10:13
 */
@Service
public class MailFolderRelationServiceImpl extends ServiceImpl<EwsMailFolderRelationMapper, EwsMailFolderRelation>
        implements IMailFolderRelationService {
    @Override
    public Boolean saveOrUpdateRelation(EwsMailFolderRelation relation) {
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
//
//    @Override
//    public List<EwsMailFolderRelation> listByRuleId(Long ruleId) {
//        return baseMapper.listFolderRelationByRuleId(ruleId);
//    }
//
//    @Override
//    public List<EwsMailFolderRelation> listByRuleId(String ruleId) {
//        return baseMapper.listFolderRelationByRuleId(ruleId);
//    }

    @Override
    public EwsMailFolderRelation findOneByCondition(Long ruleId, Long ewsFolderId, Long mailId) {
        LambdaQueryWrapper<EwsMailFolderRelation> queryWrapper
                =new LambdaQueryWrapper<EwsMailFolderRelation>()
                .eq(EwsMailFolderRelation::getEwsFolderId,ewsFolderId)
                .eq(EwsMailFolderRelation::getMailId,mailId)
                .eq(EwsMailFolderRelation::getDeleteFlag,0);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public EwsMailFolderRelation findOneByConditionCode(Long ruleId, String folderCode, Long mailId) {
        return baseMapper.findOneByConditions(ruleId,folderCode,mailId);
    }

    @Override
    public EwsMailFolderRelation findOneByConditionCode(String ruleId, String folderCode, String mailId) {
        return baseMapper.findOneByConditions(ruleId,folderCode,mailId);
    }

    @Override
    public List<EwsMailFolderRelation> listSelective(EwsMailFolderRelation relation) {
        return new LambdaQueryChainWrapper<EwsMailFolderRelation>(baseMapper)
                .eq(EwsMailFolderRelation::getDeleteFlag, 0)
                .eq(!StringUtils.isEmpty(relation.getRelationId()), EwsMailFolderRelation::getEwsFolderId, relation.getRelationId())
                .eq(!StringUtils.isEmpty(relation.getMailId()), EwsMailFolderRelation::getMailId, relation.getMailId())
                .orderByDesc(EwsMailFolderRelation::getRelationId)
                .list();
    }

    @Override
    public EwsMailFolderRelation findOne(Long relationId) {
        return baseMapper.selectById(relationId);
    }

    @Override
    public EwsMailFolderRelation findOne(String relationId) {
        return baseMapper.selectById(relationId);
    }

    @Override
    public List<EwsMailFolderRelation> findByMailId(Long mailId) {
        return baseMapper.findByConditions(mailId);
    }
}
