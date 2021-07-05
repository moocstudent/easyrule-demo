package com.example.easyruledemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.entity.EwsTopicEntity;
import com.example.easyruledemo.mapper.EwsTopicMapper;
import com.example.easyruledemo.service.IEwsEmailService;
import com.example.easyruledemo.service.IEwsTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Integer updateOne(EwsTopicEntity ewsTopic) {
        return null;
    }

    @Override
    public Integer delOne(String ews) {
        return null;
    }

    @Override
    public List<EwsTopicEntity> getList(EwsTopicEntity ewsTopic) {
        return null;
    }

    @Transactional
    @Override
    public EwsTopicEntity getTopicByMailId(String mailId) {
        EwsMailEntity mail = ewsEmailService.findOne(mailId);
        String topicId = null;
        if(mail!=null){
            topicId = mail.getTopicId();
        }
        return baseMapper.selectById(topicId);
    }

}
