package com.example.easyruledemo.task;

import com.example.easyruledemo.container.SubscriptionContainer;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.enums.ItemActionType;
import com.example.easyruledemo.event.MailEventHandler;
import com.example.easyruledemo.rules.MailEventsThread;
import com.example.easyruledemo.service.IEwsEmailService;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.notification.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-02 21:32
 */
@Component
@Slf4j
public class EmailEventTask {

//    int executeThisDay = 0;

    /**
     * 是否支持动态监听新加入数据库的邮箱
     */
    //    @Value() todo
    private boolean canDynamicInsertNewOneMail;

    @Autowired
    private IEwsEmailService ewsEmailService;

    /**
     * 每天凌晨1点执行一次
     * fixme 执行初始化订阅前,需要一次性创建被监测邮箱的初始化文件夹
     */
//    @Scheduled(cron = "0 0 1 * * ?")
//    @PostConstruct
    public void initialAttachSubscriptionToday() {
        log.info("开始初始化今日邮箱订阅监听");
        Integer size = SubscriptionContainer.initialSubscriptionToday(ewsEmailService
                .getMailConfigList(EwsMailEntity.builder().build(), ItemActionType.D));
        log.info("今日邮箱订阅监听完成,监听数量:{}个", size);
    }

    @Scheduled(cron = "*/30 * * * * ?")
    public void streamEmailEventPoll(){
        List<EwsMailEntity> mailConfigList = ewsEmailService
                .getMailConfigList(EwsMailEntity.builder().build(),
//                    /*邮件监听类型为下载以及下载并拷贝*/ItemActionType.D,ItemActionType.DC);
                        /*邮件监听类型为下载以及下载并拷贝*/ItemActionType.D);
        log.info("emailAttachEventPoll emailList:{}", mailConfigList);
        for (EwsMailEntity mailConfig : mailConfigList) {
            log.info("mailConfig in event task:{}", mailConfig);
//                PullSubscription subscription = SubscriptionContainer.initialSubscriptionToday(mailConfig);
//            PullSubscription subscription = SubscriptionContainer.getSubscriptionToday(mailConfig);
//                if (!(SubscriptionContainer.getPullSubscriptionMap().size() > 0)) {
//                    log.info("如果subscription监听未初始化,则不进行事件处理");
//                    return;
//                }
            StreamingSubscriptionConnection streamConn = SubscriptionContainer.getEmailStreamSubcriptionConn(mailConfig, 30);
            StreamingSubscription subscription = SubscriptionContainer.getStreamSubscription(mailConfig);


            GetEventsResults events = null;
            try {
                events = subscription.getService().getEvents(subscription.getId(), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(events!=null){
                Iterable<ItemEvent> itemEvents = events.getItemEvents();
                log.info("itemEvents hasNext:{}",itemEvents.iterator().hasNext());

                log.info("subscription emailEventTask:{}",subscription);
                if(subscription==null){
                    log.info("如果subscription监听未初始化,则不进行事件处理");
                    return;
                }
            }

//                MailEventHandler.event(mailConfig);
//            MailEventsThread mailEventsThread = new MailEventsThread(mailConfig, subscription);
//            mailEventsThread.start();
        }
    }
    /**
     * todo 将监听事件分离化
     * 邮件监听
     */
//    @Scheduled(cron = "*/30 * * * * ?")
    public void emailAttachEventPoll() {
//        if(!(SubscriptionContainer.getInitialCount()>0)){
//            log.info("如果subscription监听未初始化,则不进行事件处理");
//            return;
//        }
        try {
            List<EwsMailEntity> mailConfigList = ewsEmailService
                    .getMailConfigList(EwsMailEntity.builder().build(),
//                    /*邮件监听类型为下载以及下载并拷贝*/ItemActionType.D,ItemActionType.DC);
                            /*邮件监听类型为下载以及下载并拷贝*/ItemActionType.D, ItemActionType.DC);
            log.info("emailAttachEventPoll emailList:{}", mailConfigList);
            for (EwsMailEntity mailConfig : mailConfigList) {
                log.info("mailConfig in event task:{}", mailConfig);
//                PullSubscription subscription = SubscriptionContainer.initialSubscriptionToday(mailConfig);
                PullSubscription subscription = SubscriptionContainer.getSubscriptionToday(mailConfig);
//                if (!(SubscriptionContainer.getPullSubscriptionMap().size() > 0)) {
//                    log.info("如果subscription监听未初始化,则不进行事件处理");
//                    return;
//                }
                log.info("subscription emailEventTask:{}",subscription);
                if(subscription==null){
                    log.info("如果subscription监听未初始化,则不进行事件处理");
                    return;
                }
//                MailEventHandler.event(mailConfig);
                MailEventsThread mailEventsThread = new MailEventsThread(mailConfig, subscription);
                mailEventsThread.start();
            }
//            executeThisDay++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 这种方法不适用在这里
     */
//    @Scheduled(cron = "*/30 * * * * ?")
//    public void emailEventConn(){
//        try {
//            StreamingSubscriptionConnection subConn = SubscriptionContainer.initialAndGetStreamingSubscriptionConn();
//            subConn.open();
//            subConn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
