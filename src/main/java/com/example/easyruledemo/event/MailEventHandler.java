package com.example.easyruledemo.event;

import com.example.easyruledemo.container.EwsExContainer;
import com.example.easyruledemo.container.SubscriptionContainer;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.rules.MailActionsThread;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.enumeration.notification.EventType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.notification.GetEventsResults;
import microsoft.exchange.webservices.data.notification.ItemEvent;
import microsoft.exchange.webservices.data.notification.PullSubscription;

import java.time.LocalDateTime;

/**
 * @Author: zhangQi
 * @Date: 2021-07-14 16:48
 */
@Slf4j
public class MailEventHandler {


    public static void event(EwsMailEntity mailConfig){
        try {
            //todo 需要改为每天初始化subscriptionContainer后等待几分钟再拉取
            log.info("start poll email event : {}" + LocalDateTime.now());
            PullSubscription subscription = SubscriptionContainer.getSubscriptionToday(mailConfig);
            log.info("subscriptionId in events:{}",subscription.getId());
            GetEventsResults events = subscription.getEvents();
            System.out.println("----");
            // Loop through all item-related events.
            for (ItemEvent itemEvent : events.getItemEvents()) {
                if (itemEvent.getEventType() == EventType.NewMail) {
                    log.info("新的邮件已到达 itemId:{}" + itemEvent.getItemId());
                    EmailMessage message = EmailMessage.bind(EwsExContainer.getExchangeService(
                            mailConfig.getEmail(), mailConfig.getPassword()),
                            itemEvent.getItemId()
                    );
                    MailActionsThread actionsThread = new MailActionsThread(message,mailConfig);
                    actionsThread.start();
//                    if (message.getHasAttachments()) {
//                        AttachmentCollection attachments = message.getAttachments();
//                        System.out.println("attachments:" + attachments);
//                        log.info("进行附件下载");
//
//                        attachments.save();
//
//                        log.info("下载完成后将邮件移入" + FolderNameEnum.ATTACH_ALREADY.getUsename());
//                        //下载完成后将文件移动入已下载附件文件夹
//                        message.move(new FolderId(
//                                mailConfig.getMailFoldersMap()
//                                        .get(FolderNameEnum.ATTACH_ALREADY.getCode()).getFolderId())
//                        );
//                    }

                } else if (itemEvent.getEventType() == EventType.Created) {
                    Item item = Item.bind(EwsExContainer.getExchangeService(
                            mailConfig.getEmail(), mailConfig.getPassword()),
                            itemEvent.getItemId());
                } else if (itemEvent.getEventType() == EventType.Deleted) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
