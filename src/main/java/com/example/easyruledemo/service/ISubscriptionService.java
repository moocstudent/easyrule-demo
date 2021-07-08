package com.example.easyruledemo.service;

import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.entity.EwsSubscriptionEntity;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-06 19:23
 * 邮箱监听订阅服务service
 */
public interface ISubscriptionService {

    //取消订阅根据给定订阅id
    Integer unSubscriptionById(String subscriptionId, EwsMailEntity ewsMail);

    //取消订阅根据缓存
    Integer unSubscriptionByMailCache(EwsMailEntity ewsMail);

    //curd
    Boolean saveOrUpdateSubcription(EwsSubscriptionEntity ewsSubscription);

    //根据mapKey获取订阅id 或者无返回null
    String getSubscriptionIdByKey(String key);

    //动态获取订阅主体list
    List<EwsSubscriptionEntity> listSelective(EwsSubscriptionEntity ewsSubscription);

    //更新订阅主体by key
    Integer updateByKey(EwsSubscriptionEntity ewsSubscriptionEntity);

}
