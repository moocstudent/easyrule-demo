package com.example.easyruledemo;

import com.example.easyruledemo.container.EwsContainer;
import com.example.easyruledemo.container.SubscriptionContainer;
import com.example.easyruledemo.entity.EwsFoldersEntity;
import com.example.easyruledemo.entity.EwsMailEntity;
import microsoft.exchange.webservices.data.core.enumeration.notification.EventType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.notification.GetEventsResults;
import microsoft.exchange.webservices.data.notification.ItemEvent;
import microsoft.exchange.webservices.data.notification.PullSubscription;
import microsoft.exchange.webservices.data.property.complex.AttachmentCollection;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-02 21:22
 */
class Notify2Test extends BaseTest {


    //testok
    @Test
    public void testPullNotify() {
        // Subscribe to pull notifications in the Inbox folder, and get notified when a new mail is received, when an item or folder is created, or when an item or folder is deleted.
        List<FolderId> folders = new ArrayList<FolderId>();
        FolderId folderId = null;
        try {
            folderId = new FolderId("AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAWrq7QAAAA==");
        } catch (Exception e) {
            e.printStackTrace();
        }
//            FolderId foldIdEntity = FolderId.getFolderIdFromString(FolderNameEnum.ATTACH_UN.getCode());
//            folder.add(foldIdEntity);
        folders.add(folderId);

        try {

            PullSubscription subscription = SubscriptionContainer.getEmailNotifySubscription(folders, 1440);
//            PullSubscription subscription = EwsContainer.defaultExchangeService().subscribeToPullNotifications(folder, 1440
//                    /* timeOut: the subscription will end if the server is not polled within 5 minutes. */, null /* watermark: null to start a new subscription. */, EventType.NewMail, EventType.Created, EventType.Deleted);
//            Thread.sleep(30000);
            System.out.println("c1:" + subscription);
            // Wait a couple minutes, then poll the server for new events.
            String subscriptionId = subscription.getId();
            System.out.println("subcriptionId:" + subscriptionId);

            //获取
            PullSubscription containerByKey = SubscriptionContainer.getSubscriptionByKey();
            System.out.println("c:" + containerByKey);

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
    @Test
    public void notifyTest(){
        SubscriptionContainer
                .getEmailNotifySubscription(1, EwsMailEntity.builder()
                .email("frankimplements@outlook.com").password("zhangqi1112")
                        .mailFolders(EwsFoldersEntity.builder().folderIds(
                                Arrays.asList("AQMkADAwATM0MDAAMS0zNjVjLTZmODctMDACLTAwCgAuAAADRJzEnDGzDkiFHa7PpA6vRgEAL3jSJ1Y43EaEjeMalrFpowAAAiQTAAAA")
                        ).build())
                        .build());
    }

    @Test
    public void notifyTest1(){
        SubscriptionContainer
                .getEmailNotifySubscription(1, EwsMailEntity.builder()
                        .email("implementsteam@outlook.com").password("zhangqi1112")
                        .mailFolders(EwsFoldersEntity.builder().folderIds(
                                Arrays.asList("AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAWrq7QAAAA==")
                        ).build())
                        .build());
    }
}