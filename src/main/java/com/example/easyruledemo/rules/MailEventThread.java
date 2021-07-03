package com.example.easyruledemo.rules;

import com.example.easyruledemo.container.EwsContainer;
import com.example.easyruledemo.container.SubscriptionContainer;
import com.example.easyruledemo.entity.MailConfigEntity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.enumeration.notification.EventType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.notification.GetEventsResults;
import microsoft.exchange.webservices.data.notification.ItemEvent;
import microsoft.exchange.webservices.data.notification.PullSubscription;
import microsoft.exchange.webservices.data.property.complex.AttachmentCollection;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author: zhangQi
 * @Date: 2021-07-03 16:35
 */
@Slf4j
@Data
public class MailEventThread implements Runnable{

    private MailConfigEntity mailConfig;

    @Override
    public void run() {
        try {
            //todo 需要改为每天初始化subscriptionContainer后等待几分钟再拉取
            log.info("start poll email event : {}"+ LocalDateTime.now());
            PullSubscription subscription = SubscriptionContainer.getSubscriptionToday(mailConfig);
            GetEventsResults events = subscription.getEvents();
            System.out.println("----");
            // Loop through all item-related events.
            for (ItemEvent itemEvent : events.getItemEvents()) {
                if (itemEvent.getEventType() == EventType.NewMail) {
                    EmailMessage message = EmailMessage.bind(EwsContainer.defaultExchangeService(), itemEvent.getItemId());
                    if (message.getHasAttachments()) {
                        AttachmentCollection attachments = message.getAttachments();
                        System.out.println("attachments:" + attachments);
                    }
                } else if (itemEvent.getEventType() == EventType.Created) {
                    Item item = Item.bind(EwsContainer.defaultExchangeService(), itemEvent.getItemId());
                } else if (itemEvent.getEventType() == EventType.Deleted) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
