package com.example.easyruledemo.container;

import com.example.easyruledemo.delegate.EmailNotifyDelegate;
import com.example.easyruledemo.delegate.EmailSubscriptionErrorDelegate;
import com.example.easyruledemo.service.IEwsFolderService;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.enumeration.notification.EventType;
import microsoft.exchange.webservices.data.notification.PullSubscription;
import microsoft.exchange.webservices.data.notification.StreamingSubscription;
import microsoft.exchange.webservices.data.notification.StreamingSubscriptionConnection;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhangQi
 * @Date: 2021-07-02 16:49
 * 获取订阅container
 */
@Slf4j
@Component
public class SubscriptionContainer {
    // 单例map
    private static Map<String, PullSubscription> pullSubscriptionMap = new HashMap<>();
    private static Map<String, StreamingSubscription> streamSubscriptionMap = new HashMap<>();

    //日级别
    final private static String SUBSCRIPTION_KEY_PREFIX = "one_day_subscription";
    //分钟级别
    final private static String STREAM_SUBSCRIPTION_KEY_PREFIX = "minutes_subscription";

    final private static int ONE_DAY_MINUTES = 1440;
    final private static int ONE_MINUTES = 1;

    //TODO 先管理一个最新的 之后可以存储起来这些key (redis,oracle) 但一般用一个最新的 获取今天的
    public static PullSubscription getSubscriptionByKey() {
        return pullSubscriptionMap.get(SUBSCRIPTION_KEY_PREFIX+LocalDate.now());
    }

    private static IEwsFolderService ewsFolderService;

    @Autowired
    public void setEwsFolderService(IEwsFolderService ewsFolderService) {
        SubscriptionContainer.ewsFolderService = ewsFolderService;
    }

    //初始化并存放
    public static PullSubscription initialOrGetSubscriptionToday() {
        String todaySubscriptionKey = SUBSCRIPTION_KEY_PREFIX + LocalDate.now();
        log.info("todaySubscriptionKey is:"+todaySubscriptionKey);
        if (pullSubscriptionMap.get(todaySubscriptionKey) != null) {
            return pullSubscriptionMap.get(todaySubscriptionKey);
        }else{
            pullSubscriptionMap.clear();
            log.info("empty the subscriptionMap:{}",pullSubscriptionMap.size());
        }
        PullSubscription emailNotifySubscription =
                getEmailNotifySubscription(ewsFolderService.getWatchingFolderListForTest(), ONE_DAY_MINUTES);
        pullSubscriptionMap.put(todaySubscriptionKey, emailNotifySubscription);
        return emailNotifySubscription;
    }

    /**
     * @param foldersBeWatching 监测该文件list中的事件
     * @param timeoutMinutes    监测时长挂起时效
     * @return
     */
    public static PullSubscription getEmailNotifySubscription(List<FolderId> foldersBeWatching, int timeoutMinutes) {
        try {
            //挂起一个订阅,有效时长timeoutMinutes 之后通过轮询查看事件
            PullSubscription pullSubscription = EwsContainer.defaultExchangeService().subscribeToPullNotifications(foldersBeWatching, timeoutMinutes
                    /* timeOut: the subscription will end if the server is not polled within 5 minutes. */, null /* watermark: null to start a new subscription. */,
                    EventType.NewMail, EventType.Created, EventType.Deleted);
            return pullSubscription;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("get subscription error:" + e);
        }
    }

    ////////another subscription

    private static EmailNotifyDelegate emailNotifyDelegate;
    private static EmailSubscriptionErrorDelegate emailSubscriptionErrorDelegate;

    @Autowired
    public void setEmailNotifyDelegate(EmailNotifyDelegate emailNotifyDelegate) {
        SubscriptionContainer.emailNotifyDelegate = emailNotifyDelegate;
    }

    @Autowired
    public void setEmailSubscriptionErrorDelegate(EmailSubscriptionErrorDelegate emailSubscriptionErrorDelegate) {
        SubscriptionContainer.emailSubscriptionErrorDelegate = emailSubscriptionErrorDelegate;
    }

    //初始化并存放
    public static StreamingSubscriptionConnection initialAndGetStreamingSubscriptionConn() {
        //TODO 比较key时间,每次30分钟
//        String minutesSubscriptionKey = STREAM_SUBSCRIPTION_KEY_PREFIX + LocalDateTime.now();
//        log.info("minutesSubscriptionKey is:"+minutesSubscriptionKey);
//        if (streamSubscriptionMap.get(minutesSubscriptionKey) != null) {
////            return pullSubscriptionMap.get(todaySubscriptionKey);
//        }else{
//            pullSubscriptionMap.clear();
//            log.info("empty the subscriptionMap:{}",pullSubscriptionMap.size());
//        }
        StreamingSubscriptionConnection emailNotifySubscriptionInMinutes =
                getEmailStreamSubcriptionConn(ewsFolderService.getWatchingFolderListForTest(), ONE_MINUTES);
//        pullSubscriptionMap.put(todaySubscriptionKey, emailNotifySubscription);
//        return emailNotifySubscription;
        return emailNotifySubscriptionInMinutes;
    }

    /**
     * 获取stream的监测连接
     * @param foldersBeWatching
     * @param lifeTimeMinutes
     * @return
     */
    public static StreamingSubscriptionConnection getEmailStreamSubcriptionConn(List<FolderId> foldersBeWatching, int lifeTimeMinutes){
        StreamingSubscriptionConnection conn = null;
        try {
            StreamingSubscription subscription = EwsContainer.defaultExchangeService().subscribeToStreamingNotifications(foldersBeWatching, EventType.NewMail);
            //StreamingSubscriptionConnection(service,lifetime[minutes])
            conn = new StreamingSubscriptionConnection(EwsContainer.defaultExchangeService(), lifeTimeMinutes);
            conn.addSubscription(subscription);
            conn.addOnNotificationEvent(emailNotifyDelegate);
            conn.addOnDisconnect(emailSubscriptionErrorDelegate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
//        conn.open();
////            Thread.sleep(30000);
//        conn.close();
    }
}

