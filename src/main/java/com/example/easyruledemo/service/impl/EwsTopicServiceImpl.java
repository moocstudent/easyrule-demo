package com.example.easyruledemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.entity.EwsTopicEntity;
import com.example.easyruledemo.mapper.EwsTopicMapper;
import com.example.easyruledemo.service.IEwsEmailService;
import com.example.easyruledemo.service.IEwsTopicService;
import com.example.easyruledemo.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-05 17:25
 */
@Service
public class EwsTopicServiceImpl extends ServiceImpl<EwsTopicMapper, EwsTopicEntity>
        implements IEwsTopicService {

    @Autowired
    private IEwsEmailService ewsEmailService;

    @Override
    public String addOne(EwsTopicEntity ewsTopic) {
        return null;
    }

    @Override
    public Boolean saveOrUpdateTopic(EwsTopicEntity ewsTopic) {
        return super.saveOrUpdate(BeanUtil.getTrimClass(ewsTopic).get());
    }

    @Override
    public Integer updateOne(EwsTopicEntity ewsTopic) {
        return null;
    }

    @Override
    public Integer delOne(String topicId) {
        return baseMapper.deleteById(topicId);
    }

    @Override
    public Integer delOne(Long topicId) {
        return baseMapper.deleteById(topicId);
    }

    @Override
    public List<EwsTopicEntity> getList(EwsTopicEntity ewsTopic) {
        return null;
    }

    @Override
    public List<EwsTopicEntity> listSelective(EwsTopicEntity ewsTopic) {
        return new LambdaQueryChainWrapper<EwsTopicEntity>(baseMapper)
                .eq(EwsTopicEntity::getDeleteFlag,0)
                .like(!StringUtils.isEmpty(ewsTopic.getTopicName()),EwsTopicEntity::getTopicName,ewsTopic.getTopicName())
                .like(!StringUtils.isEmpty(ewsTopic.getTopicConfig()),EwsTopicEntity::getTopicConfig,ewsTopic.getTopicConfig())
                .like(!StringUtils.isEmpty(ewsTopic.getTopicDesc()),EwsTopicEntity::getTopicDesc,ewsTopic.getTopicDesc())
                .orderByDesc(EwsTopicEntity::getTopicId)
                .list();
    }

//    @Transactional
//    @Override
//    public EwsTopicEntity getTopicByMailId(String mailId) {
//        EwsMailEntity mail = ewsEmailService.findOne(mailId);
//        String topicId = null;
//        if(mail!=null){
//            topicId = mail.getTopicId();
//        }
//        return baseMapper.selectById(topicId);
//    }

    @Override
    public EwsTopicEntity getTopicByMailId(Long mailId) {
        EwsMailEntity mail = ewsEmailService.findOne(mailId);
        Long topicId = null;
        if(mail!=null){
            topicId = mail.getTopicId();
        }
        return baseMapper.selectById(topicId);
    }

    @Override
    public EwsTopicEntity findOne(String topicId) {
        return baseMapper.selectById(topicId);
    }

    @Override
    public EwsTopicEntity findOne(Long topicId) {
        return baseMapper.selectById(topicId);
    }

}
