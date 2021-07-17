package com.example.easyruledemo.rules;

import com.example.easyruledemo.container.EwsExContainer;
import com.example.easyruledemo.container.SubscriptionContainer;
import com.example.easyruledemo.entity.EwsMailEntity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.enumeration.notification.EventType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.notification.GetEventsResults;
import microsoft.exchange.webservices.data.notification.ItemEvent;
import microsoft.exchange.webservices.data.notification.PullSubscription;
import microsoft.exchange.webservices.data.notification.SubscriptionBase;

import java.time.LocalDateTime;

/**
 * @Author: zhangQi
 * @Date: 2021-07-05 11:14
 * mailEvents多线程处理
 */
@Slf4j
@Data
public class MailEventsThread extends Thread {

    private ThreadLocal<PullSubscription> pullSubscriptionTL = new ThreadLocal<>();
    private ThreadLocal<EwsMailEntity> mailConfigTL = new ThreadLocal<>();

//    @Deprecated
//    public MailEventsThread(EwsMailEntity mailConfig){
//        this.mailConfig = mailConfig;
//    }

    public MailEventsThread(EwsMailEntity mailConfig, PullSubscription subscription){
        mailConfigTL.set(mailConfig);
        pullSubscriptionTL.set(subscription);
    }

    @Override
    public void start() {
        EwsMailEntity mailConfig = mailConfigTL.get();
        PullSubscription subscription = pullSubscriptionTL.get();
        try {
            log.info("mailConfig in actions:{}",mailConfig);
            //todo 需要改为每天初始化subscriptionContainer后等待几分钟再拉取
            log.info("start poll email event : {}" + LocalDateTime.now());
//            log.info("subscriptionId in events:{}",pullSubscription.getId());
            GetEventsResults events = subscription.getEvents();
            System.out.println("----");
            // Loop through all item-related events.
//            EventType.Status;
            for (ItemEvent itemEvent : events.getItemEvents()) {
                if (itemEvent.getEventType() == EventType.NewMail) {
                    log.info("新的邮件已到达 itemId:{}" + itemEvent.getItemId());
                    log.info("mailConfig:{}",mailConfig);
                    EmailMessage message = EmailMessage.bind(EwsExContainer.getExchangeService(
                            mailConfig.getEmail(), mailConfig.getPassword()),
                            itemEvent.getItemId()
                    );
                    MailActionsThread actionsThread = new MailActionsThread(message, mailConfig);
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
                }

//                } else if (itemEvent.getEventType() == EventType.Created) {
//                    Item item = Item.bind(EwsExContainer.getExchangeService(
//                            mailConfig.getEmail(), mailConfig.getPassword()),
//                            itemEvent.getItemId());
//                } else if (itemEvent.getEventType() == EventType.Deleted) {
//                    break;
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    @Override
//    public void start() {
//        try {
//            log.info("mailConfig in actions:{}",mailConfig);
//            //todo 需要改为每天初始化subscriptionContainer后等待几分钟再拉取
//            log.info("start poll email event : {}" + LocalDateTime.now());
//            PullSubscription subscription = SubscriptionContainer.getSubscriptionToday(mailConfig);
//            log.info("subscriptionId in events:{}",subscription.getId());
//            GetEventsResults events = subscription.getEvents();
//            System.out.println("----");
//            // Loop through all item-related events.
//            for (ItemEvent itemEvent : events.getItemEvents()) {
//                if (itemEvent.getEventType() == EventType.NewMail) {
//                    log.info("新的邮件已到达 itemId:{}" + itemEvent.getItemId());
//                    EmailMessage message = EmailMessage.bind(EwsExContainer.getExchangeService(
//                            mailConfig.getEmail(), mailConfig.getPassword()),
//                            itemEvent.getItemId()
//                    );
//                    MailActionsThread actionsThread = new MailActionsThread(message,mailConfig);
//                    actionsThread.start();
////                    if (message.getHasAttachments()) {
////                        AttachmentCollection attachments = message.getAttachments();
////                        System.out.println("attachments:" + attachments);
////                        log.info("进行附件下载");
////
////                        attachments.save();
////
////                        log.info("下载完成后将邮件移入" + FolderNameEnum.ATTACH_ALREADY.getUsename());
////                        //下载完成后将文件移动入已下载附件文件夹
////                        message.move(new FolderId(
////                                mailConfig.getMailFoldersMap()
////                                        .get(FolderNameEnum.ATTACH_ALREADY.getCode()).getFolderId())
////                        );
////                    }
//
//                } else if (itemEvent.getEventType() == EventType.Created) {
//                    Item item = Item.bind(EwsExContainer.getExchangeService(
//                            mailConfig.getEmail(), mailConfig.getPassword()),
//                            itemEvent.getItemId());
//                } else if (itemEvent.getEventType() == EventType.Deleted) {
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
