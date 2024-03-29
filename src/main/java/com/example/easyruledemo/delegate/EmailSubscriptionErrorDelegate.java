package com.example.easyruledemo.delegate;

import microsoft.exchange.webservices.data.notification.StreamingSubscriptionConnection;
import microsoft.exchange.webservices.data.notification.SubscriptionErrorEventArgs;
import org.springframework.stereotype.Component;

/**
 * @Author: zhangQi
 * @Date: 2021-07-02 14:35
 */
@Component
public class EmailSubscriptionErrorDelegate implements StreamingSubscriptionConnection.ISubscriptionErrorDelegate {
    //失去stream链接
    @Override
    public void subscriptionErrorDelegate(Object sender, SubscriptionErrorEventArgs args) {
        System.out.println("sender:"+sender+",disconnecting..~~~~");
    }
}
