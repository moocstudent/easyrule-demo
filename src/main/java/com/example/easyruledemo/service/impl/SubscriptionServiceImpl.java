package com.example.easyruledemo.service.impl;

import com.example.easyruledemo.container.EwsExContainer;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.service.ISubscriptionService;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.ExchangeService;
import org.springframework.stereotype.Service;

/**
 * @Author: zhangQi
 * @Date: 2021-07-06 19:26
 */
@Service
@Slf4j
public class SubscriptionServiceImpl implements ISubscriptionService {
    @Override
    public Integer unSubscriptionById(String subscriptionId, EwsMailEntity ewsMail) {
        ExchangeService exchangeService = EwsExContainer.getExchangeService(ewsMail.getEmail(), ewsMail.getPassword());
        try {
            exchangeService.unsubscribe(subscriptionId);
            log.info("取消订阅,订阅id每日更新一次,如果没有进行主动替换,则需要进行手动替换.");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("取消订阅异常");
            return -1;
        }
        return 1;
    }

    @Override
    public Integer unSubscriptionByMailCache(EwsMailEntity ewsMail) {
        ExchangeService exchangeService = EwsExContainer.getExchangeService(ewsMail.getEmail(), ewsMail.getPassword());
        String key = ewsMail.getEmail()+ewsMail.getPassword();
        String subscriptionId = "从缓存中根据key拿取订阅id";
        try {
            exchangeService.unsubscribe(subscriptionId);
            log.info("取消订阅,相应清除redis中订阅id.订阅id每日更新一次,如果没有进行主动替换,则需要进行手动替换.");
            //todo 清除id从缓存中
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通过缓存取消订阅异常");
            return -1;
        }
        return 1;
    }


}
