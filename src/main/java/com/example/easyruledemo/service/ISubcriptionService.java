package com.example.easyruledemo.service;

import com.example.easyruledemo.entity.EwsMailEntity;

/**
 * @Author: zhangQi
 * @Date: 2021-07-06 19:23
 * 邮箱监听订阅服务service
 */
public interface ISubcriptionService {

    //取消订阅根据给定订阅id
    Integer unSubscriptionById(String subscriptionId, EwsMailEntity ewsMail);

    //取消订阅根据缓存
    Integer unSubscriptionByMailCache(EwsMailEntity ewsMail);
}
