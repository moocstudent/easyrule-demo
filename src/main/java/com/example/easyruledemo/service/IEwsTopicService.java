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

    Integer updateOne(EwsTopicEntity ewsTopic);

    Integer delOne(String ews);

    List<EwsTopicEntity> getList(EwsTopicEntity ewsTopic);

    //根据mail主键获取收件主题
    EwsTopicEntity getTopicByMailId(String mailId);


}
