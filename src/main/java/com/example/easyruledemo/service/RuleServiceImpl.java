package com.example.easyruledemo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.easyruledemo.entity.EasyRuleEntity;
import com.example.easyruledemo.mapper.EasyRuleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Frank
 * @Date: 2021-06-25 9:36
 */
@Service
public class RuleServiceImpl extends ServiceImpl<EasyRuleMapper, EasyRuleEntity> implements IRuleService{
    @Override
    public Integer execRuleByCode(int ruleCode) {
        System.out.println("trigger rule :"+ ruleCode);
        return null;
    }

    @Override
    public List<?> getAll() {
        return super.list();
    }

    @Override
    public List<EasyRuleEntity> getAllUsed() {
        List<EasyRuleEntity> easyRuleEntities = baseMapper.selectList(new LambdaQueryWrapper<EasyRuleEntity>()
                .eq(EasyRuleEntity::getDeleteFlag, 0));
        return easyRuleEntities;
    }

    @Override
    public Integer fireRuleByType(String ruleType,String action) {
        System.out.println("fire rule with ruleType:"+ruleType+",download or download and copy file from:"+action);
        return null;
    }
}
