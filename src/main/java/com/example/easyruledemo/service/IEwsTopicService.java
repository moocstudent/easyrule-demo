package com.example.easyruledemo.service;

import com.example.easyruledemo.entity.EwsTopicEntity;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-05 17:25
 */
public interface IEwsTopicService {

    //添加一个
    String addOne(EwsTopicEntity ewsTopic);

    //保存或者更新topic
    Boolean saveOrUpdateTopic(EwsTopicEntity ewsTopic);

    Integer updateOne(EwsTopicEntity ewsTopic);

    Integer delOne(String topicId);

    List<EwsTopicEntity> getList(EwsTopicEntity ewsTopic);

    //动态获取
    List<EwsTopicEntity> listSelective(EwsTopicEntity ewsTopic);

    //根据mail主键获取收件主题
    EwsTopicEntity getTopicByMailId(String mailId);
    EwsTopicEntity getTopicByMailId(Long mailId);

    //根据topicId查一个
    EwsTopicEntity findOne(String topicId);

    //713
    //设定到未激活状态
    Integer inactive(String topicId);
}
