package com.example.easyruledemo;

import com.example.easyruledemo.callback.EmailAttachCallBack;
import com.example.easyruledemo.container.EwsContainer;
import com.example.easyruledemo.container.SubscriptionContainer;
import com.example.easyruledemo.delegate.EmailNotifyDelegate;
import com.example.easyruledemo.delegate.EmailSubscriptionErrorDelegate;
import com.example.easyruledemo.service.IEmailNotifyService;
import com.sun.jndi.toolkit.url.Uri;
import microsoft.exchange.webservices.data.core.enumeration.notification.EventType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.credential.CredentialConstants;
import microsoft.exchange.webservices.data.misc.IAsyncResult;
import microsoft.exchange.webservices.data.notification.*;
import microsoft.exchange.webservices.data.property.complex.AttachmentCollection;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @Author: zhangQi
 * @Date: 2021-07-02 13:37
 */
class NotifyTest extends BaseTest {

    @Test
    public void testBeginStreamingNotify() {

        List<FolderId> folder = new ArrayList<FolderId>();
        try {
//            WellKnownFolderName sd = WellKnownFolderName.Inbox;
            FolderId folderId = new FolderId("AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAWrq7QAAAA==");
//            FolderId foldIdEntity = FolderId.getFolderIdFromString(FolderNameEnum.ATTACH_UN.getCode());
//            folder.add(foldIdEntity);
            folder.add(folderId);

            IAsyncResult asyncResult = EwsContainer.defaultExchangeService().beginSubscribeToStreamingNotifications(new EmailAttachCallBack(), null, folder, EventType.NewMail);
//            IAsyncResult subscription = EwsContainer.defaultExchangeService().beginSubscribeToPullNotifications(new EmailAttachCallBack(), null, folder, 5, null, EventType.NewMail, EventType.Created, EventType.Deleted);

            boolean completeSynchronously = asyncResult.getCompleteSynchronously();
            System.out.println("complete::::::" + completeSynchronously);

            Thread.sleep(40000);
            StreamingSubscription subscription = EwsContainer.defaultExchangeService().endSubscribeToStreamingNotifications(asyncResult);
            System.out.println(subscription.getId());
            System.out.println(subscription);

            System.out.println("complete::::::after" + subscription);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //testok
    @Test
    public void eventAllFolder() {
        // Subscribe to push notifications on the Inbox folder, and only listen to "new mail" events.

        GetEventsResults events = null;
        try {
            IAsyncResult asyncresult = EwsContainer.defaultExchangeService()
                    .beginSubscribeToPullNotificationsOnAllFolders(
                            null, null, 5, null, EventType.NewMail, EventType.Created, EventType.Deleted);
            Thread.sleep(40000);
            PullSubscription subscription = EwsContainer.defaultExchangeService().endSubscribeToPullNotifications(asyncresult);
            events = subscription.getEvents();
            Iterable<ItemEvent> itemEvents = events.getItemEvents();
            List<EmailMessage> collect = StreamSupport.stream(itemEvents.spliterator(), false)
                    .map(itemEvent -> {
                        ItemId itemId = itemEvent.getItemId();
                        // Bind to an existing message using its unique identifier.
                        try {
                            EmailMessage message = EmailMessage.bind(EwsContainer.defaultExchangeService(), itemId);
                            // Write the sender's name.
                            System.out.println(message.getSubject());
                            return message;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .map(eMsg -> {
                        System.out.println("进行邮件附件的下载" + eMsg);
                        return eMsg;
                    })
                    .collect(Collectors.toList());

            System.out.println("count new email:" + collect.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("events======" + events.getItemEvents());

    }

    @Autowired
    private EmailNotifyDelegate emailNotifyDelegate;
    @Autowired
    private EmailSubscriptionErrorDelegate emailSubscriptionErrorDelegate;

    @Test
    public void testConnStreamNotify() {
        LocalDateTime startTime = LocalDateTime.now();
        List<FolderId> folder = new ArrayList<FolderId>();
        FolderId folderId = null;
        try {
            folderId = new FolderId("AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAWrq7QAAAA==");
        } catch (Exception e) {
            e.printStackTrace();
        }
//            FolderId foldIdEntity = FolderId.getFolderIdFromString(FolderNameEnum.ATTACH_UN.getCode());
//            folder.add(foldIdEntity);
        folder.add(folderId);
        try {
            StreamingSubscription subscription = EwsContainer.defaultExchangeService().subscribeToStreamingNotifications(folder, EventType.NewMail);
            //StreamingSubscriptionConnection(service,lifetime[minutes])
            StreamingSubscriptionConnection conn = new StreamingSubscriptionConnection(EwsContainer.defaultExchangeService(), 2);
            conn.addSubscription(subscription);
            conn.addOnNotificationEvent(emailNotifyDelegate);
            conn.addOnDisconnect(emailSubscriptionErrorDelegate);
            conn.open();

//            Thread.sleep(30000);
            conn.close();
//            subscription.unsubscribe();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("end........");
        LocalDateTime endTime = LocalDateTime.now();
        int i = endTime.getSecond() - startTime.getSecond();
        System.out.println("use time:"+i);

    }

    @Autowired
    private IEmailNotifyService emailNotifyService;

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
            System.out.println("c1:"+subscription);
            // Wait a couple minutes, then poll the server for new events.
            String subscriptionId = subscription.getId();
            System.out.println("subcriptionId:"+subscriptionId);

            //获取
            PullSubscription containerByKey = SubscriptionContainer.getSubscriptionByKey();
            System.out.println("c:"+containerByKey);

            GetEventsResults events = subscription.getEvents();
            System.out.println("----");
            // Loop through all item-related events.
            for (ItemEvent itemEvent : events.getItemEvents()) {
                if (itemEvent.getEventType() == EventType.NewMail) {
                    EmailMessage message = EmailMessage.bind(EwsContainer.defaultExchangeService(), itemEvent.getItemId());
                    if(message.getHasAttachments()){
                        AttachmentCollection attachments = message.getAttachments();
                        System.out.println("attachments:"+attachments);
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

// Loop through all folder-related events.
//        for (FolderEvent folderEvent : events.getFolderEvents()) {
//            if (folderEvent.getEventType() == EventType.Created) {
//                Folder folder1 = Folder.bind(EwsContainer.defaultExchangeService(), folderEvent.getFolderId());
//            } else if (folderEvent.getEventType() == EventType.Deleted) {
//                System.out.println("folder  deleted"+ folderEvent.getFolderId().getUniqueId());
//            }
//        }

    }
//    @Test
//    public void testConnStreamNotify1(){
//        List<FolderId> folder = new ArrayList<FolderId>();
//        FolderId folderId = null;
//        try {
//            folderId = new FolderId("AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAWrq7QAAAA==");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////            FolderId foldIdEntity = FolderId.getFolderIdFromString(FolderNameEnum.ATTACH_UN.getCode());
////            folder.add(foldIdEntity);
//        folder.add(folderId);
//        try {
//            IAsyncResult asyncResult = EwsContainer.defaultExchangeService().beginSubscribeToStreamingNotifications(new EmailAttachCallBack(), null, folder, EventType.NewMail);
////            IAsyncResult subscription = EwsContainer.defaultExchangeService().beginSubscribeToPullNotifications(new EmailAttachCallBack(), null, folder, 5, null, EventType.NewMail, EventType.Created, EventType.Deleted);
//
//            boolean completeSynchronously = asyncResult.getCompleteSynchronously();
//            System.out.println("complete::::::" + completeSynchronously);
//
//            StreamingSubscription subscription = EwsContainer.defaultExchangeService().endSubscribeToStreamingNotifications(asyncResult);
////            StreamingSubscription subscription = EwsContainer.defaultExchangeService().subscribeToStreamingNotifications(folder, EventType.NewMail);
//            //StreamingSubscriptionConnection(service,lifetime[minutes])
//            StreamingSubscriptionConnection conn = new StreamingSubscriptionConnection(EwsContainer.defaultExchangeService(), 30);
//            conn.addSubscription(subscription);
//            conn.addOnNotificationEvent(emailNotifyDelegate);
//            conn.addOnDisconnect(emailSubscriptionErrorDelegate);
//            conn.open();
//
//            Thread.sleep(30000);
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("end........");
//
//
//
//    }
//    @Test
//    public void pushEvent(){
//
//        List<FolderId> folders = new ArrayList<FolderId>();
//        FolderId folderId = null;
//        try {
//            folderId = new FolderId("AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAWrq7QAAAA==");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////            FolderId foldIdEntity = FolderId.getFolderIdFromString(FolderNameEnum.ATTACH_UN.getCode());
////            folder.add(foldIdEntity);
//        folders.add(folderId);
//        try {
//            IAsyncResult result = EwsContainer.defaultExchangeService().beginSubscribeToPushNotifications(null, null, folders, new URI("http://localhost:93/notify"), 5, null, EventType.NewMail, EventType.Created, EventType.Deleted);
//            Object o = result.get();
//
//            System.out.println(o);
//            Thread.sleep(20000);
//            PushSubscription subscription =EwsContainer.defaultExchangeService().endSubscribeToPushNotifications(result);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    @Test
    public void pushNotify() throws Exception {
        // Subscribe to push notifications on the Inbox folder, and only listen
// to "new mail" events.
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
        PushSubscription pushSubscription = EwsContainer.defaultExchangeService()
                .subscribeToPushNotifications(folders,
                        new URI("http://localhost:93/notify"),
                        5,
                        null,
                        EventType.NewMail
                        );

    }
}
