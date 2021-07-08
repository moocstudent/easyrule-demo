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

    //在创建订阅后进行新增一个订阅信息
    Boolean saveOrUpdateSubcription(EwsSubscriptionEntity ewsSubscription);

    //根据mapKey获取订阅id 或者无返回null
    String getSubscriptionIdByKey(String key);

    //动态获取订阅主体list
    List<EwsSubscriptionEntity> listSelective(EwsSubscriptionEntity ewsSubscription);

    /**
     * 在再次初始化时，根据subscriptionId取消原有订阅，进行新的订阅，同时updateByKey
     * 如果同天的话
     * @param ewsSubscriptionEntity
     * @return
     */
    Integer updateByKey(EwsSubscriptionEntity ewsSubscriptionEntity);

}
