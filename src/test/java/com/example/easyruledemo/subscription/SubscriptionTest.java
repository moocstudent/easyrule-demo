package com.example.easyruledemo.subscription;

import com.example.easyruledemo.BaseTest;
import com.example.easyruledemo.container.SubscriptionContainer;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.service.ISubscriptionService;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.notification.PullSubscription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: zhangQi
 * @Date: 2021-07-07 10:58
 */
@Slf4j
public class SubscriptionTest extends BaseTest {

    @Autowired
    private ISubscriptionService subscriptionService;

    @Test
    public void testSubscription(){
        EwsMailEntity mailEntity = EwsMailEntity
                .builder()
                .email("implementsteam@outlook.com")
                .password("zhangqi1112")
                .build();
        PullSubscription customSubscription = SubscriptionContainer.getSubscriptionCustom(
                mailEntity, 1
        );
        String customSubscriptionId = customSubscription.getId();
        log.info("customSubscriptionId:{}",customSubscriptionId);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Integer unsubscription = subscriptionService.unSubscriptionById(customSubscriptionId, mailEntity);
        log.info("取消订阅:{}",unsubscription>0);

    }




}
