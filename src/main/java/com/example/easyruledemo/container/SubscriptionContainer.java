package com.example.easyruledemo.container;

import com.example.easyruledemo.delegate.EmailNotifyDelegate;
import com.example.easyruledemo.delegate.EmailSubscriptionErrorDelegate;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.service.IEwsEmailService;
import com.example.easyruledemo.service.IEwsFolderService;
import com.example.easyruledemo.service.ISubscriptionService;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.enumeration.notification.EventType;
import microsoft.exchange.webservices.data.notification.PullSubscription;
import microsoft.exchange.webservices.data.notification.StreamingSubscription;
import microsoft.exchange.webservices.data.notification.StreamingSubscriptionConnection;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhangQi
 * @Date: 2021-07-02 16:49
 * 获取事件订阅container,这里也是不同邮件来获取多个不同的事件订阅
 */
@Slf4j
@Component
public class SubscriptionContainer {
    // 单例map
    private static Map<String, PullSubscription> pullSubscriptionMap = new HashMap<>();
    private static Map<String, StreamingSubscription> streamSubscriptionMap = new HashMap<>();
    private static Map<String, PullSubscription> customSubscriptionMap = new HashMap<>();

    //日级别
    final private static String SUBSCRIPTION_KEY_INIT = "one_day_subscription";
    //分钟级别
    final private static String STREAM_SUBSCRIPTION_KEY_INIT = "minutes_subscription";
    //定制级别
    final private static String CUSTOM_INIT = "custom_subscription";


    final private static int ONE_DAY_MINUTES = 1440;
    final private static int ONE_MINUTES = 1;

    private static int initialCount = 0;

    //TODO 先管理一个最新的 之后可以存储起来这些key (redis,oracle) 但一般用一个最新的 获取今天的
    public static PullSubscription getSubscriptionByKey() {
        return pullSubscriptionMap.get(SUBSCRIPTION_KEY_INIT + LocalDate.now());
    }

    public static int getInitialCount() {
        return initialCount;
    }

    private static IEwsFolderService ewsFolderService;

    private static IEwsEmailService ewsEmailService;

    private static ISubscriptionService subcriptionService;

    @Autowired
    public void setEwsFolderService(IEwsFolderService ewsFolderService) {
        SubscriptionContainer.ewsFolderService = ewsFolderService;
    }

    @Autowired
    public void setEwsEmailService(IEwsEmailService ewsEmailService) {
        SubscriptionContainer.ewsEmailService = ewsEmailService;
    }

    @Autowired
    public void setSubcriptionService(ISubscriptionService subcriptionService) {
        SubscriptionContainer.subcriptionService = subcriptionService;
    }

    /**
     * todo 可以的话需将todayKey放入redis存放一天
     * 初始化订阅subscriptionMap,如果今日的map没有生成,则要生成今日的subscription订阅根据每个邮件
     * 此时如果设定的定时任务时间不标准,就要先将之前的进行unsubscribe取消订阅
     * 再用今日时间做为其中key的一部分生成新的subscription订阅
     */
    public static void initialSubcriptionMap(){
        long todayKey = pullSubscriptionMap.keySet().stream()
                .filter(k ->k.indexOf(String.valueOf(LocalDate.now())) > -1)
                .count();
        if (todayKey == 0) {
            long unsubscribeCount = pullSubscriptionMap.keySet().stream()
                    .map(k -> {
                        try {
                            pullSubscriptionMap.get(k).unsubscribe();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return 0;
                        }
                        return 1;
                    })
                    .count();
            pullSubscriptionMap.clear();
            initialCount = 0;
            log.info("unsubscribeCount is:{}, and empty the subscriptionMap:{}, and set initialCount to 0",
                    unsubscribeCount, pullSubscriptionMap.size());
        }
    }

    /**
     * 不判断,直接取消全部订阅
     */
    public static void unsubscriptionAll(){


//        List<EwsMailEntity> mailConfigList = ewsEmailService
//                .getMailConfigList(EwsMailEntity.builder().build());
        log.info("pullSubscriptionSize:{}",pullSubscriptionMap.size());
        long unsubscribeCount = pullSubscriptionMap.keySet().stream()
                .map(k -> {
                    try {
                        log.info("begin to unsubscribe");
                        pullSubscriptionMap.get(k).unsubscribe();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                    return 1;
                })
                .count();
        pullSubscriptionMap.clear();
        initialCount = 0;
        log.info("unsubscribeCount is:{}, and empty the subscriptionMap:{}, and set initialCount to 0",
                unsubscribeCount, pullSubscriptionMap.size());
    }

    /**
     * 取消订阅,根据传入订阅id
     * @param subscriptionId
     */
    public static void unsubscriptionBySubcriptionId(String subscriptionId,EwsMailEntity ewsMail){
        Integer subscriptionCode = subcriptionService.unSubscriptionById(subscriptionId, ewsMail);
        if(subscriptionCode>0){
            log.info("取消订阅DONE");
        }
    }

    /**
     * 取消订阅,相应清除redis中订阅id.订阅id每日更新一次,如果没有进行主动替换,则需要进行手动替换.
     * @param ewsMail
     */
    public static void unsubscriptionByCache(EwsMailEntity ewsMail){
        Integer subscriptionCode = subcriptionService.unSubscriptionByMailCache(ewsMail);
        if(subscriptionCode>0){
            log.info("取消订阅DONE");
        }
    }

    /**
     * 将接收方邮箱mailConfig传入初始化监听pullSubscription
     * 初始化并存放 todo 定时每天零点几分钟后开始初始化(在task中使用固定的cron)
     * @param mailConfigList
     * @return
     */
    public static Integer initialSubscriptionToday(List<EwsMailEntity> mailConfigList) {
        initialSubcriptionMap();
        String todaySubscriptionKeyPrefix = SUBSCRIPTION_KEY_INIT + LocalDate.now();
        PullSubscription emailNotifySubscription = null;
        int count = 0;
        for (EwsMailEntity mailConfig : mailConfigList) {
            emailNotifySubscription = getEmailNotifySubscription(ONE_DAY_MINUTES, mailConfig);
            //再拼接邮件email,邮件password为key的后缀拼接
            String keySuffix = mailConfig.getEmail() + mailConfig.getPassword();
            log.info("a new subscribe is coming : {}",todaySubscriptionKeyPrefix+keySuffix);
            pullSubscriptionMap.put(todaySubscriptionKeyPrefix + keySuffix, emailNotifySubscription);
            count++;
        }
        initialCount = count;
        return count;
    }

    /**
     * 当任务执行时,获取其中一个从map,如果不存在,则重新生产,并放入map
     * @param mailConfig
     * @return
     */
    public static PullSubscription getSubscriptionToday(EwsMailEntity mailConfig) {
        String todaySubscriptionKeyFull = SUBSCRIPTION_KEY_INIT + LocalDate.now() + mailConfig.getEmail() + mailConfig.getPassword();
        log.info("todaySubscriptionKeyFull is:" + todaySubscriptionKeyFull);
        if (pullSubscriptionMap.get(todaySubscriptionKeyFull) != null) {
            return pullSubscriptionMap.get(todaySubscriptionKeyFull);
        }
        PullSubscription emailNotifySubscription =
                getEmailNotifySubscription(ONE_DAY_MINUTES, mailConfig);
        pullSubscriptionMap.put(todaySubscriptionKeyFull, emailNotifySubscription);
        return emailNotifySubscription;
    }

    /**
     * 获取订阅,根据定义的超时时间分钟数
     * @param mailConfig
     * @param timeoutMinutes
     * @return
     */
    public static PullSubscription getSubscriptionCustom(EwsMailEntity mailConfig,Integer timeoutMinutes){
        String customSubscriptionKeyFull = CUSTOM_INIT + LocalDate.now() + mailConfig.getEmail() + mailConfig.getPassword();
        log.info("customSubscriptionKeyFull is:" + customSubscriptionKeyFull);
        if (customSubscriptionMap.get(customSubscriptionKeyFull) != null) {
            return customSubscriptionMap.get(customSubscriptionKeyFull);
        }
        PullSubscription emailNotifySubscription =
                getEmailNotifySubscription(timeoutMinutes, mailConfig);
        customSubscriptionMap.put(customSubscriptionKeyFull, emailNotifySubscription);
        return emailNotifySubscription;
    }

    public static PullSubscription initialSubscriptionToday(EwsMailEntity mailConfig) {
        String todaySubscriptionKeyPrefix = SUBSCRIPTION_KEY_INIT + LocalDate.now();
        String keySuffix = mailConfig.getEmail() + mailConfig.getPassword();
        PullSubscription emailNotifySubscription = null;
        emailNotifySubscription = getEmailNotifySubscription(ONE_DAY_MINUTES, mailConfig);
        pullSubscriptionMap.put(todaySubscriptionKeyPrefix + keySuffix, emailNotifySubscription);
        return emailNotifySubscription;
    }

    /**
     * @param foldersBeWatching 监测该文件list中的事件
     * @param timeoutMinutes    监测时长挂起时效
     * @return
     */
    @Deprecated
    public static PullSubscription getEmailNotifySubscription(List<FolderId> foldersBeWatching, int timeoutMinutes) {
        try {
            //挂起一个订阅,有效时长timeoutMinutes 之后通过轮询查看事件
            PullSubscription pullSubscription = EwsExContainer.defaultExchangeService().subscribeToPullNotifications(foldersBeWatching, timeoutMinutes
                    /* timeOut: the subscription will end if the server is not polled within 5 minutes. */, null /* watermark: null to start a new subscription. */,
                    EventType.NewMail, EventType.Created, EventType.Deleted);
            return pullSubscription;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("get subscription error:" + e);
        }
    }


    /**
     * 获取一个监听,之中调用的folderService,获取相关要监听的文件夹,这里只暂时监听邮件含附件标题含字眼的移入文件夹("待下载附件邮件")
     * //todo 需要设定不同监听时,就要设定多个pullSubscription,并设定根据eventType,和watchingFolderName加入key的拼接中
     * //todo 需要保存pullSubscription的id,将id保存到redis,其中以对应的INIT+LocalDate.now()+email+password为key,subscriptionId为value
     * @param timeoutMinutes 监测时长挂起时效
     * @param mailConfig 邮箱配置
     * @return
     */
    public static PullSubscription getEmailNotifySubscription(int timeoutMinutes, EwsMailEntity mailConfig) {
        try {
            //挂起一个订阅,有效时长timeoutMinutes 之后通过轮询查看事件
            PullSubscription pullSubscription = EwsExContainer.getExchangeService(mailConfig.getEmail(), mailConfig.getPassword())
                    //todo 这里的只是将字符串的folder unionId 转为folderId实体
//                    .subscribeToPullNotifications(ewsFolderService.getWatchingFolderByIds(mailConfig.getMailFolders().getFolderIds()),
                    //implementsteam@outlook.com邮箱的folderId
//                    .subscribeToPullNotifications(ewsFolderService.getWatchingFolderListForTest(),
                    //todo 通过ewsFolderService 根据topicId获取folderId实例list
                    .subscribeToPullNotifications(ewsFolderService.listFolderIdByTopicIdUnAction(mailConfig.getTopicId()),
                            timeoutMinutes
                            /* timeOut: the subscription will end if the server is not polled within 5 minutes. */, null /* watermark: null to start a new subscription. */,
                            EventType.NewMail, EventType.Created, EventType.Deleted);
            String pullSubscriptionId = pullSubscription.getId();
            log.info("pullSubscriptionId:{}",pullSubscriptionId);
            return pullSubscription;
        } catch (Exception e) {
            e.getMessage();
            if(e.getMessage()!=null){
                if (e.getMessage().indexOf("401")>-1){
                    log.error("邮箱权限错误,请核实邮箱账户有效性.");
                }
                if(e.getMessage().indexOf("have exceeded the available subscriptions for your account")>-1){
                    log.warn("创建subscribe错误,已经进行了订阅,请勿重复订阅.");
                    //如果重复订阅不抛出异常
                    return null;
                }
            }
            e.printStackTrace();
            throw new RuntimeException("get subscription error:" + e);
        }
    }

    ////////another subscription 暂时不使用

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
        //比较key时间,每次30分钟
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
     *
     * @param foldersBeWatching
     * @param lifeTimeMinutes
     * @return
     */
    public static StreamingSubscriptionConnection getEmailStreamSubcriptionConn(List<FolderId> foldersBeWatching, int lifeTimeMinutes) {
        StreamingSubscriptionConnection conn = null;
        try {
            StreamingSubscription subscription = EwsExContainer.defaultExchangeService().subscribeToStreamingNotifications(foldersBeWatching, EventType.NewMail);
            //StreamingSubscriptionConnection(service,lifetime[minutes])
            conn = new StreamingSubscriptionConnection(EwsExContainer.defaultExchangeService(), lifeTimeMinutes);
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

